package com.roboadvice.serviceImpl;

import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.model.*;
import com.roboadvice.repository.*;
import com.roboadvice.service.StrategyService;
import com.roboadvice.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class StrategyServiceImpl implements StrategyService{

    private StrategyRepository strategyRepository;
    private UserRepository userRepository;
    private AssetsClassRepository assetsClassRepository;
    private AssetsRepository assetsRepository;
    private PortfolioRepository portfolioRepository;
    private ApiDataRepository apiDataRepository;

    @Autowired
    public StrategyServiceImpl(StrategyRepository strategyRepository, UserRepository userRepository, AssetsClassRepository assetsClassRepository, PortfolioRepository portfolioRepository, AssetsRepository assetsRepository, ApiDataRepository apiDataRepository) {
        this.strategyRepository = strategyRepository;
        this.userRepository = userRepository;
        this.assetsClassRepository = assetsClassRepository;
        this.portfolioRepository = portfolioRepository;
        this.assetsRepository = assetsRepository;
        this.apiDataRepository = apiDataRepository;
    }


    @Override
    @CacheEvict(cacheNames = "strategyFullHistory", allEntries = true)
    public StrategyDTO insert(String userEmail, StrategyDTO strategyDTO) {
        User u = userRepository.findByEmail(userEmail);
        if (u == null) {
            return null;
        }
        strategyRepository.setInactive(u);
        AssetsClass ac = null;
        Strategy str = null;

        for (int i = 1; i < Constant.NUM_ASSETS_CLASS+1; i++) {
            ac = assetsClassRepository.findById(i);
            str = new Strategy(0, strategyDTO.getName(), LocalDate.now(), true, strategyDTO.getPercentage(i), u, ac);
            strategyRepository.save(str);
        }
        StrategyDTO strDTO = strategyDTO;
        strDTO.setDate(LocalDate.now());
        strDTO.setActive(true);
        return strDTO;
    }

    @Override
    @CacheEvict(cacheNames = "strategyFullHistory", allEntries = true)
    public Boolean deletePendingStrategy(String userEmail) {
        User u = userRepository.findByEmail(userEmail);
        if (u==null)
            return false;

        int resp = strategyRepository.deleteActiveStrategyByUserAndDate(u, LocalDate.now());
        if(resp==Constant.NUM_ASSETS_CLASS){
            List<Strategy> strategies = strategyRepository.findLatestInactiveStrategy(u, new PageRequest(0, Constant.NUM_ASSETS_CLASS));
            for(Strategy s : strategies){
                s.setActive(true);
                strategyRepository.save(s);
            }
            return true;
        }
        else
            return false;
    }

    @Override
    public StrategyDTO getCurrentActiveStrategy(String userEmail) {
        StrategyDTO strDTO = new StrategyDTO();
        User u = userRepository.findByEmail(userEmail);
        if(u == null)
            return null;

        List<Strategy> strategies = strategyRepository.findByUserAndActive(u, true);
        if (!strategies.isEmpty()) {
            strDTO.setName(strategies.get(0).getName());
            strDTO.setDate(strategies.get(0).getDate());
            strDTO.setActive(strategies.get(0).getActive());
            for (Strategy strategy : strategies) {
                strDTO.setPercentage(strategy.getAssetsClass().getId(), strategy.getPercentage());
            }
            return strDTO;
        } else
            return null;
    }

    @Override
    public StrategyDTO getLastUsedStrategy(String userEmail) {

        StrategyDTO strDTO = new StrategyDTO();

        User u = userRepository.findByEmail(userEmail);
        if(u == null)
            return null;

        List<Strategy> strategies = strategyRepository.findLastUsedStrategy(u, new PageRequest(0, Constant.NUM_ASSETS_CLASS));
        if(strategies!=null && !strategies.isEmpty()){
            strDTO.setName(strategies.get(0).getName());
            strDTO.setDate(strategies.get(0).getDate());
            strDTO.setActive(strategies.get(0).getActive());
            for (Strategy strategy : strategies) {
                strDTO.setPercentage(strategy.getAssetsClass().getId(), strategy.getPercentage());
            }
            return strDTO;
        }
        else
            return null;
    }

    @Override
    @Cacheable("strategyFullHistory")
    public List<StrategyDTO> getFullHistoryByUser(String userEmail) {
        User u = userRepository.findByEmail(userEmail);
        if (u == null)
            return null;

        List<Strategy> strategyList = strategyRepository.fullHistoryByUser(u);
        if(strategyList!= null && !strategyList.isEmpty()) {
            StrategyDTO strDTO = new StrategyDTO();
            List<StrategyDTO> strategyDTOList = new ArrayList<>();

            for(int i=0;i<strategyList.size();i+=Constant.NUM_ASSETS_CLASS){
                strDTO = new StrategyDTO();
                strDTO.setName(strategyList.get(i).getName());
                strDTO.setDate(strategyList.get(i).getDate());
                strDTO.setActive(strategyList.get(i).getActive());
                for(int j=i;j<i+Constant.NUM_ASSETS_CLASS;j++){
                    strDTO.setPercentage(strategyList.get(j).getAssetsClass().getId(), strategyList.get(j).getPercentage());
                }
                strategyDTOList.add(strDTO);
            }
            return strategyDTOList;
        }
        else
            return null;
    }

    @Override
    public PortfolioDTO getAdvice(String userEmail, int strategyCode) {
        User u = userRepository.findByEmail(userEmail);
        if(u==null)
            return null;

        List<Strategy> currentStrategyList = strategyRepository.findByUserAndActive(u, true);
        if(currentStrategyList == null || currentStrategyList.isEmpty())
            return null;
        if(currentStrategyList.get(0).getDate().equals(LocalDate.now()))
            currentStrategyList = strategyRepository.findLastUsedStrategy(u, new PageRequest(0, Constant.NUM_ASSETS_CLASS));

        List<Strategy> strategyList=new ArrayList<>();
        List<AssetsClass> assetsClassList = (List<AssetsClass>) assetsClassRepository.findAll();
        Strategy s;
        if(strategyCode==0){
            s = new Strategy(0, "Bonds", LocalDate.now(), true, new BigDecimal(95), u, assetsClassList.get(0));
            strategyList.add(s);
            s = new Strategy(0, "Bonds", LocalDate.now(), true, new BigDecimal(0), u, assetsClassList.get(1));
            strategyList.add(s);
            s = new Strategy(0, "Bonds", LocalDate.now(), true, new BigDecimal(0), u, assetsClassList.get(2));
            strategyList.add(s);
            s = new Strategy(0, "Bonds", LocalDate.now(), true, new BigDecimal(5), u, assetsClassList.get(3));
            strategyList.add(s);
        }
        else if(strategyCode==1){
            s = new Strategy(0, "Income", LocalDate.now(), true, new BigDecimal(65), u, assetsClassList.get(0));
            strategyList.add(s);
            s = new Strategy(0, "Income", LocalDate.now(), true, new BigDecimal(10), u, assetsClassList.get(1));
            strategyList.add(s);
            s = new Strategy(0, "Income", LocalDate.now(), true, new BigDecimal(15), u, assetsClassList.get(2));
            strategyList.add(s);
            s = new Strategy(0, "Income", LocalDate.now(), true, new BigDecimal(10), u, assetsClassList.get(3));
            strategyList.add(s);
        }
        else if(strategyCode==2){
            s = new Strategy(0, "Balanced", LocalDate.now(), true, new BigDecimal(30), u, assetsClassList.get(0));
            strategyList.add(s);
            s = new Strategy(0, "Balanced", LocalDate.now(), true, new BigDecimal(30), u, assetsClassList.get(1));
            strategyList.add(s);
            s = new Strategy(0, "Balanced", LocalDate.now(), true, new BigDecimal(20), u, assetsClassList.get(2));
            strategyList.add(s);
            s = new Strategy(0, "Balanced", LocalDate.now(), true, new BigDecimal(20), u, assetsClassList.get(3));
            strategyList.add(s);
        }
        else if(strategyCode==3){
            s = new Strategy(0, "Growth", LocalDate.now(), true, new BigDecimal(20), u, assetsClassList.get(0));
            strategyList.add(s);
            s = new Strategy(0, "Growth", LocalDate.now(), true, new BigDecimal(60), u, assetsClassList.get(1));
            strategyList.add(s);
            s = new Strategy(0, "Growth", LocalDate.now(), true, new BigDecimal(10), u, assetsClassList.get(2));
            strategyList.add(s);
            s = new Strategy(0, "Growth", LocalDate.now(), true, new BigDecimal(10), u, assetsClassList.get(3));
            strategyList.add(s);
        }
        else if(strategyCode==4){
            s = new Strategy(0, "Stocks", LocalDate.now(), true, new BigDecimal(0), u, assetsClassList.get(0));
            strategyList.add(s);
            s = new Strategy(0, "Stocks", LocalDate.now(), true, new BigDecimal(100), u, assetsClassList.get(1));
            strategyList.add(s);
            s = new Strategy(0, "Stocks", LocalDate.now(), true, new BigDecimal(0), u, assetsClassList.get(2));
            strategyList.add(s);
            s = new Strategy(0, "Stocks", LocalDate.now(), true, new BigDecimal(0), u, assetsClassList.get(3));
            strategyList.add(s);
        }
        else return null;

        LocalDate startDate = currentStrategyList.get(0).getDate();
        LocalDate endDate = LocalDate.now();
        int precision;
        if(endDate.isBefore(startDate.plusDays(90)))
            precision=1;
        else if(endDate.isBefore(startDate.plusDays(210)))
            precision=2;
        else precision=3;

        BigDecimal startAmount = portfolioRepository.findTotalAmountByUserAndDate(u, startDate);
        if(startAmount==null)
            startAmount = new BigDecimal(10000);
        BigDecimal amount=BigDecimal.ZERO, value=BigDecimal.ZERO, units=BigDecimal.ZERO;
        List<Assets> assetsList = (List<Assets>) assetsRepository.findAll();
        ApiData api;
        List<Portfolio> portfolioList = new ArrayList<>();
        Portfolio p;

        //=====================Generate first portfolio and first amount
        for(Strategy str : strategyList){
            for(Assets asset : assetsList){
                if(str.getAssetsClass().getId() == asset.getAssetsClass().getId()){
                    amount = Constant.percentage(startAmount, str.getPercentage());
                    value = Constant.percentage(amount, asset.getAllocation_p());

                    //api = apiDataRepository.findLatestValueByAssetAndDate(asset.getId(), startDate.toString());
                    api = apiDataRepository.findTopByAssetsAndDateLessThanEqualOrderByDateDesc(asset, startDate);

                    if(api != null)
                        units = value.divide(api.getValue(), 5, RoundingMode.HALF_UP);
                    else{
                        units = BigDecimal.ZERO;
                        value = BigDecimal.ZERO;
                    }

                    p=new Portfolio();
                    p.setDate(startDate.plusDays(1));
                    p.setAssetsClass(str.getAssetsClass());
                    p.setAssets(asset);
                    p.setAmount(amount);
                    p.setUnits(units);

                    portfolioList.add(p);
                }
            }
        }

        //======================================Generate all the others portfolios
        amount = BigDecimal.ZERO;
        int index=0;
        List<Portfolio> oldPortfolioList = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();

        for(LocalDate date = startDate.plusDays(1);date.isBefore(endDate);date = date.plusDays(precision)){
            oldPortfolioList.clear();
            for( ;index<portfolioList.size();index++){
                oldPortfolioList.add(portfolioList.get(index));
            }

            List<ApiData> apiDataList= apiDataRepository.findLatestApiValuesByDate(date.toString());
            for(int i=0;i<Constant.NUM_ASSETS;i++){
                api = apiDataList.get(Constant.NUM_ASSETS-i-1);
                value = oldPortfolioList.get(i).getUnits().multiply(api.getValue());
                p = new Portfolio();
                p.setDate(date.plusDays(1));
                p.setUnits(oldPortfolioList.get(i).getUnits());
                p.setValue(value);
                portfolioList.add(p);
            }
        }

        PortfolioDTO pDTO = new PortfolioDTO();
        BigDecimal totalAmount=BigDecimal.ZERO;

        for(int j=portfolioList.size()-Constant.NUM_ASSETS;j<portfolioList.size();j++)
            totalAmount = totalAmount.add(portfolioList.get(j).getValue());

        pDTO.setTotalAmount(totalAmount);

        return pDTO;
    }
}
