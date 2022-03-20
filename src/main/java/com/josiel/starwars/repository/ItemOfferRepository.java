package com.josiel.starwars.repository;

import com.josiel.starwars.model.ItemOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOfferRepository extends JpaRepository<ItemOffer, Integer> {
}