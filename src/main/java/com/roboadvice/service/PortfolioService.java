package com.roboadvice.service;

import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.model.Portfolio;
import com.roboadvice.model.User;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PortfolioService {


    PortfolioDTO getCurrent(String userEmail);
    List<PortfolioDTO> getFullHistory(String userEmail);
    List<PortfolioDTO> getHistoryByDates(String userEmail, LocalDate from, LocalDate to);





    Portfolio save(Portfolio p);
    List<Portfolio> getAllYesterdayPortfolios();
    List<User> getAllYesterdayPortfoliosUsers();
    List<Portfolio> findByUserAndDate(User u, LocalDate date);
    List<Portfolio> findByUser(User u);





}
