package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Poursuite;
import com.DPRIHKAT.repository.PoursuiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PoursuiteService {

    @Autowired
    private PoursuiteRepository poursuiteRepository;

    public List<Poursuite> findAll() {
        return poursuiteRepository.findAll();
    }

    public Poursuite findById(UUID id) {
        return poursuiteRepository.findById(id).orElse(null);
    }

    public Poursuite save(Poursuite poursuite) {
        return poursuiteRepository.save(poursuite);
    }

    public Poursuite update(UUID id, Poursuite poursuite) {
        if (poursuiteRepository.existsById(id)) {
            poursuite.setId(id);
            return poursuiteRepository.save(poursuite);
        }
        return null;
    }

    public void deleteById(UUID id) {
        poursuiteRepository.deleteById(id);
    }
}
