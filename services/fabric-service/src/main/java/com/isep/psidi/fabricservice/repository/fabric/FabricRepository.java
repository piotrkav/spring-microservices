package com.isep.psidi.fabricservice.repository.fabric;

import com.isep.psidi.fabricservice.domain.fabric.Fabric;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FabricRepository extends MongoRepository<Fabric, String> {

    public Fabric findByFabricCode(String fabricCode);
}
