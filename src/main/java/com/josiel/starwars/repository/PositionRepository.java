package com.josiel.starwars.repository;

import com.josiel.starwars.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Integer> {
}