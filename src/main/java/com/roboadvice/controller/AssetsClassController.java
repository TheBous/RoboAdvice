package com.roboadvice.controller;

import com.roboadvice.dto.AssetsClassDTO;
import com.roboadvice.service.AssetsClassService;
import com.roboadvice.utils.Constant;
import com.roboadvice.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assetsclass")
public class AssetsClassController {

    private AssetsClassService assetsClassService;

    @Autowired
    public AssetsClassController(AssetsClassService assetsClassService) {
        this.assetsClassService = assetsClassService;
    }

    @RequestMapping("/trend")
    public GenericResponse<List<AssetsClassDTO>> getAssetsClassHistory(){
        List<AssetsClassDTO> assetsClassDTOList = assetsClassService.getAssetsClassHistory();

        if(assetsClassDTOList != null && !assetsClassDTOList.isEmpty())
            return new GenericResponse<>(assetsClassDTOList, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(assetsClassDTOList, Constant.ERROR_MSG, Constant.ERROR);
    }

}
