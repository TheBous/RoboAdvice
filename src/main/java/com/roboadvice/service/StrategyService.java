package com.roboadvice.service;


import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.model.Strategy;
import com.roboadvice.model.User;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public interface StrategyService {

    StrategyDTO insert(String userEmail, StrategyDTO strategyDTO);
    Boolean deletePendingStrategy(String userEmail);
    StrategyDTO getCurrentActiveStrategy(String userEmail);
    StrategyDTO getLastUsedStrategy(String userEmail);
    List<StrategyDTO> getFullHistoryByUser(String userEmail);





    void setInactive (User u);
    List<Strategy> newStrategiesFromNewUsers();
    List<Strategy> newStrategiesFromOldUsers();


}
