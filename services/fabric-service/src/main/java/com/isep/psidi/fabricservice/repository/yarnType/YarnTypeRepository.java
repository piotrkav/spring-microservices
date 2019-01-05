package com.isep.psidi.fabricservice.repository.yarnType;

import com.isep.psidi.fabricservice.domain.yarn.YarnType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface YarnTypeRepository extends MongoRepository<YarnType, String> {
    YarnType findByCode(String code);
}
