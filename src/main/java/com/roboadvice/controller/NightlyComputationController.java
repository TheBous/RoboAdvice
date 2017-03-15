package com.roboadvice.controller;

import com.jimmoores.quandl.DataSetRequest;
import com.jimmoores.quandl.QuandlSession;
import com.jimmoores.quandl.TabularResult;
import com.roboadvice.model.*;
import com.roboadvice.service.*;
import com.roboadvice.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.threeten.bp.Period;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NightlyComputationController {

    private ApiDataService apiDataService;
    private StrategyService strategyService;
    private AssetsClassService assetsClassService;
    private AssetsService assetsService;
    private PortfolioService portfolioService;

    @Autowired
    public NightlyComputationController(ApiDataService apiDataService, StrategyService strategyService, AssetsClassService assetsClassService, AssetsService assetsService, PortfolioService portfolioService) {
        this.apiDataService = apiDataService;
        this.strategyService = strategyService;
        this.assetsClassService = assetsClassService;
        this.assetsService = assetsService;
        this.portfolioService = portfolioService;
    }

    @Scheduled(cron ="0 0 5 * * *") //scheduled every day at 5:00 am
    @Transactional
    public void updateAPI(){
        System.out.println("============= NIGHTLY COMPUTATIONS STARTED =============\n");
        int j = 0;

        Iterable<Assets> assetsList = assetsService.findAll();
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
                apiDataService.insert(api);
                j++;
            }
        }
        System.out.println("*** NIGHTLY TASK 1: QUANDL API UPDATED! ***\n");

        insertPortfoliosForNewStrategiesFromNewUsers();
        updatePortfolios();
        insertPortfoliosForNewStrategiesFromOldUsers();

        System.out.println("============= NIGHTLY COMPUTATIONS FINISHED =============");
    }

    //CREATE NEW PORTFOLIO FOR NEW USERS
    @Transactional
    public void insertPortfoliosForNewStrategiesFromNewUsers(){
        BigDecimal investment = new BigDecimal(Constant.INITIAL_INVESTMENT);
        BigDecimal amount, value, units;

        ApiData api = new ApiData();
        Portfolio p = new Portfolio();
        //Portfolio p[] = new Portfolio[Constant.NUM_ASSETS];
        //int i=0;

        //CHECK IF THERE ARE NEW STRATEGIES FROM NEW USERS
        List<Strategy> newStrategies = strategyService.newStrategiesFromNewUsers();

        if(newStrategies!=null && !newStrategies.isEmpty()){
            Iterable<Assets> allAssets = assetsService.findAll();
            for(Strategy strategy : newStrategies){
                for(Assets asset : allAssets){
                    if(strategy.getAssetsClass().getId() == asset.getAssetsClass().getId()){
                        amount = Constant.percentage(investment, strategy.getPercentage());   //(10'000 * 65)/100
                        value = Constant.percentage(amount, asset.getAllocation_p());

                        api = apiDataService.findLatestValueByAsset(asset);

                        units = value.divide(api.getValue(),5, RoundingMode.HALF_UP);

                        p = new Portfolio();
                        p.setId(0);
                        p.setUser(strategy.getUser());
                        p.setDate(LocalDate.now());
                        p.setAssetsClass(strategy.getAssetsClass());
                        p.setAssets(asset);
                        p.setAmount(amount);
                        p.setValue(value);
                        p.setUnits(units);

                        portfolioService.save(p);
                        //i++;
                    }
                }
                //i=0;
            }
            System.out.println("*** NIGHTLY TASK 2: PORTFOLIOs CORRECTLY CREATED FOR NEW USERS! ***\n");
        }
        else{
            System.out.println("*** NIGHTLY TASK 2: NEW STRATEGIES FROM NEW USERS NOT FOUND! ***\n");
        }
    }

    //UPDATE PORTFOLIOS DAILY FOR OLD USERS THAT DID NOT CHANGE STRATEGY
    @Transactional
    public void updatePortfolios(){
        BigDecimal amount = new BigDecimal(0);
        List<BigDecimal> values = new ArrayList<>();
        ApiData api;
        int i=0;

        //prendo tutti i portfoli della giornata di ieri degli utenti che non hanno cambiato strategia
        //cioé utenti la cui strategia attiva ha data<ieri
        List<Portfolio> portfolios = portfolioService.getAllYesterdayPortfolios();

        if(!portfolios.isEmpty()) {
            List<User> users = portfolioService.getAllYesterdayPortfoliosUsers();
            Portfolio updatedPortfolio;
            Iterable<AssetsClass> assetsClasses = assetsClassService.findAll();
            Iterable<Assets> assets = assetsService.findAll();

            for(User user : users) {
                for (AssetsClass assetClass : assetsClasses) {
                    for (Assets asset : assets) {
                        if (asset.getAssetsClass().getId() == assetClass.getId()) {
                            for (Portfolio portfolio : portfolios) {
                                if (portfolio.getAssets().getId() == asset.getId() && portfolio.getUser().getId() == user.getId()) {
                                    api = apiDataService.findLatestValueByAsset(asset);
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
                                    updatedPortfolio.setDate(LocalDate.now());
                                    updatedPortfolio.setAssetsClass(assetClass);
                                    updatedPortfolio.setAssets(asset);
                                    updatedPortfolio.setAmount(amount);
                                    updatedPortfolio.setValue(values.get(i));
                                    updatedPortfolio.setUnits(portfolio.getUnits());

                                    portfolioService.save(updatedPortfolio);
                                    i++;
                                }
                            }
                        }
                    }
                    i = 0;
                    amount = BigDecimal.ZERO;
                    values.clear();
                }
            }
            System.out.println("*** NIGHTLY TASK 3: USER's PORTFOLIO HAS BEEN UPDATED! ***\n");
        }
        else{
            System.out.println("*** NIGHTLY TASK 3: NO PORTFOLIOs HAVE BEEN FOUND! ***\n");
        }
    }

    //CREATE NEW PORTFOLIO FOR OLD USERS THAT CHANGED STRATEGY
    @Transactional
    public void insertPortfoliosForNewStrategiesFromOldUsers(){
        BigDecimal investment = new BigDecimal(0);
        BigDecimal amount, value, units;
        List<Portfolio> oldPortfolio = new ArrayList<>();

        ApiData api = new ApiData();
        User user = new User();
        Portfolio p = new Portfolio();
        //Portfolio p[] = new Portfolio[Constant.NUM_ASSETS];
        //int i=0;

        //CHECK IF THERE ARE NEW STRATEGIES FROM OLD USERS
        List<Strategy> newStrategies = strategyService.newStrategiesFromOldUsers();

        if(newStrategies!=null && !newStrategies.isEmpty()){
            Iterable<Assets> allAssets = assetsService.findAll();
            for(Strategy strategy : newStrategies){
                user = strategy.getUser();
                oldPortfolio = portfolioService.findByUserAndDate(user, LocalDate.now().minus(java.time.Period.ofDays(1))); //portfolio di ieri dell'utente
                for(Assets asset : allAssets) {
                    for (Portfolio portfolio : oldPortfolio) {
                        if(asset.getId() == portfolio.getAssets().getId()){
                            api = apiDataService.findLatestValueByAsset(asset);
                            investment = investment.add(portfolio.getUnits().multiply(api.getValue())); //calcolo l'investimento iniziale del nuovo portfolio partendo dal vecchio
                        }

                    }
                }

                for(Assets asset : allAssets){
                    if(strategy.getAssetsClass().getId() == asset.getAssetsClass().getId()){

                        amount = Constant.percentage(investment, strategy.getPercentage());
                        value = Constant.percentage(amount, asset.getAllocation_p());

                        api = apiDataService.findLatestValueByAsset(asset);

                        units = value.divide(api.getValue(), 5, RoundingMode.HALF_UP);

                        p = new Portfolio();
                        p.setId(0);
                        p.setUser(strategy.getUser());
                        p.setDate(LocalDate.now());
                        p.setAssetsClass(strategy.getAssetsClass());
                        p.setAssets(asset);
                        p.setAmount(amount);
                        p.setValue(value);
                        p.setUnits(units);

                        portfolioService.save(p);
                        //i++;
                    }
                }
                //i=0;
                investment = BigDecimal.ZERO;
            }
            System.out.println("*** NIGHTLY TASK 4: PORTFOLIOs CORRECTLY CREATED FOR USERS THAT CHANGED STRATEGY! ***\n");
        }
        else{
            System.out.println("*** NIGHTLY TASK 4: NEW STRATEGIES FROM OLD USERS NOT FOUND! ***\n");
        }
    }

}
