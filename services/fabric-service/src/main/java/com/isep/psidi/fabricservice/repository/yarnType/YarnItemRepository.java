package com.isep.psidi.fabricservice.repository.yarnType;

import com.isep.psidi.fabricservice.domain.yarn.YarnItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface YarnItemRepository extends MongoRepository<YarnItem, String> {

    public YarnItem findByPercentageAndYarnTypeCode(int percentage, String code);
}
