package com.roboadvice.serviceImpl;

import com.roboadvice.dto.AssetsClassDTO;
import com.roboadvice.model.AssetsClass;
import com.roboadvice.model.Portfolio;
import com.roboadvice.repository.AssetsClassRepository;
import com.roboadvice.service.AssetsClassService;
import com.roboadvice.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
    @Cacheable("assetsClassTrend")
    public List<AssetsClassDTO> getAssetsClassTrend() {

        List<Portfolio> sum_list= assetsClassRepository.getHistoryByDate(LocalDate.now().minusYears(1), LocalDate.now()); //1 year
        List<AssetsClassDTO> assetsClassDTOList = new ArrayList<>();
        AssetsClassDTO assetsClassDTO;

        int y;
        for(int i=0;i<sum_list.size();i+=y) {
            assetsClassDTO = new AssetsClassDTO();
            assetsClassDTO.setDate(sum_list.get(i).getDate());
            y = 0;
            if (assetsClassDTO.getDate().getDayOfMonth() == 1) {
                for (int j = i; j < i + Constant.NUM_ASSETS_CLASS && j < sum_list.size(); j++) {
                    if (sum_list.get(j).getDate().equals(sum_list.get(i).getDate())) {
                        if (sum_list.get(j).getAssetsClass().getId() == 1) {
                            assetsClassDTO.setBondsValue(sum_list.get(j).getValue());
                            y++;
                        }
                        if (sum_list.get(j).getAssetsClass().getId() == 2) {
                            assetsClassDTO.setStocksValue(sum_list.get(j).getValue());
                            y++;
                        }
                        if (sum_list.get(j).getAssetsClass().getId() == 3) {
                            assetsClassDTO.setForexValue(sum_list.get(j).getValue());
                            y++;
                        }
                        if (sum_list.get(j).getAssetsClass().getId() == 4) {
                            assetsClassDTO.setCommoditiesValue(sum_list.get(j).getValue());
                            y++;
                        }
                    } else
                        break;
                }
                assetsClassDTOList.add(assetsClassDTO);
            }
            else
                y++;
        }
        return  assetsClassDTOList;

    }

}
