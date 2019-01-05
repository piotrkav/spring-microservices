package com.isep.psidi.fabricservice.repository.color;

import com.isep.psidi.fabricservice.domain.color.Color;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ColorRepository extends MongoRepository<Color, String> {

    public Color findByName(String name);

}
