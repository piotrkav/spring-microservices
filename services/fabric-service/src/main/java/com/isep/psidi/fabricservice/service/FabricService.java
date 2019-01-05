package com.isep.psidi.fabricservice.service;

import com.isep.psidi.fabricservice.domain.fabric.Fabric;
import com.isep.psidi.fabricservice.repository.fabric.FabricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class FabricService {

    @Autowired
    FabricRepository fabricRepository;

    public List<Fabric> getAllFabrics() {
        return fabricRepository.findAll();
    }

    public Fabric getFabricById(String id) {
        return fabricRepository.findOne(id);
    }

    public Fabric getFabricByFabricCode(String fabricCode) {
        return fabricRepository.findByFabricCode(fabricCode);
    }

    public Fabric createFabric(Fabric fabric) {
        return fabricRepository.save(fabric);
    }

    public void updateFabric(Fabric fabric) {
        fabric = fabricRepository.save(fabric);
    }


    public void deleteFabric(String id) {
        Fabric fabric = fabricRepository.findOne(id);
        if (fabric == null) {
            throw new NotFoundException();
        } else {
            fabricRepository.delete(fabric);
        }
    }

    public boolean checkIfYarnTypeExists(String code) {
        return fabricRepository.findByFabricCode(code) != null;
    }


}
