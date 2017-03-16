package com.roboadvice.serviceImpl;


import com.roboadvice.model.ApiData;
import com.roboadvice.model.Assets;
import com.roboadvice.repository.ApiDataRepository;
import com.roboadvice.service.ApiDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiDataServiceImpl implements ApiDataService{

    private ApiDataRepository apiDataRepository;

    @Autowired
    public ApiDataServiceImpl(ApiDataRepository apiDataRepository) {
        this.apiDataRepository = apiDataRepository;
    }

    @Override
    public ApiData insert(ApiData ad) {
        if(apiDataRepository.findByAssetsIdAndDate(ad.getAssets().getId(), ad.getDate()) == null)
            return apiDataRepository.save(ad);
        else
            return null;
    }

    /*@Override
    public ApiData findLatestValueByAsset(Assets asset) {
        return apiDataRepository.findLatestValueByAsset(asset.getId());
    }*/
}
