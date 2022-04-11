package com.josiel.starwars.repository;

import com.josiel.starwars.model.Rebel;
import com.josiel.starwars.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RebelRepository extends JpaRepository<Rebel, Integer> {
    Optional<Rebel> findByUser(User User);
}