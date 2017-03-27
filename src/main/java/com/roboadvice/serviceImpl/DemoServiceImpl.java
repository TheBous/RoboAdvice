package com.roboadvice.serviceImpl;

import com.roboadvice.dto.BacktestingDTO;
import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.model.*;
import com.roboadvice.repository.ApiDataRepository;
import com.roboadvice.repository.AssetsClassRepository;
import com.roboadvice.repository.AssetsRepository;
import com.roboadvice.repository.UserRepository;
import com.roboadvice.service.DemoService;
import com.roboadvice.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DemoServiceImpl implements DemoService{

    private UserRepository userRepository;
    private AssetsRepository assetsRepository;
    private AssetsClassRepository assetsClassRepository;
    private ApiDataRepository apiDataRepository;

    @Autowired
    public DemoServiceImpl(UserRepository userRepository, AssetsRepository assetsRepository, AssetsClassRepository assetsClassRepository, ApiDataRepository apiDataRepository) {
        this.userRepository = userRepository;
        this.assetsRepository = assetsRepository;
        this.assetsClassRepository = assetsClassRepository;
        this.apiDataRepository = apiDataRepository;
    }

    @Override
    public List<BacktestingDTO> getBacktestingDemo(String userEmail, StrategyDTO strategyDTO, LocalDate startDate) {
        User u = userRepository.findByEmail(userEmail);
        if(u==null)
            return null;

        List<AssetsClass> assetsClassList = (List<AssetsClass>) assetsClassRepository.findAll();
        List<Assets> assetsList = (List<Assets>) assetsRepository.findAll();
        ApiData api;

        //Convert StrategyDTO to List of Strategy
        List<Strategy> strategyList = new ArrayList<>();
        Strategy strategy;
        for(int i=0;i<Constant.NUM_ASSETS_CLASS;i++){
            strategy = new Strategy();
            strategy.setAssetsClass(assetsClassList.get(i));
            strategy.setPercentage(strategyDTO.getPercentage(i+1));
            strategyList.add(strategy);
        }

        List<BacktestingDTO> backtestingDTOList = new ArrayList<>();
        BacktestingDTO bDTO;
        List<Portfolio> portfolioList = new ArrayList<>();
        Portfolio p;

        BigDecimal investment = new BigDecimal(Constant.INITIAL_INVESTMENT);
        BigDecimal amount=BigDecimal.ZERO, value=BigDecimal.ZERO, units=BigDecimal.ZERO;
        LocalDate endDate = LocalDate.now();
        int precision;
        if(endDate.isBefore(startDate.plusDays(90)))
            precision=1;
        else if(endDate.isBefore(startDate.plusDays(210)))
            precision=2;
        else precision=3;
        //Create first portfolio========================================================================
        for(Strategy str : strategyList){
            for(Assets asset : assetsList){
                if(str.getAssetsClass().getId() == asset.getAssetsClass().getId()){
                    amount = Constant.percentage(investment, str.getPercentage());
                    value = Constant.percentage(amount, asset.getAllocation_p());
                    api = apiDataRepository.findTopByAssetsAndDateLessThanEqualOrderByDateDesc(asset, startDate);
                    if (api != null)
                        units = value.divide(api.getValue(), 5, RoundingMode.HALF_UP);
                    else {
                        units = BigDecimal.ZERO;
                        value = BigDecimal.ZERO;
                    }
                    p = new Portfolio();
                    p.setDate(startDate.plusDays(1));
                    p.setAssetsClass(str.getAssetsClass());
                    p.setAssets(asset);
                    p.setAmount(amount);
                    p.setValue(value);
                    p.setUnits(units);
                    portfolioList.add(p);
                }
            }
        }
        //Update portfolio every day===================================================================
        int index=0;
        List<Portfolio> oldPortfolioList = new ArrayList<>();
        for(LocalDate date = startDate.plusDays(1); date.isBefore(endDate); date = date.plusDays(precision)){
            oldPortfolioList.clear();
            for( ;index<portfolioList.size();index++){
                oldPortfolioList.add(portfolioList.get(index));
            }
            List<ApiData> apiDataList = apiDataRepository.findLatestApiValuesByDate(date.toString());
            for(int i=0;i<Constant.NUM_ASSETS;i++){
                //api = apiDataRepository.findTopByAssetsAndDateLessThanEqualOrderByDateDesc(assetsList.get(i), date);
                api = apiDataList.get(Constant.NUM_ASSETS-i-1);
                value = oldPortfolioList.get(i).getUnits().multiply(api.getValue());
                p = new Portfolio();
                p.setDate(date.plusDays(1));
                p.setUnits(oldPortfolioList.get(i).getUnits());
                p.setValue(value);
                portfolioList.add(p);
            }
        }
        //Create list of BacktestingDTO====================================================================
        for(int i=0; i<portfolioList.size();i+=Constant.NUM_ASSETS) {
            bDTO = new BacktestingDTO();
            bDTO.setDate(portfolioList.get(i).getDate());
            bDTO.setTotalAmount(BigDecimal.ZERO);
            for(int j=i;j<i+Constant.NUM_ASSETS && j<portfolioList.size();j++) {
                bDTO.setTotalAmount(bDTO.getTotalAmount().add(portfolioList.get(j).getValue()));
            }
            backtestingDTOList.add(bDTO);
        }
        return  backtestingDTOList;
    }
}
