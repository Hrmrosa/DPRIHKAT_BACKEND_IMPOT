package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.repository.ContribuableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContribuableService {

    @Autowired
    private ContribuableRepository contribuableRepository;

    public List<Contribuable> findAll() {
        return contribuableRepository.findAll();
    }

    public Contribuable findById(UUID id) {
        return contribuableRepository.findById(id).orElse(null);
    }

    public Contribuable save(Contribuable contribuable) {
        return contribuableRepository.save(contribuable);
    }

    public Contribuable update(UUID id, Contribuable contribuable) {
        if (contribuableRepository.existsById(id)) {
            contribuable.setId(id);
            return contribuableRepository.save(contribuable);
        }
        return null;
    }

    public void deleteById(UUID id) {
        contribuableRepository.deleteById(id);
    }
}
