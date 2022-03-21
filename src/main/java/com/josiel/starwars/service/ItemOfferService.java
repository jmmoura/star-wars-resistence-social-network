package com.josiel.starwars.service;

import com.josiel.starwars.exception.ItemOfferAmountOutOfBoundsException;
import com.josiel.starwars.exception.ItemOfferNotFoundException;
import com.josiel.starwars.exception.RebelNotFoundException;
import com.josiel.starwars.model.Item;
import com.josiel.starwars.model.ItemOffer;
import com.josiel.starwars.model.Rebel;
import com.josiel.starwars.repository.ItemOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemOfferService {

    @Autowired
    private ItemOfferRepository itemOfferRepository;
    @Autowired
    private RebelService rebelService;

    public List<ItemOffer> findAll() {
        return itemOfferRepository.findAll();
    }

    public ItemOffer findByProposerIdAndReceiverId(Integer proposerId, Integer receiverId) {
        Optional<ItemOffer> optionalItemOffer = itemOfferRepository.findByProposerIdAndReceiverId(proposerId, receiverId);
        if (optionalItemOffer.isPresent()) {
            return optionalItemOffer.get();
        }
        throw new ItemOfferNotFoundException();
    }

    public ItemOffer save(ItemOffer itemOffer) {
        Rebel proposer = rebelService.findById(itemOffer.getProposerId());
        Rebel receiver = rebelService.findById(itemOffer.getReceiverId());
        for (Item item : proposer.getInventory()) {
            if (item.getDescription().equals(itemOffer.getItemDescription())) {
                if (item.getAmount() < itemOffer.getAmount() || item.getAmount() <= 0) {
                    throw new ItemOfferAmountOutOfBoundsException();
                }
            }
        }
        return itemOfferRepository.save(itemOffer);
    }


}
