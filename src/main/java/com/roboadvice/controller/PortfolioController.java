package com.roboadvice.controller;

import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.model.Portfolio;
import com.roboadvice.model.User;
import com.roboadvice.service.PortfolioService;
import com.roboadvice.service.UserService;
import com.roboadvice.utils.Constant;
import com.roboadvice.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    private PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService, UserService userService) {
        this.portfolioService = portfolioService;
    }

    @RequestMapping(value = "/getCurrent", method = RequestMethod.POST)
    public GenericResponse<PortfolioDTO> getCurrent(Authentication authentication){

        String email = authentication.getName();
        PortfolioDTO portfolioDTO = portfolioService.getCurrent(email);

        if(portfolioDTO != null)
            return new GenericResponse<>(portfolioDTO, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }


    @RequestMapping(value = "/getFullHistory", method = RequestMethod.POST)
    public GenericResponse<List<PortfolioDTO>> getFullHistory(Authentication authentication){
        String email = authentication.getName();

        List<PortfolioDTO> portfolioDTOList = portfolioService.getFullHistory(email);

        if(portfolioDTOList!=null)
            return new GenericResponse<>(portfolioDTOList, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);

    }


    @RequestMapping(value = "/getHistoryByDate", method = RequestMethod.POST)
    public GenericResponse<List<PortfolioDTO>> getHistoryByDate(@RequestParam(value = "fromDate", defaultValue = "null") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                @RequestParam(value = "toDate", defaultValue = "null") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                                Authentication authentication) {
        if(fromDate.isAfter(toDate))
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);

        String email = authentication.getName();
        List<PortfolioDTO> portfolioDTOList = portfolioService.getHistoryByDates(email, fromDate, toDate);

        if(portfolioDTOList!=null)
            return new GenericResponse<>(portfolioDTOList, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);

    }

}
