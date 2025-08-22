package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Poursuite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PoursuiteRepository extends JpaRepository<Poursuite, UUID> {
}
