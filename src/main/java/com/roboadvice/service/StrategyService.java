package com.roboadvice.service;


import com.roboadvice.model.Strategy;
import com.roboadvice.model.User;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public interface StrategyService {
    Strategy insert(Strategy str);
    void setInactive (User u);
    List<Strategy> getCurrentStrategy(User u);
    List<Strategy> newStrategiesFromNewUsers();
    List<Strategy> newStrategiesFromOldUsers();
    List<Strategy> findHistoryStrategiesFromUser(User u);
    List<Strategy> findStrategiesByUserAndDate(User u, LocalDate date);
    List<Strategy> getLastStrategy(User u);
    List<Strategy> findHistoryByUserAndDates(User u, LocalDate start, LocalDate end);

}
