package com.josiel.starwars.repository;

import com.josiel.starwars.model.Rebel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RebelRepository extends JpaRepository<Rebel, Integer> {
}