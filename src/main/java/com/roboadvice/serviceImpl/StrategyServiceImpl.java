package com.roboadvice.serviceImpl;

import com.roboadvice.model.Strategy;
import com.roboadvice.model.User;
import com.roboadvice.repository.StrategyRepository;
import com.roboadvice.service.StrategyService;
import com.roboadvice.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


@Service
public class StrategyServiceImpl implements StrategyService{

    private StrategyRepository strategyRepository;

    @Autowired
    public StrategyServiceImpl(StrategyRepository strategyRepository) {
        this.strategyRepository = strategyRepository;
    }

    @Override
    public Strategy insert(Strategy str) {
        return strategyRepository.save(str);
    }

    @Override
    public void setInactive(User u) {
        strategyRepository.setInactive(u);
    }

    @Override
    public List<Strategy> getCurrentStrategy(User u) {
        return strategyRepository.findByUserAndActive(u, (byte) 1);
    }

    @Override
    public List<Strategy> newStrategiesFromNewUsers() {
        List<Strategy> newStrategies = strategyRepository.findNewStrategies(LocalDate.now().minus(Period.ofDays(1)));

        if (!newStrategies.isEmpty()){
            List<Strategy> oldUserStrategies = new ArrayList<>();
            List<Strategy> newStrategiesFromNewUsers = new ArrayList<>();

            for (Strategy strategy : newStrategies) {
                //se trovo strategia con active=0 && data precedente, significa che non è un nuovo utente ma è un utente che ha cambiato strategia
                oldUserStrategies = strategyRepository.findUserOldStrategies(strategy.getUser(), strategy.getDate());
                if (oldUserStrategies.isEmpty()) {
                    newStrategiesFromNewUsers.add(strategy);
                }
            }
            return newStrategiesFromNewUsers;
        }
        else {
            return null;
        }
    }

    @Override
    public List<Strategy> newStrategiesFromOldUsers() {
        List<Strategy> newStrategies = strategyRepository.findNewStrategies(LocalDate.now().minus(Period.ofDays(1)));

        if (!newStrategies.isEmpty()){
            List<Strategy> oldUserStrategies = new ArrayList<>();
            List<Strategy> newStrategiesFromOldUsers = new ArrayList<>();

            for (Strategy strategy : newStrategies) {
                oldUserStrategies = strategyRepository.findUserOldStrategies(strategy.getUser(), strategy.getDate());
                if (!oldUserStrategies.isEmpty()) {
                    newStrategiesFromOldUsers.add(strategy);
                }
            }
            return newStrategiesFromOldUsers;
        }
        else {
            return null;
        }
    }
    @Override
    public List<Strategy> findHistoryStrategiesFromUser(User u) {
        return strategyRepository.findStrategiesHistory(u);
    }
    @Override
    public List<Strategy> findStrategiesByUserAndDate(User u, LocalDate date) {
        return strategyRepository.findByUserAndDate(u,date);
    }

    @Override
    public List<Strategy> getLastStrategy(User u) {
        return strategyRepository.findLastStrategy(u, new PageRequest(0, Constant.NUM_ASSETS_CLASS));
    }

    @Override
    public List<Strategy> findHistoryByUserAndDates(User u, LocalDate start, LocalDate end) {
        return strategyRepository.findHistoryByUserAndDates(u, start, end);
    }
}
