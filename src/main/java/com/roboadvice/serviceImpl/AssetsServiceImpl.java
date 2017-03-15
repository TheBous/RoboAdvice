package com.roboadvice.serviceImpl;

import com.roboadvice.model.Assets;
import com.roboadvice.repository.AssetsRepository;
import com.roboadvice.service.AssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AssetsServiceImpl implements AssetsService{

    private AssetsRepository assetsRepository;

    @Autowired
    public AssetsServiceImpl(AssetsRepository assetsRepository) {
        this.assetsRepository = assetsRepository;
    }

    @Override
    public Assets insert(Assets asset) {

        Assets a = assetsRepository.findById(asset.getId());
        if(a == null){
            return assetsRepository.save(asset);
        }
        else
            return a;
    }

    @Override
    public Iterable<Assets> findAll() {
        return assetsRepository.findAll();
    }
}
