package com.roboadvice.controller;

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

    /*@RequestMapping(value = "/backtesting", method = RequestMethod.POST)
    public GenericResponse<List<PortfolioDTO>> getBacktestingChart(@RequestParam(value="precision", defaultValue = "null") String precision,
                                                                   @RequestParam(value="months", defaultValue = "0") int months,
                                                                   Authentication authentication){

        if(precision.equals("null") || (!precision.equals("weekly") && !precision.equals("monthly")))
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
        if(months<=0)
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);

        String userEmail = authentication.getName();
        List<PortfolioDTO> portfolioDTOList = portfolioService.getBackTestingChart(userEmail, precision, months);

        if(portfolioDTOList!=null)
            return new GenericResponse<>(portfolioDTOList, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }*/

    /*@RequestMapping(value = "/advice", method = RequestMethod.POST)
    public GenericResponse<BigDecimal> getAdvice(@RequestParam(value = "strategy", defaultValue = "null") int strategyCode,
                                                 Authentication authentication){
        if(strategyCode<1 || strategyCode>4)
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);

        String userEmail = authentication.getName();
        PortfolioDTO pDTO = portfolioService.getAdvice(userEmail, strategyCode);
    }*/

}
