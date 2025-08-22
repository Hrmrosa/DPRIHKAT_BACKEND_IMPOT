package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.DossierRecouvrement;
import com.DPRIHKAT.repository.DossierRecouvrementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DossierRecouvrementService {

    @Autowired
    private DossierRecouvrementRepository dossierRecouvrementRepository;

    public List<DossierRecouvrement> findAll() {
        return dossierRecouvrementRepository.findAll();
    }

    public DossierRecouvrement findById(UUID id) {
        return dossierRecouvrementRepository.findById(id).orElse(null);
    }

    public DossierRecouvrement save(DossierRecouvrement dossier) {
        return dossierRecouvrementRepository.save(dossier);
    }

    public DossierRecouvrement update(UUID id, DossierRecouvrement dossier) {
        if (dossierRecouvrementRepository.existsById(id)) {
            dossier.setId(id);
            return dossierRecouvrementRepository.save(dossier);
        }
        return null;
    }

    public void deleteById(UUID id) {
        dossierRecouvrementRepository.deleteById(id);
    }
}
