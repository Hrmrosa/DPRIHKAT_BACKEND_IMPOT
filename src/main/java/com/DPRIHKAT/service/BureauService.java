package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Bureau;
import com.DPRIHKAT.repository.BureauRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.UUID;

@Service
public class BureauService {

    @Autowired
    private BureauRepository bureauRepository;

    public List<Bureau> findAll() {
        return bureauRepository.findAll();
    }

    public Page<Bureau> findAll(Pageable pageable) {
        return bureauRepository.findAll(pageable);
    }

    public Bureau findById(UUID id) {
        return bureauRepository.findById(id).orElse(null);
    }

    public Bureau save(Bureau bureau) {
        // Check if a bureau with the same name already exists
        if (bureauRepository.existsByNom(bureau.getNom())) {
            throw new DataIntegrityViolationException("Un bureau avec le nom '" + bureau.getNom() + "' existe déjà");
        }
        return bureauRepository.save(bureau);
    }

    public Bureau update(UUID id, Bureau bureau) {
        if (!bureauRepository.existsById(id)) {
            return null;
        }
        
        // Check if another bureau with the same name exists (excluding this one)
        if (bureauRepository.existsByNomAndIdNot(bureau.getNom(), id)) {
            throw new DataIntegrityViolationException("Un bureau avec le nom '" + bureau.getNom() + "' existe déjà");
        }
        
        bureau.setId(id);
        return bureauRepository.save(bureau);
    }

    public void deleteById(UUID id) {
        bureauRepository.deleteById(id);
    }
}
