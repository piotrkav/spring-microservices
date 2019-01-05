package com.isep.psidi.fabricservice.service;

import com.isep.psidi.fabricservice.domain.supplier.SupplierYarn;
import com.isep.psidi.fabricservice.repository.supplier.SupplierYarnRepository;
import com.isep.psidi.fabricservice.repository.yarnType.YarnTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class SupplierYarnService {

    @Autowired
    YarnTypeRepository yarnTypeRepository;
    @Autowired
    SupplierYarnRepository supplierYarnRepository;


    public List<SupplierYarn> getAllSupplierYarns() {
        return supplierYarnRepository.findAll();
    }

    public List<SupplierYarn> getSupplierYarnBySupplierId(String supplierId) {
        return supplierYarnRepository.findBySupplierId(supplierId);
    }

    public SupplierYarn getSupplierYarnById(String id) {
        return supplierYarnRepository.findOne(id);
    }

    public SupplierYarn getSupplierYarnBySupplierIdAndYarnTypeCode(String supplierId, String code) {
        return supplierYarnRepository.findBySupplierIdAndYarnTypeCode(supplierId, code);
    }

    public String createSupplierYarn(SupplierYarn supplierYarn) {
        supplierYarnRepository.save(supplierYarn);
        return supplierYarn.getId();
    }

    @StreamListener(InputChannel.YARN_NEEDED_INPUT)
    public void handleDeletedProduct(String message) {
        System.out.println("RABBITMQ_MESSAGE: " + message);
    }

    public void updateSupplierYarn(SupplierYarn supplierYarn) {
        supplierYarn = supplierYarnRepository.save(supplierYarn);
    }


    public void deleteSupplierYarn(String id) {
        SupplierYarn supplierYarn = supplierYarnRepository.findOne(id);
        if (supplierYarn == null) {
            throw new NotFoundException();
        } else {
            supplierYarnRepository.delete(supplierYarn);
        }
    }

    public boolean checkIfSupplierYarnExists(String supplierId, String code) {
        if (supplierYarnRepository.findBySupplierIdAndYarnTypeCode(supplierId, code) != null){
            return true;
        }
        else {
            return false;
        }
    }
}
