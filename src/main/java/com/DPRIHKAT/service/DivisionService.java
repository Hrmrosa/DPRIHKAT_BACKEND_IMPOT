package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Division;
import com.DPRIHKAT.repository.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DivisionService {

    @Autowired
    private DivisionRepository divisionRepository;

    public List<Division> findAll() {
        return divisionRepository.findAll();
    }

    public Division findById(UUID id) {
        return divisionRepository.findById(id).orElse(null);
    }

    public Division save(Division division) {
        return divisionRepository.save(division);
    }

    public Division update(UUID id, Division division) {
        if (divisionRepository.existsById(id)) {
            division.setId(id);
            return divisionRepository.save(division);
        }
        return null;
    }

    public void deleteById(UUID id) {
        divisionRepository.deleteById(id);
    }
}
