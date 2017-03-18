package com.roboadvice.service;

import com.roboadvice.dto.AssetsClassDTO;
import com.roboadvice.model.Assets;
import com.roboadvice.model.AssetsClass;

import java.util.List;


public interface AssetsClassService {

    AssetsClass insert(AssetsClass ac);
    List<AssetsClassDTO> getAssetsClassHistory();

}
