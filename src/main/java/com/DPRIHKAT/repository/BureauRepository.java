package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Bureau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BureauRepository extends JpaRepository<Bureau, UUID> {
}
