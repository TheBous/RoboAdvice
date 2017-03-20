package com.roboadvice.serviceImpl;

import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.model.*;
import com.roboadvice.repository.*;
import com.roboadvice.service.PortfolioService;
import com.roboadvice.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService{

    private PortfolioRepository portfolioRepository;
    private UserRepository userRepository;
    private StrategyRepository strategyRepository;
    private AssetsRepository assetsRepository;
    private ApiDataRepository apiDataRepository;
    private AssetsClassRepository assetsClassRepository;

    @Autowired
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, UserRepository userRepository, StrategyRepository strategyRepository,
                                AssetsRepository assetsRepository, ApiDataRepository apiDataRepository,
                                AssetsClassRepository assetsClassRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.strategyRepository = strategyRepository;
        this.assetsRepository = assetsRepository;
        this.apiDataRepository = apiDataRepository;
        this.assetsClassRepository = assetsClassRepository;
    }

    @Override
    public PortfolioDTO getCurrent(String userEmail) {
        User u = userRepository.findByEmail(userEmail);
        if(u != null){
            List<Portfolio> portfolioList = portfolioRepository.getCurrent(u);
            if(portfolioList != null && !portfolioList.isEmpty()){
                PortfolioDTO portfolioDTO = new PortfolioDTO();
                portfolioDTO.setTotalAmount(BigDecimal.ZERO);
                for(Portfolio p : portfolioList){
                    portfolioDTO.setTotalAmount(portfolioDTO.getTotalAmount().add(p.getValue()));
                }
                for(int i = 0; i< Constant.NUM_ASSETS_CLASS; i++){
                    portfolioDTO.setAssetsClassAmount(i+1, portfolioList.get(i).getValue() );
                    portfolioDTO.setAssetsClassPercentage(i+1, portfolioDTO.getAssetsClassAmount(i+1).multiply(new BigDecimal(100)).divide(portfolioDTO.getTotalAmount(), 2, RoundingMode.HALF_UP));
                }
                portfolioDTO.setDate(portfolioList.get(0).getDate());
                return portfolioDTO;
            }
            else
                return null;
        }
        else
            return null;
    }


    @Override
    @Cacheable("portfolioFullHistory")
    public List<PortfolioDTO> getFullHistory(String userEmail) {

        User u = userRepository.findByEmail(userEmail);
        if(u != null) {
            List<Portfolio> portfolioList = portfolioRepository.fullHistoryByUser(u);

            if(portfolioList != null && !portfolioList.isEmpty()){
                List<PortfolioDTO> portfolioDTO_list = new ArrayList<>();
                PortfolioDTO pDTO = new PortfolioDTO();
                for(int i=0; i<portfolioList.size();i+=Constant.NUM_ASSETS_CLASS) {
                    pDTO = new PortfolioDTO();

                    pDTO.setDate(portfolioList.get(i).getDate());

                    pDTO.setTotalAmount(BigDecimal.ZERO);

                    for(int j=i;j<i+Constant.NUM_ASSETS_CLASS;j++) {
                        pDTO.setTotalAmount(pDTO.getTotalAmount().add(portfolioList.get(j).getAmount()));
                        pDTO.setAssetsClassAmount(portfolioList.get(j).getAssetsClass().getId(), portfolioList.get(j).getAmount());
                    }
                    for(int y=i;y<i+Constant.NUM_ASSETS_CLASS;y++) {
                        pDTO.setAssetsClassPercentage(portfolioList.get(y).getAssetsClass().getId(), portfolioList.get(y).getAmount().multiply(new BigDecimal(100).divide(pDTO.getTotalAmount(), 2, RoundingMode.HALF_UP)));
                    }
                    portfolioDTO_list.add(pDTO);
                }
                return portfolioDTO_list;
            }
            else {
                return null;
            }
        }
        else
            return null;


    }

    @Override
    public List<PortfolioDTO> getHistoryByDates(String userEmail, LocalDate fromDate, LocalDate toDate) {
        User u = userRepository.findByEmail(userEmail);
        if(u != null){
            List<Portfolio> portfolioList = portfolioRepository.historyByUserAndDates(u, fromDate.toString(), toDate.toString());

            if (portfolioList != null && !portfolioList.isEmpty()) {
                List<PortfolioDTO> portfolioDTO_list = new ArrayList<>();

                PortfolioDTO pDTO = new PortfolioDTO();
                for(int i=0; i<portfolioList.size();i+=Constant.NUM_ASSETS_CLASS) {
                    pDTO = new PortfolioDTO();

                    pDTO.setDate(portfolioList.get(i).getDate());

                    pDTO.setTotalAmount(BigDecimal.ZERO);

                    for (int j = i; j < i + Constant.NUM_ASSETS_CLASS; j++) {
                        pDTO.setTotalAmount(pDTO.getTotalAmount().add(portfolioList.get(j).getAmount()));
                        pDTO.setAssetsClassAmount(portfolioList.get(j).getAssetsClass().getId(), portfolioList.get(j).getAmount());
                    }
                    for (int y = i; y < i + Constant.NUM_ASSETS_CLASS; y++) {
                        pDTO.setAssetsClassPercentage(portfolioList.get(y).getAssetsClass().getId(), portfolioList.get(y).getAmount().multiply(new BigDecimal(100).divide(pDTO.getTotalAmount(), 2, RoundingMode.HALF_UP)));
                    }
                    portfolioDTO_list.add(pDTO);
                }
                return portfolioDTO_list;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<PortfolioDTO> getBackTestingChart(String userEmail, String prec, int months) {
        User u = userRepository.findByEmail(userEmail);
        if(u==null)
            return null;

        List<Strategy> currentStrategyList = strategyRepository.findByUserAndActive(u, true);
        if(currentStrategyList==null || currentStrategyList.isEmpty())
            return null;

        int precision;
        if(prec.equals("weekly"))
            precision = 1;
        else
            precision = 4;

        List<PortfolioDTO> portfolioDTOList = new ArrayList<>();
        PortfolioDTO pDTO;

        List<Portfolio> portfolioList = new ArrayList<>();
        Portfolio p;

        BigDecimal investment = new BigDecimal(Constant.INITIAL_INVESTMENT);
        BigDecimal amount=BigDecimal.ZERO, value=BigDecimal.ZERO, units=BigDecimal.ZERO;
        LocalDate startDate = LocalDate.now().minusMonths(months);//LocalDate.parse("2017-01-01");
        LocalDate endDate = LocalDate.now();
        Iterable<Assets> assetsList = assetsRepository.findAll();
        Iterable<AssetsClass> assetsClassList = assetsClassRepository.findAll();
        ApiData api;

        //Create first portfolio========================================================================
        for(Strategy str : currentStrategyList){
            for(Assets asset : assetsList){
                if(str.getAssetsClass().getId() == asset.getAssetsClass().getId()){
                    amount = Constant.percentage(investment, str.getPercentage());
                    value = Constant.percentage(amount, asset.getAllocation_p());

                    api = apiDataRepository.findLatestValueByAssetAndDate(asset.getId(), startDate.toString());

                    if (api != null)
                        units = value.divide(api.getValue(), 5, RoundingMode.HALF_UP);
                    else {
                        units = BigDecimal.ZERO;
                        value = BigDecimal.ZERO;
                    }

                    p = new Portfolio();
                    //p.setId(0);
                    //p.setUser(str.getUser());
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
        System.out.println("First portfolio created");


        //Update portfolio every day===================================================================
        amount = BigDecimal.ZERO;
        int i=0, index=0;

        List<Portfolio> oldPortfolioList = new ArrayList<>();

        List<BigDecimal> values = new ArrayList<>();

        for(LocalDate date = startDate.plusDays(1); date.isBefore(endDate); date = date.plusWeeks(precision)){
            //System.out.println("Date: "+date.toString()+" - Size: "+portfolioList.size());
            oldPortfolioList.clear();
            for( ;index<portfolioList.size();index++){
                oldPortfolioList.add(portfolioList.get(index));
            }

            for(AssetsClass ac : assetsClassList){
                for(Assets asset : assetsList){
                    if(asset.getAssetsClass().getId() == ac.getId()){
                        for(Portfolio oldP : oldPortfolioList){
                            if(oldP.getAssets().getId() == asset.getId()){
                                //System.out.println(oldP.toString());
                                api = apiDataRepository.findLatestValueByAssetAndDate(asset.getId(), date.toString());
                                if(api != null)
                                    values.add(oldP.getUnits().multiply(api.getValue()));
                                else
                                    values.add(BigDecimal.ZERO);
                            }
                        }
                    }
                }

                for (BigDecimal v : values) {
                    amount = amount.add(v);
                }

                for(Assets asset : assetsList){
                    if(asset.getAssetsClass().getId() == ac.getId()){
                        for(Portfolio oldP : oldPortfolioList){
                            if(oldP.getAssets().getId() == asset.getId()){
                                p = new Portfolio();
                                //p.setId(0);
                                //p.setUser(oldP.getUser());
                                p.setDate(date.plusDays(1));
                                p.setAssetsClass(ac);
                                p.setAssets(asset);
                                p.setAmount(amount);
                                p.setValue(values.get(i));
                                p.setUnits(oldP.getUnits());

                                portfolioList.add(p);
                                i++;
                            }
                        }
                    }
                }
                i=0;
                amount = BigDecimal.ZERO;
                values.clear();
            }
            System.out.println("Portfolio created for date: "+date.toString());
        }

        //Create list of PortfolioDTO====================================================================
        for(i=0; i<portfolioList.size();i+=Constant.NUM_ASSETS) {
            pDTO = new PortfolioDTO();

            pDTO.setDate(portfolioList.get(i).getDate());

            pDTO.setTotalAmount(BigDecimal.ZERO);

            for(int j=i;j<i+Constant.NUM_ASSETS && j<portfolioList.size();j++) {
                pDTO.setTotalAmount(pDTO.getTotalAmount().add(portfolioList.get(j).getValue()));
                //pDTO.setAssetsClassAmount(portfolioList.get(j).getAssetsClass().getId(), portfolioList.get(j).getAmount());
            }
            /*for(int y=i;y<i+Constant.NUM_ASSETS_CLASS;y++) {
                pDTO.setAssetsClassPercentage(portfolioList.get(y).getAssetsClass().getId(), portfolioList.get(y).getAmount().multiply(new BigDecimal(100).divide(pDTO.getTotalAmount(), 2, RoundingMode.HALF_UP)));
            }*/
            portfolioDTOList.add(pDTO);
        }

        return  portfolioDTOList;

    }
}
