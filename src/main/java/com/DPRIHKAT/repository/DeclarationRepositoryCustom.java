package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.enums.StatutDeclaration;

public interface DeclarationRepositoryCustom {
    long countDeclarationsEnRetard();
    long countByStatut(StatutDeclaration statut);
}
