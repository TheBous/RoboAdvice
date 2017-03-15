package com.roboadvice.serviceImpl;

import com.roboadvice.model.Portfolio;
import com.roboadvice.model.User;
import com.roboadvice.repository.PortfolioRepository;
import com.roboadvice.repository.StrategyRepository;
import com.roboadvice.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService{

    private PortfolioRepository portfolioRepository;
    private StrategyRepository strategyRepository;

    @Autowired
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, StrategyRepository strategyRepository) {
        this.portfolioRepository = portfolioRepository;
        this.strategyRepository = strategyRepository;
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


    @Override
    public List<Portfolio> getCurrent(User u) {
        return portfolioRepository.getCurrent(u);
    }

    @Override
    public List<Portfolio> fullHistoryByUser(User u) {
        return portfolioRepository.fullHistoryByUser(u);
    }

    @Override
    public List<Portfolio> historyByUserAndDates(User u, LocalDate from, LocalDate to) {
        return portfolioRepository.historyByUserAndDates(u, from.toString(), to.toString());
    }
}
