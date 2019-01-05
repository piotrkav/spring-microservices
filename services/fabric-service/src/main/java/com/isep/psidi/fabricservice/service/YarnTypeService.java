package com.isep.psidi.fabricservice.service;

import com.isep.psidi.fabricservice.domain.yarn.YarnType;
import com.isep.psidi.fabricservice.repository.yarnType.YarnTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class YarnTypeService {

    @Autowired
    YarnTypeRepository yarnTypeRepository;

    public List<YarnType> getAllYarnTypes() {
        return yarnTypeRepository.findAll();
    }

    public YarnType getYarnTypeById(String id) {
        return yarnTypeRepository.findOne(id);
    }

    public YarnType getYarnTypeByCode(String code) {
        return yarnTypeRepository.findByCode(code);
    }

    public String createYarnType(YarnType yarnType) {
        yarnTypeRepository.save(yarnType);
        return yarnType.getId();
    }

    public void updateYarnType(YarnType yarnType) {
        yarnType = yarnTypeRepository.save(yarnType);
    }


    public void deleteYarnType(String id) {
        YarnType yarnType = yarnTypeRepository.findOne(id);
        if (yarnType == null) {
            throw new NotFoundException();
        } else {
            yarnTypeRepository.delete(yarnType);
        }
    }

    public boolean checkIfYarnTypeExists(String code) {
        return yarnTypeRepository.findByCode(code) != null;
    }
}
