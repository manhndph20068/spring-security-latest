package com.backend.repository;

import com.backend.entity.ShoeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoeDetailRepository extends JpaRepository<ShoeDetail, Long> {
}
