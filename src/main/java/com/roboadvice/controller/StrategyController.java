package com.roboadvice.controller;

import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.service.StrategyService;
import com.roboadvice.utils.Constant;
import com.roboadvice.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/strategy")
@CrossOrigin(origins = "*")
public class StrategyController {

    private StrategyService strategyService;

    @Autowired
    public StrategyController(StrategyService strategyService) {
        this.strategyService = strategyService;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST, consumes = "application/json")
    public GenericResponse<StrategyDTO> insertStrategy(@RequestBody @Valid StrategyDTO strategyDTO,
                                                       Authentication authentication) {
        //{"user_id": 56, "bonds_p": 30, "stocks_p": 30, "forex_p": 20, "commodities_p":20, "name": Balanced}
        String email = authentication.getName();
        StrategyDTO strDTO = strategyService.insert(email, strategyDTO);
        if(strDTO!=null)
            return new GenericResponse<>(strDTO, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }


    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public GenericResponse<Boolean> deletePendingStrategy(Authentication authentication){
        String email =authentication.getName();
        Boolean resp = strategyService.deletePendingStrategy(email);
        if(resp)
            return new GenericResponse<>(true, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(false, Constant.ERROR_MSG, Constant.ERROR);
    }

    @RequestMapping(value = "/getCurrent", method = RequestMethod.POST)
    public GenericResponse<StrategyDTO> getCurrentStrategy(Authentication authentication){
        String email = authentication.getName();
        StrategyDTO strategyDTO = strategyService.getCurrentActiveStrategy(email);
        if(strategyDTO!=null)
            return new GenericResponse<>(strategyDTO, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }


    @RequestMapping(value = "/getLast", method = RequestMethod.POST)
    public GenericResponse<StrategyDTO> getLastStrategy(Authentication authentication){
        String email = authentication.getName();
        StrategyDTO strategyDTO = strategyService.getLastUsedStrategy(email);
        if(strategyDTO != null)
            return new GenericResponse<>(strategyDTO, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }


    @RequestMapping(value = "/getFullHistory", method = RequestMethod.POST)
    public GenericResponse<List <StrategyDTO>> getFullHistory(Authentication authentication) {
        String email = authentication.getName();
        List<StrategyDTO> strategyDTOList = strategyService.getFullHistoryByUser(email);
        if(strategyDTOList!=null && !strategyDTOList.isEmpty())
            return new GenericResponse<>(strategyDTOList, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }

    @RequestMapping(value = "/advice", method = RequestMethod.POST)
    public GenericResponse<BigDecimal> getAdvice(@RequestParam(value = "strategy", defaultValue = "null") int strategyCode,
                                                 Authentication authentication){
        if(strategyCode<0 || strategyCode>4)
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);

        String userEmail = authentication.getName();
        PortfolioDTO pDTO = strategyService.getAdvice(userEmail, strategyCode);

        if(pDTO!=null)
            return new GenericResponse<>(pDTO.getTotalAmount(), Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);

    }

}

