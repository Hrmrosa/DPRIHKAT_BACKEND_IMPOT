package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Contribuable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContribuableRepository extends JpaRepository<Contribuable, UUID> {
}
