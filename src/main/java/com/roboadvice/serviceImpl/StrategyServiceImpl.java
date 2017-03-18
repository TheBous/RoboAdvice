package com.roboadvice.serviceImpl;

import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.model.AssetsClass;
import com.roboadvice.model.Strategy;
import com.roboadvice.model.User;
import com.roboadvice.repository.AssetsClassRepository;
import com.roboadvice.repository.StrategyRepository;
import com.roboadvice.repository.UserRepository;
import com.roboadvice.service.StrategyService;
import com.roboadvice.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class StrategyServiceImpl implements StrategyService{

    private StrategyRepository strategyRepository;
    private UserRepository userRepository;
    private AssetsClassRepository assetsClassRepository;

    @Autowired
    public StrategyServiceImpl(StrategyRepository strategyRepository, UserRepository userRepository, AssetsClassRepository assetsClassRepository) {
        this.strategyRepository = strategyRepository;
        this.userRepository = userRepository;
        this.assetsClassRepository = assetsClassRepository;
    }


    @Override
    @CacheEvict(cacheNames = "strategyFullHistory", allEntries = true)
    public StrategyDTO insert(String userEmail, StrategyDTO strategyDTO) {
        User u = userRepository.findByEmail(userEmail);
        if (u == null) {
            return null;
        }
        strategyRepository.setInactive(u);
        AssetsClass ac = null;
        Strategy str = null;

        for (int i = 1; i < Constant.NUM_ASSETS_CLASS+1; i++) {
            ac = assetsClassRepository.findById(i);
            str = new Strategy(0, strategyDTO.getName(), LocalDate.now(), true, strategyDTO.getPercentage(i), u, ac);
            strategyRepository.save(str);
        }
        StrategyDTO strDTO = strategyDTO;
        strDTO.setDate(LocalDate.now());
        strDTO.setActive(true);
        return strDTO;
    }

    @Override
    @CacheEvict(cacheNames = "strategyFullHistory", allEntries = true)
    public Boolean deletePendingStrategy(String userEmail) {
        User u = userRepository.findByEmail(userEmail);
        if (u==null)
            return false;

        int resp = strategyRepository.deleteActiveStrategyByUserAndDate(u, LocalDate.now());
        if(resp==Constant.NUM_ASSETS_CLASS){
            List<Strategy> strategies = strategyRepository.findLatestInactiveStrategy(u, new PageRequest(0, Constant.NUM_ASSETS_CLASS));
            for(Strategy s : strategies){
                s.setActive(true);
                strategyRepository.save(s);
            }
            return true;
        }
        else
            return false;
    }

    @Override
    public StrategyDTO getCurrentActiveStrategy(String userEmail) {
        StrategyDTO strDTO = new StrategyDTO();
        User u = userRepository.findByEmail(userEmail);
        if(u == null)
            return null;

        List<Strategy> strategies = strategyRepository.findByUserAndActive(u, true);
        if (!strategies.isEmpty()) {
            strDTO.setName(strategies.get(0).getName());
            strDTO.setDate(strategies.get(0).getDate());
            strDTO.setActive(strategies.get(0).getActive());
            for (Strategy strategy : strategies) {
                strDTO.setPercentage(strategy.getAssetsClass().getId(), strategy.getPercentage());
            }
            return strDTO;
        } else
            return null;
    }

    @Override
    public StrategyDTO getLastUsedStrategy(String userEmail) {

        StrategyDTO strDTO = new StrategyDTO();

        User u = userRepository.findByEmail(userEmail);
        if(u == null)
            return null;

        List<Strategy> strategies = strategyRepository.findLastUsedStrategy(u, new PageRequest(0, Constant.NUM_ASSETS_CLASS));
        if(strategies!=null && !strategies.isEmpty()){
            strDTO.setName(strategies.get(0).getName());
            strDTO.setDate(strategies.get(0).getDate());
            strDTO.setActive(strategies.get(0).getActive());
            for (Strategy strategy : strategies) {
                strDTO.setPercentage(strategy.getAssetsClass().getId(), strategy.getPercentage());
            }
            return strDTO;
        }
        else
            return null;
    }

    @Override
    @Cacheable("strategyFullHistory")
    public List<StrategyDTO> getFullHistoryByUser(String userEmail) {
        User u = userRepository.findByEmail(userEmail);
        if (u == null)
            return null;

        List<Strategy> strategyList = strategyRepository.fullHistoryByUser(u);
        if(strategyList!= null && !strategyList.isEmpty()) {
            StrategyDTO strDTO = new StrategyDTO();
            List<StrategyDTO> strategyDTOList = new ArrayList<>();

            for(int i=0;i<strategyList.size();i+=Constant.NUM_ASSETS_CLASS){
                strDTO = new StrategyDTO();
                strDTO.setName(strategyList.get(i).getName());
                strDTO.setDate(strategyList.get(i).getDate());
                strDTO.setActive(strategyList.get(i).getActive());
                for(int j=i;j<i+Constant.NUM_ASSETS_CLASS;j++){
                    strDTO.setPercentage(strategyList.get(j).getAssetsClass().getId(), strategyList.get(j).getPercentage());
                }
                strategyDTOList.add(strDTO);
            }
            return strategyDTOList;
        }
        else
            return null;
    }
}
