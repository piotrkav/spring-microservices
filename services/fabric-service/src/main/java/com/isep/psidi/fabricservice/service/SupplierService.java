package com.isep.psidi.fabricservice.service;

import com.isep.psidi.fabricservice.domain.supplier.Supplier;
import com.isep.psidi.fabricservice.domain.supplier.SupplierYarn;
import com.isep.psidi.fabricservice.repository.supplier.SupplierRepository;
import com.isep.psidi.fabricservice.repository.supplier.SupplierYarnRepository;
import com.isep.psidi.fabricservice.repository.yarnType.YarnTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class SupplierService {

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    SupplierYarnRepository supplierYarnRepository;

    @Autowired
    YarnTypeRepository yarnTypeRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(String id) {
        return supplierRepository.findOne(id);
    }

    public String createSupplier(Supplier supplier) {
        supplierRepository.save(supplier);
        return supplier.getId();
    }

    public List<SupplierYarn> getSupplierYarns(String supplierId) {
        return supplierYarnRepository.findBySupplierId(supplierId);
    }

    public void updateSupplier(Supplier supplier) {
        supplier = supplierRepository.save(supplier);
    }

    public void deleteSupplier(String id) {
        Supplier supplier = supplierRepository.findOne(id);
        if (supplier == null) {
            throw new NotFoundException();
        } else {
            supplierRepository.delete(supplier);
        }
    }

    public boolean checkIfSupplierExists(String supplierId) {
        return supplierRepository.findOne(supplierId) != null;
    }

    public boolean checkIfSupplierExistsByName(String name) {
        return supplierRepository.findBySupplierName(name) != null;
    }
}
