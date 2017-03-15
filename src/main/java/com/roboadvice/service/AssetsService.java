package com.roboadvice.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.roboadvice.model.Assets;

import java.util.List;


public interface AssetsService {

    Assets insert(Assets asset);
    Iterable<Assets> findAll();


}
