package com.isep.psidi.fabricservice.service;

import com.isep.psidi.fabricservice.domain.color.Color;
import com.isep.psidi.fabricservice.repository.color.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    public String createColor(Color color) {
        color = colorRepository.save(color);
        return color.getId();
    }

    public void updateColor(Color color) {
        color = colorRepository.save(color);
    }

    public Color getColorById(String id) {
        Color color = colorRepository.findOne(id);
        if (color == null) {
            throw new NotFoundException();
        }
        return color;
    }

    public Color getColorByName(String name) {
        Color color = colorRepository.findByName(name);
        if (color == null) {
            throw new NotFoundException();
        }
        return color;
    }

    public void deleteColor(String id) {
        Color color = colorRepository.findOne(id);
        if (color == null) {
            throw new NotFoundException();
        } else {
            colorRepository.delete(id);
        }
    }

    public boolean checkIfColorExists(String name) {
        Color color = colorRepository.findByName(name);
        return color != null;
    }
}
