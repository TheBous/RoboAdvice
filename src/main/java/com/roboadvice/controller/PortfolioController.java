package com.roboadvice.controller;

import com.roboadvice.dto.BacktestingDTO;
import com.roboadvice.dto.ForecastingDTO;
import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.service.PortfolioService;
import com.roboadvice.utils.Constant;
import com.roboadvice.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    private PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
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

    @RequestMapping(value = "/backtesting", method = RequestMethod.POST)
    public GenericResponse<List<BacktestingDTO>> getBacktestingChart(@RequestParam(value = "fromDate", defaultValue = "null") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                     Authentication authentication){

        if(fromDate.isBefore(LocalDate.parse("2014-05-01")) || fromDate.isAfter(LocalDate.now()))
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);

        String userEmail = authentication.getName();
        List<BacktestingDTO> backtestingDTOList = portfolioService.getBackTestingChart(userEmail, fromDate);

        if(backtestingDTOList!=null)
            return new GenericResponse<>(backtestingDTOList, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }

    @RequestMapping(value = "/forecast", method = RequestMethod.POST)
    public GenericResponse<List<ForecastingDTO>> getForecast(@RequestParam(value = "targetDate", defaultValue = "1900-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
                                                             Authentication authentication){
        if(LocalDate.parse("1900-01-01").equals(targetDate))
            targetDate = LocalDate.now().plusMonths(1);

        if(targetDate.isBefore(LocalDate.now()) || targetDate.isEqual(LocalDate.now()))
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);

        String userEmail = authentication.getName();
        List<ForecastingDTO> forecastingDTOList = portfolioService.getForecast(userEmail, targetDate);

        if(forecastingDTOList!=null)
            return new GenericResponse<>(forecastingDTOList, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }
}
