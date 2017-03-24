package com.roboadvice.service;

import com.roboadvice.dto.BacktestingDTO;
import com.roboadvice.dto.PortfolioDTO;

import java.time.LocalDate;
import java.util.List;

public interface PortfolioService {


    PortfolioDTO getCurrent(String userEmail);
    List<PortfolioDTO> getFullHistory(String userEmail);
    List<PortfolioDTO> getHistoryByDates(String userEmail, LocalDate from, LocalDate to);

    List<BacktestingDTO> getBackTestingChart(String userEmail, LocalDate fromDate);



}
