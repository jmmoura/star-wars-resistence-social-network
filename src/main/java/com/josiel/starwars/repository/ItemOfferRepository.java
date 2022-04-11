package com.josiel.starwars.repository;

import com.josiel.starwars.model.ItemOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemOfferRepository extends JpaRepository<ItemOffer, Integer> {
    Optional<ItemOffer> findByProposerIdAndReceiverId(Integer proposerId, Integer receiverId);

    List<ItemOffer> findByProposerId(Integer id);
}