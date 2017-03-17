package com.roboadvice.controller;

import com.roboadvice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/quandl")
@CrossOrigin(origins = "*")
public class ApiDataController {

    private ApiDataService apiDataService;

    @Autowired
    public ApiDataController(ApiDataService apiDataService) {
        this.apiDataService = apiDataService;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public void insertAPI(@RequestParam(value = "pass", defaultValue = "null") String pass) {
        if(pass.equals("prattico")) {
            apiDataService.firstInsert();
            System.out.println("QUANDL DATAS CORRECTLY INSERTED IN DATABASE");
        }
        else System.out.println("ACCESS DENIED!");
    }





}
