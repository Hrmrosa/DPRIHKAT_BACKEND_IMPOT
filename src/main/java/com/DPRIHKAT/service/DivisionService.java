package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Division;
import com.DPRIHKAT.repository.DivisionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.UUID;

@Service
public class DivisionService {

    @Autowired
    private DivisionRepository divisionRepository;

    public List<Division> findAll() {
        return divisionRepository.findAll();
    }

    public Page<Division> findAll(Pageable pageable) {
        return divisionRepository.findAll(pageable);
    }

    public Division findById(UUID id) {
        return divisionRepository.findById(id).orElse(null);
    }

    public Division save(Division division) {
        // Check if a division with the same name already exists
        if (divisionRepository.existsByNom(division.getNom())) {
            throw new DataIntegrityViolationException("Une division avec le nom '" + division.getNom() + "' existe déjà");
        }
        return divisionRepository.save(division);
    }

    public Division update(UUID id, Division division) {
        if (!divisionRepository.existsById(id)) {
            return null;
        }
        
        // Check if another division with the same name exists (excluding this one)
        if (divisionRepository.existsByNomAndIdNot(division.getNom(), id)) {
            throw new DataIntegrityViolationException("Une division avec le nom '" + division.getNom() + "' existe déjà");
        }
        
        division.setId(id);
        return divisionRepository.save(division);
    }

    public void deleteById(UUID id) {
        divisionRepository.deleteById(id);
    }
}
