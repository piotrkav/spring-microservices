package com.isep.psidi.fabricservice.repository.supplier;

import com.isep.psidi.fabricservice.domain.supplier.SupplierYarn;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SupplierYarnRepository extends MongoRepository<SupplierYarn, String> {

    public List<SupplierYarn> findBySupplierId(String supplierId);

    public SupplierYarn findBySupplierIdAndYarnTypeCode(String supplierId, String code);

}
