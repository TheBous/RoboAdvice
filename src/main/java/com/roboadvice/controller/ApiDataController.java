package com.roboadvice.controller;


import com.jimmoores.quandl.DataSetRequest;
import com.jimmoores.quandl.QuandlSession;
import com.jimmoores.quandl.TabularResult;
import com.roboadvice.model.*;
import com.roboadvice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.threeten.bp.Period;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/quandl")
@CrossOrigin(origins = "*")
public class ApiDataController {

    private ApiDataService apiDataService;
    private AssetsService assetsService;

    @Autowired
    public ApiDataController(ApiDataService apiDataService, AssetsService assetsService) {
        this.apiDataService = apiDataService;
        this.assetsService = assetsService;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public void insertAPI(@RequestParam(value = "pass", defaultValue = "null") String pass) {
        if(pass.equals("prattico")) {
            int j = 0;

            Iterable<Assets> assetsList = assetsService.findAll();
            ApiData api;

            for (Assets asset : assetsList) {
                QuandlSession session = QuandlSession.create("-Kcq55sDChWyAc2wcxcM");
                TabularResult tabularResult = session.getDataSet(
                        DataSetRequest.Builder
                                .of(asset.getApi_data_src())
                                .withColumn(asset.getColumn_num())
                                .withStartDate(org.threeten.bp.LocalDate.of(2012,01,01))
                                .withEndDate(org.threeten.bp.LocalDate.now().minus(Period.ofDays(1)))
                                .build());
                j = 0;
                while (j < tabularResult.size()) {

                    api = new ApiData(0, LocalDate.parse(tabularResult.get(j).getString(0)), BigDecimal.valueOf(tabularResult.get(j).getDouble(1)), asset);

                    apiDataService.insert(api);

                    j++;
                }
            }
            System.out.println("QUANDL DATAS CORRECTLY INSERTED IN DATABASE");
        }
        else System.out.println("ACCESS DENIED!");
    }





}
