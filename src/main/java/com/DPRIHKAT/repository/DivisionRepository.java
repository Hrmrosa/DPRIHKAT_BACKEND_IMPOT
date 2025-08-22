package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DivisionRepository extends JpaRepository<Division, UUID> {
}
