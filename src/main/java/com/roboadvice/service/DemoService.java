package com.roboadvice.service;


import com.roboadvice.dto.BacktestingDTO;
import com.roboadvice.dto.StrategyDTO;

import java.time.LocalDate;
import java.util.List;

public interface DemoService {

    List<BacktestingDTO> getBacktestingDemo(String userEmail, StrategyDTO strategyDTO, LocalDate fromDate);
}
