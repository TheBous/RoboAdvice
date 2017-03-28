package com.roboadvice.service;


import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.dto.StrategyDTO;

import java.util.List;

public interface StrategyService {

    StrategyDTO insert(String userEmail, StrategyDTO strategyDTO);
    Boolean deletePendingStrategy(String userEmail);
    StrategyDTO getCurrentActiveStrategy(String userEmail);
    StrategyDTO getLastUsedStrategy(String userEmail);
    List<StrategyDTO> getFullHistoryByUser(String userEmail);
    PortfolioDTO getAdvice(String userEmail, int strategyCode);

}
