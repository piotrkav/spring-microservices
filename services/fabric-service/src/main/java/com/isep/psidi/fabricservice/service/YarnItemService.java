package com.isep.psidi.fabricservice.service;

import com.isep.psidi.fabricservice.domain.yarn.YarnItem;
import com.isep.psidi.fabricservice.repository.yarnType.YarnItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class YarnItemService {

    @Autowired
    YarnItemRepository yarnItemRepository;

    public List<YarnItem> getAllYarnItems() {
        return yarnItemRepository.findAll();
    }

    public YarnItem getYarnItemById(String id) {
        return yarnItemRepository.findOne(id);
    }

    public YarnItem getYarnItemByPercentageAndCode(int percentage, String code) {
        return yarnItemRepository.findByPercentageAndYarnTypeCode(percentage, code);
    }

    public String createYarnItem(YarnItem yarnItem) {
        yarnItemRepository.save(yarnItem);
        return yarnItem.getId();
    }


    public void deleteYarnItem(String id) {
        YarnItem yarnItem = yarnItemRepository.findOne(id);
        if (yarnItem == null) {
            throw new NotFoundException();
        } else {
            yarnItemRepository.delete(yarnItem);
        }
    }

    public boolean checkIfYarnTypeExists(int percentage, String code) {
        return yarnItemRepository.findByPercentageAndYarnTypeCode(percentage, code) != null;
    }
}