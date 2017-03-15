package com.roboadvice.serviceImpl;

import com.roboadvice.model.AssetsClass;
import com.roboadvice.repository.AssetsClassRepository;
import com.roboadvice.service.AssetsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AssetsClassServiceImpl implements AssetsClassService {

    private AssetsClassRepository assetsClassRepository;

    @Autowired
    public AssetsClassServiceImpl(AssetsClassRepository assetsClassRepository) {
        this.assetsClassRepository = assetsClassRepository;
    }

    @Override
    public AssetsClass insert(AssetsClass ac) {

        AssetsClass assetsClass = assetsClassRepository.findById(ac.getId());

        if(assetsClass == null)
            return assetsClassRepository.save(ac);
        else
            return assetsClass;

    }

    @Override
    public AssetsClass selectById(long id){
        return assetsClassRepository.findById(id);
    }

    @Override
    public Iterable<AssetsClass> findAll() {
        return assetsClassRepository.findAll();
    }
}
