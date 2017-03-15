package com.roboadvice.serviceImpl;

import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.model.Portfolio;
import com.roboadvice.model.User;
import com.roboadvice.repository.PortfolioRepository;
import com.roboadvice.repository.StrategyRepository;
import com.roboadvice.repository.UserRepository;
import com.roboadvice.service.PortfolioService;
import com.roboadvice.utils.Constant;
import com.roboadvice.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService{

    private PortfolioRepository portfolioRepository;
    private UserRepository userRepository;

    @Autowired
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, UserRepository userRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
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
    public Portfolio save(Portfolio p) {
        return portfolioRepository.save(p);
    }

    @Override
    public List<Portfolio> getAllYesterdayPortfolios() {
        return portfolioRepository.findAllPortfoliosToBeUpdatedByDate(LocalDate.now().minus(Period.ofDays(1)));
    }

    @Override
    public List<User> getAllYesterdayPortfoliosUsers() {
        return portfolioRepository.findAllPortfoliosUsersByDate(LocalDate.now().minus(Period.ofDays(1)));
    }

    @Override
    public List<Portfolio> findByUserAndDate(User u, LocalDate date) {
        return portfolioRepository.findByUserAndDate(u, date);
    }

    @Override
    public List<Portfolio> findByUser(User u) {
        return portfolioRepository.findByUser(u);
    }







}
