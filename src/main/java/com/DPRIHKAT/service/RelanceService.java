package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Relance;
import com.DPRIHKAT.repository.RelanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RelanceService {

    @Autowired
    private RelanceRepository relanceRepository;

    public List<Relance> findAll() {
        return relanceRepository.findAll();
    }

    public Relance findById(UUID id) {
        return relanceRepository.findById(id).orElse(null);
    }

    public Relance save(Relance relance) {
        return relanceRepository.save(relance);
    }

    public Relance update(UUID id, Relance relance) {
        if (relanceRepository.existsById(id)) {
            relance.setId(id);
            return relanceRepository.save(relance);
        }
        return null;
    }

    public void deleteById(UUID id) {
        relanceRepository.deleteById(id);
    }
}
