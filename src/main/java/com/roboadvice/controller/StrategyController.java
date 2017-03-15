package com.roboadvice.controller;

import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.model.AssetsClass;
import com.roboadvice.model.Strategy;
import com.roboadvice.model.User;
import com.roboadvice.repository.StrategyRepository;
import com.roboadvice.service.AssetsClassService;
import com.roboadvice.service.StrategyService;
import com.roboadvice.service.UserService;
import com.roboadvice.utils.Constant;
import com.roboadvice.utils.GenericResponse;
import org.apache.tomcat.jni.Local;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/strategy")
@CrossOrigin(origins = "*")
public class StrategyController {

    private UserService userService;
    private AssetsClassService assetsClassService;
    private StrategyService strategyService;
    private StrategyRepository strategyRepository;

    @Autowired
    public StrategyController(UserService userService, AssetsClassService assetsClassService, StrategyService strategyService,StrategyRepository strategyRepository ) {
        this.userService = userService;
        this.assetsClassService = assetsClassService;
        this.strategyService = strategyService;
        this.strategyRepository= strategyRepository;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST, consumes = "application/json")
    public GenericResponse<StrategyDTO> insertStrategy(@RequestBody @Validated StrategyDTO strategyDTO,
                                                       Authentication authentication) {
        //{"user_id": 56, "bonds_p": 30, "stocks_p": 30, "forex_p": 20, "commodities_p":20, "name": Balanced}
        String email = authentication.getName();
        User u = userService.selectByEmail(email);
        if (u == null) {
            return new GenericResponse<>(null, "USER_ID NOT FOUND", Constant.ERROR);
        }
        strategyService.setInactive(u);
        AssetsClass ac = null;
        Strategy str = null;

        for (int i = 1; i < Constant.NUM_ASSETS_CLASS+1; i++) {
            ac = assetsClassService.selectById(i);
            str = new Strategy(0, strategyDTO.getName(), LocalDate.now(), true, strategyDTO.getPercentage(i), u, ac);
            strategyService.insert(str);
        }
        StrategyDTO strDTO = strategyDTO;
        strDTO.setDate(LocalDate.now());
        strDTO.setActive(true);

        return new GenericResponse<>(strDTO, Constant.SUCCES_MSG, Constant.SUCCESS);
    }


    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public GenericResponse<Boolean> deletePendingStrategy(Authentication authentication){
        String email =authentication.getName();
        User u = userService.selectByEmail(email);
        if (u !=null){
            int risposta = strategyService.deleteActiveStrategy(u, LocalDate.now());
            if(risposta==4){
                List<Strategy> strategies = strategyService.findLatestStrategy(u);

                for(Strategy s : strategies){
                    s.setActive(true);
                    strategyService.insert(s);
                }

                return new GenericResponse<>(true, "STRATEGY DELETED", Constant.SUCCESS);

            }
            else
                return new GenericResponse<>(null, "IMPOSSIBLE TO DELETE STRATEGY", Constant.ERROR);

        }
        else
            return new GenericResponse<>(null, "USER_ID NOT FOUND", Constant.ERROR);

    }


    @RequestMapping(value = "/getCurrent", method = RequestMethod.POST)
    public GenericResponse<StrategyDTO> getCurrentStrategy(Authentication authentication){
        StrategyDTO strDTO = new StrategyDTO();
        String email = authentication.getName();
        User u = userService.selectByEmail(email);
        if(u != null) {
            List<Strategy> strategies = strategyService.getCurrentStrategy(u);
            if (!strategies.isEmpty()) {
                strDTO.setName(strategies.get(0).getName());
                strDTO.setDate(strategies.get(0).getDate());
                strDTO.setActive(strategies.get(0).getActive());
                for (Strategy strategy : strategies) {
                    strDTO.setPercentage(strategy.getAssetsClass().getId(), strategy.getPercentage());
                }
                return new GenericResponse<>(strDTO, Constant.SUCCES_MSG, Constant.SUCCESS);
            } else
                return new GenericResponse<>(null, "NO STRATEGIES FOUND FOR USER_EMAIL: " + u.getEmail(), Constant.ERROR);
        }
        else
            return new GenericResponse<>(null, "USER_ID NOT FOUND", Constant.ERROR);
    }


    @RequestMapping(value = "/getLast", method = RequestMethod.POST)
    public GenericResponse<StrategyDTO> getLastStrategy(Authentication authentication){
        String email = authentication.getName();
        StrategyDTO strDTO = new StrategyDTO();

        User u = userService.selectByEmail(email);
        if(u != null){
            List<Strategy> strategies = strategyService.getLastStrategy(u);
            if(strategies!=null && !strategies.isEmpty()){
                strDTO.setName(strategies.get(0).getName());
                strDTO.setDate(strategies.get(0).getDate());
                strDTO.setActive(strategies.get(0).getActive());
                for (Strategy strategy : strategies) {
                    strDTO.setPercentage(strategy.getAssetsClass().getId(), strategy.getPercentage());
                }
                return new GenericResponse<>(strDTO, Constant.SUCCES_MSG, Constant.SUCCESS);
            }
            else
                return new GenericResponse<>(null, "NO OLD STRATEGY FOUND FOR USER_EMAIL: " + u.getEmail(), Constant.ERROR);
        }
        else
            return new GenericResponse<>(null, "USER_ID NOT FOUND", Constant.ERROR);

    }


    @RequestMapping(value = "/getFullHistory", method = RequestMethod.POST)
    public GenericResponse<List <StrategyDTO>> findHistoryStrategiesFromUser(Authentication authentication) {
        String email = authentication.getName();
        User u = userService.selectByEmail(email);
        if (u != null) {
            List<Strategy> strategyList = strategyService.fullHistoryByUser(u);
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
                    return new GenericResponse<>(strategyDTOList, Constant.SUCCES_MSG, Constant.SUCCESS);
            }
            else
                return new GenericResponse<>(null, "THE USER HAS NO STRATEGIES YET!", Constant.ERROR);
        } else
            return new GenericResponse<>(null, "USER NOT FOUND", Constant.ERROR);
    }

}

