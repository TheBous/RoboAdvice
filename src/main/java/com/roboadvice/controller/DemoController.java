package com.roboadvice.controller;

import com.roboadvice.dto.BacktestingDTO;
import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.service.DemoService;
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

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

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

}
