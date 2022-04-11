package com.josiel.starwars.service;

import com.josiel.starwars.exception.*;
import com.josiel.starwars.model.ItemSet;
import com.josiel.starwars.model.ItemOffer;
import com.josiel.starwars.model.Rebel;
import com.josiel.starwars.model.User;
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
    private final ItemOfferRepository itemOfferRepository;

    @Autowired
    private final RebelService rebelService;

    @Autowired
    private final SecurityService securityService;

    public List<ItemOffer> findAll() {
        Rebel proposer = getProposer();
        return itemOfferRepository.findByProposerId(proposer.getId());
    }

    public ItemOffer findByProposerIdAndReceiverId(Integer proposerId, Integer receiverId) {
        Optional<ItemOffer> optionalItemOffer = itemOfferRepository.findByProposerIdAndReceiverId(proposerId, receiverId);
        if (optionalItemOffer.isPresent()) {
            return optionalItemOffer.get();
        }
        throw new ItemOfferNotFoundException();
    }

    public ItemOffer findByReceiverId(Integer receiverId) {
        Rebel proposer = getProposer();
        Optional<ItemOffer> optionalItemOffer = itemOfferRepository.findByProposerIdAndReceiverId(proposer.getId(), receiverId);
        if (optionalItemOffer.isPresent()) {
            return optionalItemOffer.get();
        }
        throw new ItemOfferNotFoundException();
    }

    public ItemOffer offerItem(ItemOffer itemOffer) {
        Rebel proposer = getProposer();
        Rebel receiver = rebelService.findById(itemOffer.getReceiverId());

        if (proposer.getBetrayerReportsCount() > 2 || receiver.getBetrayerReportsCount() > 2) {
            throw new BetrayerInventoryException();
        }

        for (ItemSet offerItemSet : itemOffer.getItemSetList()) {
            for (ItemSet proposerItemSet : proposer.getInventory()) {
                if (proposerItemSet.getItem().equals(offerItemSet.getItem())) {
                    if (proposerItemSet.getAmount() < offerItemSet.getAmount() || proposerItemSet.getAmount() <= 0) {
                        throw new ItemOfferAmountOutOfBoundsException();
                    }
                }
            }
        }

        ItemOffer firstItemOffer;
        try {
            firstItemOffer = findByProposerIdAndReceiverId(receiver.getId(), proposer.getId());
        } catch (ItemOfferNotFoundException e) {
            itemOffer.setProposerId(proposer.getId());
            return itemOfferRepository.save(itemOffer);
        }

        int firstOfferAmount = firstItemOffer.getItemSetList().stream()
                .map(itemSet -> itemSet.getAmount() * itemSet.getItem().getPoints())
                .reduce(0, Integer::sum);
        int currentOfferAmount = itemOffer.getItemSetList().stream()
                .map(itemSet -> itemSet.getAmount() * itemSet.getItem().getPoints())
                .reduce(0, Integer::sum);

        delete(firstItemOffer.getId());

        if (firstOfferAmount != currentOfferAmount) {
            throw new AmountItemsPointsDoesNotMatchException();
        }

        updateAmount(proposer, receiver, itemOffer);
        updateAmount(receiver, proposer, firstItemOffer);

        return itemOffer;
    }

    public void delete(Integer id) {
        if (!itemOfferRepository.existsById(id)) {
            throw new RebelNotFoundException();
        }
        itemOfferRepository.deleteById(id);
    }

    private Rebel getProposer() {
        User user = securityService.getCurrentUser();
        return rebelService.findByUser(user);
    }

    private void updateAmount(Rebel proposer, Rebel receiver, ItemOffer itemOffer) {
        for (ItemSet itemSet : itemOffer.getItemSetList()) {
            int proposerItemAmount = proposer.getInventory().stream()
                    .filter(item -> itemSet.getItem() == item.getItem())
                    .map(item -> item.getAmount())
                    .reduce(0, Integer::sum);
            int receiverItemAmount = receiver.getInventory().stream()
                    .filter(item -> itemSet.getItem() == item.getItem())
                    .map(item -> item.getAmount())
                    .reduce(0, Integer::sum);

            rebelService.updateInventory(itemOffer.getProposerId(), itemSet.getItem(), proposerItemAmount - itemSet.getAmount());
            rebelService.updateInventory(receiver.getId(), itemSet.getItem(), receiverItemAmount + itemSet.getAmount());

        }
    }

}
