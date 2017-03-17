package com.roboadvice.service;


import com.roboadvice.model.ApiData;
import com.roboadvice.model.Assets;

public interface ApiDataService {

    boolean firstInsert();
    ApiData insert(ApiData ad);
    //ApiData findLatestValueByAsset(Assets asset);




}
