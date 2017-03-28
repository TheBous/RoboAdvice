package com.roboadvice.controller;

import com.roboadvice.dto.BacktestingDTO;
import com.roboadvice.dto.ForecastingDTO;
import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.service.DemoService;
import com.roboadvice.service.PortfolioService;
import com.roboadvice.utils.Constant;
import com.roboadvice.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/demo")
@CrossOrigin(origins = "*")
public class DemoController {

    private DemoService demoService;
    private PortfolioService portfolioService;

    @Autowired
    public DemoController(DemoService demoService, PortfolioService portfolioService) {
        this.demoService = demoService;
        this.portfolioService = portfolioService;
    }

    /**
     * Method that generates a portfolio starting from a date and a strategy specified as parameters.
     * This method is used as a demo of the platform and will be used by a new registered user.
     *
     * @param fromDate the portfolio will be generated from this date.
     * @param strategyDTO DTO object of a strategy.
     * @param authentication parameter from Spring Security used for user's authentication.
     * @return list of BacktestingDTO objects.
     */
    @RequestMapping(value = "/backtesting", method = RequestMethod.POST)
    public GenericResponse<List<BacktestingDTO>> getBacktestingDemo(@RequestParam(value = "fromDate", defaultValue = "null") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                    @RequestBody @Valid StrategyDTO strategyDTO,
                                                                    Authentication authentication){

        String userEmail = authentication.getName();
        List<BacktestingDTO> backtestingDTOList = demoService.getBacktestingDemo(userEmail, strategyDTO, fromDate);
        if(backtestingDTOList!=null)
            return new GenericResponse<>(backtestingDTOList, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }

    /**
     * Method used in the demo that generate the forecast of the user's portfolio for the next two days.
     *
     * @param authentication parameter from Spring Security used for user's authentication.
     * @return list of ForecastingDTO objects.
     */
    @RequestMapping(value = "/forecast", method = RequestMethod.POST)
    public GenericResponse<List<ForecastingDTO>> getForecastingDemo(Authentication authentication){
        String userEmail = authentication.getName();
        List<ForecastingDTO> forecastingDTOList = portfolioService.getForecast(userEmail, LocalDate.now().plusDays(2));
        if(forecastingDTOList!=null)
            return new GenericResponse<>(forecastingDTOList, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }
}
