package com.albertoalegria.mendel.repositories;

import com.albertoalegria.mendel.models.Building;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alberto Alegria
 */
public interface BuildingRepository extends JpaRepository<Building, Long> {
}
