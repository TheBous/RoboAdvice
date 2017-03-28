package com.roboadvice.controller;

import com.roboadvice.service.*;
import com.roboadvice.utils.Sha256Util;
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

    /**
     * Method used to initialize the Api Data table in the database.
     *
     * @param pass password used for security.
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public void insertAPI(@RequestParam(value = "pass", defaultValue = "null") String pass) {
        long start = System.currentTimeMillis();
        if(Sha256Util.sha256(pass).equals("60fe3a47fb8e5441a87291a96df16f64fb67f8a8b2decd3c0ad6edda76afece8")) {
            apiDataService.firstInsert();
            long end = System.currentTimeMillis();
            System.out.println("QUANDL DATAS CORRECTLY INSERTED IN DATABASE - Total time: "+(end-start)/1000+"s");
        }
        else System.out.println("ACCESS DENIED!");
    }





}
