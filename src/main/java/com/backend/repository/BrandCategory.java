package com.backend.repository;

import com.backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandCategory extends JpaRepository<Brand, Long> {
}
