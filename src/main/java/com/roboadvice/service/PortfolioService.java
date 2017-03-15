package com.roboadvice.service;

import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.model.Portfolio;
import com.roboadvice.model.User;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PortfolioService {

    Portfolio save(Portfolio p);
    List<Portfolio> getAllYesterdayPortfolios();
    List<User> getAllYesterdayPortfoliosUsers();
    List<Portfolio> findByUserAndDate(User u, LocalDate date);
    List<Portfolio> findByUser(User u);
    List<Portfolio> getCurrent(User u);
    List<Portfolio> fullHistoryByUser(User u);
    List<Portfolio> historyByUserAndDates(User u, LocalDate from, LocalDate to);


}
