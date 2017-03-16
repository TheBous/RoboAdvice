package com.roboadvice.service;


import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.model.Strategy;

import java.util.List;

public interface StrategyService {

    StrategyDTO insert(String userEmail, StrategyDTO strategyDTO);
    Boolean deletePendingStrategy(String userEmail);
    StrategyDTO getCurrentActiveStrategy(String userEmail);
    StrategyDTO getLastUsedStrategy(String userEmail);
    List<StrategyDTO> getFullHistoryByUser(String userEmail);




    List<Strategy> newStrategiesFromNewUsers();
    List<Strategy> newStrategiesFromOldUsers();


}
