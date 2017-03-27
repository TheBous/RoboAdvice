package com.roboadvice.serviceImpl;

import com.jimmoores.quandl.DataSetRequest;
import com.jimmoores.quandl.QuandlSession;
import com.jimmoores.quandl.TabularResult;
import com.roboadvice.model.*;
import com.roboadvice.repository.*;
import com.roboadvice.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.threeten.bp.Period;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NightlyTaskServiceImpl {

    private PortfolioRepository portfolioRepository;
    private ApiDataRepository apiDataRepository;
    private StrategyRepository strategyRepository;
    private AssetsClassRepository assetsClassRepository;
    private AssetsRepository assetsRepository;

    private long startTime;
    private long endTime;
    private LocalDate lastUpdate;

    @Autowired
    public NightlyTaskServiceImpl(PortfolioRepository portfolioRepository, ApiDataRepository apiDataRepository, StrategyRepository strategyRepository,
                                  AssetsClassRepository assetsClassRepository, AssetsRepository assetsRepository) {
        this.portfolioRepository = portfolioRepository;
        this.apiDataRepository = apiDataRepository;
        this.strategyRepository = strategyRepository;
        this.assetsClassRepository = assetsClassRepository;
        this.assetsRepository = assetsRepository;
    }

    @Scheduled(cron ="0 8 15 * * *") //scheduled every day at 5:00 am
    @Caching(evict = {@CacheEvict(cacheNames = "portfolioFullHistory", allEntries = true), @CacheEvict(cacheNames = "assetsClassTrend", allEntries = true)})
    @Transactional
    public void updateAPI(){
        startTime = System.currentTimeMillis();
        System.out.println("============= NIGHTLY COMPUTATIONS STARTED =============\n");
        int j;

        Iterable<Assets> assetsList = assetsRepository.findAll();
        ApiData api;

        for (Assets asset : assetsList) {
            QuandlSession session = QuandlSession.create("-Kcq55sDChWyAc2wcxcM");
            TabularResult tabularResult = session.getDataSet(
                    DataSetRequest.Builder
                            .of(asset.getApi_data_src())
                            .withColumn(asset.getColumn_num())
                            .withStartDate(org.threeten.bp.LocalDate.now().minus(Period.ofDays(5)))
                            .withEndDate(org.threeten.bp.LocalDate.now().minus(Period.ofDays(1)))
                            .build());
            j = 0;
            while (j < tabularResult.size()) {
                api = new ApiData(0, LocalDate.parse(tabularResult.get(j).getString(0)), BigDecimal.valueOf(tabularResult.get(j).getDouble(1)), asset);
                insertApiData(api);
                j++;
            }
        }
        System.out.println("*** NIGHTLY TASK 1: QUANDL API UPDATED! ***\n");

        lastUpdate = portfolioRepository.getLastUpdateDate();
        for( ;lastUpdate.isBefore(LocalDate.now());lastUpdate = lastUpdate.plusDays(1)) {
            insertPortfoliosForNewStrategiesFromNewUsers();
            updatePortfolios();
            insertPortfoliosForNewStrategiesFromOldUsers();
        }

        endTime = System.currentTimeMillis();
        System.out.println("============= NIGHTLY COMPUTATIONS FINISHED - Total time: "+((endTime-startTime)/1000)+"s =============");
    }

    //CREATE NEW PORTFOLIO FOR NEW USERS
    @Transactional
    private void insertPortfoliosForNewStrategiesFromNewUsers(){
        BigDecimal investment = new BigDecimal(Constant.INITIAL_INVESTMENT);
        BigDecimal amount, value, units;

        ApiData api = new ApiData();
        Portfolio p = new Portfolio();

        //CHECK IF THERE ARE NEW STRATEGIES FROM NEW USERS
        List<Strategy> newStrategies = getNewStrategiesFromNewUsers();

        if(newStrategies!=null && !newStrategies.isEmpty()){
            Iterable<Assets> allAssets = assetsRepository.findAll();
            for(Strategy strategy : newStrategies){
                for(Assets asset : allAssets){
                    if(strategy.getAssetsClass().getId() == asset.getAssetsClass().getId()){
                        amount = Constant.percentage(investment, strategy.getPercentage());   //(10'000 * 65)/100
                        value = Constant.percentage(amount, asset.getAllocation_p());

                        //api = apiDataRepository.findLatestValueByAsset(asset.getId());
                        api = apiDataRepository.findTopByAssetsAndDateLessThanEqualOrderByDateDesc(asset, lastUpdate);

                        units = value.divide(api.getValue(),5, RoundingMode.HALF_UP);

                        p = new Portfolio();
                        p.setId(0);
                        p.setUser(strategy.getUser());
                        //p.setDate(LocalDate.now());
                        p.setDate(lastUpdate.plusDays(1));
                        p.setAssetsClass(strategy.getAssetsClass());
                        p.setAssets(asset);
                        p.setAmount(amount);
                        p.setValue(value);
                        p.setUnits(units);

                        portfolioRepository.save(p);
                    }
                }
            }
            System.out.println("*** NIGHTLY TASK 2: PORTFOLIOs CORRECTLY CREATED FOR NEW USERS! - "+lastUpdate.plusDays(1).toString()+" ***\n");
        }
        else{
            System.out.println("*** NIGHTLY TASK 2: NEW STRATEGIES FROM NEW USERS NOT FOUND! - "+lastUpdate.plusDays(1).toString()+" ***\n");
        }
    }

    //UPDATE PORTFOLIOS DAILY FOR OLD USERS THAT DID NOT CHANGE STRATEGY
    @Transactional
    private void updatePortfolios(){
        BigDecimal amount = new BigDecimal(0);
        List<BigDecimal> values = new ArrayList<>();
        ApiData api;
        int i=0;

        //prendo tutti i portfoli della giornata di ieri degli utenti che non hanno cambiato strategia,
        //cioé utenti la cui strategia attiva ha data<last_update
        List<Portfolio> portfolios = portfolioRepository.findAllPortfoliosToBeUpdatedByDate(lastUpdate);
        //List<Portfolio> portfolios = portfolioRepository.findPortfoliosToBeUpdated();

        if(!portfolios.isEmpty()) {
            //List<User> users = portfolioRepository.findAllPortfoliosUsersByDate(LocalDate.now().minus(java.time.Period.ofDays(1)));
            List<User> users = portfolioRepository.findAllPortfoliosUsersByDate(portfolios.get(0).getDate());
            Portfolio updatedPortfolio;
            Iterable<AssetsClass> assetsClasses = assetsClassRepository.findAll();
            Iterable<Assets> assets = assetsRepository.findAll();

            List<Portfolio> portfolioList = new ArrayList<>();

            for(User user : users) {
                for (AssetsClass assetClass : assetsClasses) {
                    for (Assets asset : assets) {
                        if (asset.getAssetsClass().getId() == assetClass.getId()) {
                            for (Portfolio portfolio : portfolios) {
                                if (portfolio.getAssets().getId() == asset.getId() && portfolio.getUser().getId() == user.getId()) {
                                    //api = apiDataRepository.findLatestValueByAsset(asset.getId());
                                    api = apiDataRepository.findTopByAssetsAndDateLessThanEqualOrderByDateDesc(asset, lastUpdate);
                                    values.add(portfolio.getUnits().multiply(api.getValue()));
                                }
                            }
                        }
                    }

                    for (BigDecimal value : values) {
                        amount = amount.add(value);
                    }

                    for (Assets asset : assets) {
                        if (asset.getAssetsClass().getId() == assetClass.getId()) {
                            for (Portfolio portfolio : portfolios) {
                                if (portfolio.getAssets().getId() == asset.getId() && portfolio.getUser().getId() == user.getId()) {
                                    updatedPortfolio = new Portfolio();
                                    updatedPortfolio.setId(0);
                                    updatedPortfolio.setUser(portfolio.getUser());
                                    //updatedPortfolio.setDate(LocalDate.now());
                                    updatedPortfolio.setDate(lastUpdate.plusDays(1));
                                    updatedPortfolio.setAssetsClass(assetClass);
                                    updatedPortfolio.setAssets(asset);
                                    updatedPortfolio.setAmount(amount);
                                    updatedPortfolio.setValue(values.get(i));
                                    updatedPortfolio.setUnits(portfolio.getUnits());

                                    portfolioList.add(updatedPortfolio);
                                    //portfolioRepository.save(updatedPortfolio);
                                    i++;
                                }
                            }
                        }
                    }
                    i = 0;
                    amount = BigDecimal.ZERO;
                    values.clear();
                }

                if(checkRebalance(portfolioList)) {
                    rebalanceAndSave(portfolioList);
                }
                else{
                    for(Portfolio p : portfolioList){
                        portfolioRepository.save(p);
                    }
                }
                portfolioList.clear();
            }
            System.out.println("*** NIGHTLY TASK 3: USER's PORTFOLIO HAS BEEN UPDATED! - "+lastUpdate.plusDays(1).toString()+" ***\n");
        }
        else{
            System.out.println("*** NIGHTLY TASK 3: NO PORTFOLIOs HAVE BEEN FOUND! - "+lastUpdate.plusDays(1).toString()+" ***\n");
        }
    }

    //CREATE NEW PORTFOLIO FOR OLD USERS THAT CHANGED STRATEGY
    @Transactional
    private void insertPortfoliosForNewStrategiesFromOldUsers(){
        BigDecimal investment = new BigDecimal(0);
        BigDecimal amount, value, units;
        List<Portfolio> oldPortfolio = new ArrayList<>();

        ApiData api = new ApiData();
        User user = new User();
        Portfolio p = new Portfolio();

        //CHECK IF THERE ARE NEW STRATEGIES FROM OLD USERS
        List<Strategy> newStrategies = getNewStrategiesFromOldUsers();

        if(newStrategies!=null && !newStrategies.isEmpty()){
            Iterable<Assets> allAssets = assetsRepository.findAll();
            for(Strategy strategy : newStrategies){
                user = strategy.getUser();
                //oldPortfolio = portfolioRepository.findByUserAndDate(user, LocalDate.now().minus(java.time.Period.ofDays(1))); //portfolio di ieri dell'utente
                oldPortfolio = portfolioRepository.findByUserAndDate(user, strategy.getDate());
                for(Assets asset : allAssets) {
                    for (Portfolio portfolio : oldPortfolio) {
                        if(asset.getId() == portfolio.getAssets().getId()){
                            //api = apiDataRepository.findLatestValueByAsset(asset.getId());
                            api = apiDataRepository.findTopByAssetsAndDateLessThanEqualOrderByDateDesc(asset, lastUpdate);
                            investment = investment.add(portfolio.getUnits().multiply(api.getValue())); //calcolo l'investimento iniziale del nuovo portfolio partendo dal vecchio
                        }
                    }
                }

                for(Assets asset : allAssets){
                    if(strategy.getAssetsClass().getId() == asset.getAssetsClass().getId()){

                        amount = Constant.percentage(investment, strategy.getPercentage());
                        value = Constant.percentage(amount, asset.getAllocation_p());

                        //api = apiDataRepository.findLatestValueByAsset(asset.getId());
                        api = apiDataRepository.findTopByAssetsAndDateLessThanEqualOrderByDateDesc(asset, lastUpdate);

                        units = value.divide(api.getValue(), 5, RoundingMode.HALF_UP);

                        p = new Portfolio();
                        p.setId(0);
                        p.setUser(strategy.getUser());
                        //p.setDate(LocalDate.now());
                        p.setDate(lastUpdate.plusDays(1));
                        p.setAssetsClass(strategy.getAssetsClass());
                        p.setAssets(asset);
                        p.setAmount(amount);
                        p.setValue(value);
                        p.setUnits(units);

                        portfolioRepository.save(p);
                    }
                }
                investment = BigDecimal.ZERO;
            }
            System.out.println("*** NIGHTLY TASK 4: PORTFOLIOs CORRECTLY CREATED FOR USERS THAT CHANGED STRATEGY! - "+lastUpdate.plusDays(1).toString()+" ***\n");
        }
        else{
            System.out.println("*** NIGHTLY TASK 4: NEW STRATEGIES FROM OLD USERS NOT FOUND! - "+lastUpdate.plusDays(1).toString()+" ***\n");
        }
    }

    private ApiData insertApiData(ApiData ad){
        if(apiDataRepository.findByAssetsIdAndDate(ad.getAssets().getId(), ad.getDate()) == null)
            return apiDataRepository.save(ad);
        else
            return null;
    }

    private List<Strategy> getNewStrategiesFromNewUsers() {
        //List<Strategy> newStrategies = strategyRepository.findNewStrategies(LocalDate.now().minus(java.time.Period.ofDays(1)));
        List<Strategy> newStrategies = strategyRepository.findNewStrategies(lastUpdate);

        if (!newStrategies.isEmpty()){
            List<Strategy> oldUserStrategies = new ArrayList<>();
            List<Strategy> newStrategiesFromNewUsers = new ArrayList<>();

            for (Strategy strategy : newStrategies) {
                //se trovo strategia con active=0 && data precedente, significa che non è un nuovo utente ma è un utente che ha cambiato strategia
                oldUserStrategies = strategyRepository.findUserOldStrategies(strategy.getUser(), strategy.getDate());
                if (oldUserStrategies.isEmpty()) {
                    newStrategiesFromNewUsers.add(strategy);
                }
            }
            return newStrategiesFromNewUsers;
        }
        else {
            return null;
        }
    }

    private List<Strategy> getNewStrategiesFromOldUsers() {
        //List<Strategy> newStrategies = strategyRepository.findNewStrategies(LocalDate.now().minus(java.time.Period.ofDays(1)));
        List<Strategy> newStrategies = strategyRepository.findNewStrategies(lastUpdate);

        if (!newStrategies.isEmpty()){
            List<Strategy> oldUserStrategies = new ArrayList<>();
            List<Strategy> newStrategiesFromOldUsers = new ArrayList<>();

            for (Strategy strategy : newStrategies) {
                oldUserStrategies = strategyRepository.findUserOldStrategies(strategy.getUser(), strategy.getDate());
                if (!oldUserStrategies.isEmpty()) {
                    newStrategiesFromOldUsers.add(strategy);
                }
            }
            return newStrategiesFromOldUsers;
        }
        else {
            return null;
        }
    }

    private boolean checkRebalance(List<Portfolio> portfolioList){
        BigDecimal totalAmount = BigDecimal.ZERO, currentAssetsClassPercentage;
        for(Portfolio p : portfolioList)
            totalAmount = totalAmount.add(p.getValue());

        List<Strategy> strategyList = strategyRepository.findByUserAndActive(portfolioList.get(0).getUser(), true);

        for(Strategy str : strategyList){
            for(Portfolio p : portfolioList){
                if(str.getAssetsClass().getId() == p.getAssetsClass().getId()){
                    currentAssetsClassPercentage = p.getAmount().multiply(new BigDecimal(100)).divide(totalAmount, 2, RoundingMode.HALF_UP);

                    if(currentAssetsClassPercentage.compareTo(str.getPercentage().add(new BigDecimal(2))) > 0 ||
                       currentAssetsClassPercentage.compareTo(str.getPercentage().add(new BigDecimal(-2))) < 0) {
                        return true;
                    }
                    else break;
                }
            }
        }
        return false;
    }

    private void rebalanceAndSave(List<Portfolio> portfolioList){
        BigDecimal totalAmount = BigDecimal.ZERO, amount, value, assetWeight, units;
        for(Portfolio p : portfolioList)
            totalAmount = totalAmount.add(p.getValue());

        List<Strategy> strategyList = strategyRepository.findByUserAndActive(portfolioList.get(0).getUser(), true);
        List<Assets> assetsList = (List<Assets>) assetsRepository.findAll();
        ApiData api;

        for(Strategy str :strategyList){
            for(Assets asset : assetsList){
                if(str.getAssetsClass().getId() == asset.getAssetsClass().getId()){
                    for(Portfolio p : portfolioList) {
                        if (asset.getId() == p.getAssets().getId()) {
                            assetWeight = str.getPercentage().divide(new BigDecimal(100),5,RoundingMode.HALF_UP).multiply(asset.getAllocation_p().divide(new BigDecimal(100), 5, RoundingMode.HALF_UP));
                            api = apiDataRepository.findTopByAssetsAndDateLessThanEqualOrderByDateDesc(asset, lastUpdate);

                            amount = totalAmount.multiply(str.getPercentage()).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
                            value = totalAmount.multiply(assetWeight);
                            units = value.divide(api.getValue(), 5, RoundingMode.HALF_UP);

                            p.setAmount(amount);
                            p.setValue(value);
                            p.setUnits(units);
                            portfolioRepository.save(p);
                        }
                    }
                }
            }
        }
        System.out.println("*** Portfolio rebalanced for userID: "+portfolioList.get(0).getUser().getId()+" ***");
    }

}

