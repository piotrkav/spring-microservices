package com.isep.psidi.fabricservice.repository.supplier;

import com.isep.psidi.fabricservice.domain.supplier.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierRepository extends MongoRepository<Supplier, String> {

    public Supplier findBySupplierName(String supplierName);
}
