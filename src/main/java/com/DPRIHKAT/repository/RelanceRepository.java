package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Relance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RelanceRepository extends JpaRepository<Relance, UUID> {
}
