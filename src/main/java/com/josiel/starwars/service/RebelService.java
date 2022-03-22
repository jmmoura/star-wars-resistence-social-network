package com.josiel.starwars.service;

import com.josiel.starwars.exception.RebelNotFoundException;
import com.josiel.starwars.model.ItemSet;
import com.josiel.starwars.model.Item;
import com.josiel.starwars.model.Rebel;
import com.josiel.starwars.repository.RebelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RebelService {

    @Autowired
    private RebelRepository rebelRepository;

    public List<Rebel> findAll() {
        return rebelRepository.findAll();
    }

    public Rebel findById(Integer id) {
        Optional<Rebel> optionalRebel = rebelRepository.findById(id);
        if (optionalRebel.isPresent()) {
            return optionalRebel.get();
        }
        throw new RebelNotFoundException();
    }

    public Rebel save(Rebel rebel) {
        return rebelRepository.save(rebel);
    }

    public Rebel update(Rebel rebel, Integer id) {
        return rebelRepository.findById(id)
                .map(rebelOld -> merge(rebelOld, rebel))
                .map(rebelRepository::save)
                .orElseThrow(RebelNotFoundException::new);
    }

    public Rebel reportBetrayer(Integer id) {
        Optional<Rebel> optionalRebel = rebelRepository.findById(id);
        if (!optionalRebel.isPresent()) {
            throw new RebelNotFoundException();
        }
        Rebel rebel = optionalRebel.get();
        rebel.setBetrayerReportsCount(rebel.getBetrayerReportsCount()+1);
        return rebelRepository.save(rebel);
    }

    public Rebel updateInventory(Integer id, Item item, int amount) {
        Optional<Rebel> optionalRebel = rebelRepository.findById(id);
        if (!optionalRebel.isPresent()) {
            throw new RebelNotFoundException();
        }
        Rebel rebel = optionalRebel.get();
        for (ItemSet itemSet : rebel.getInventory()) {
            if (itemSet.getItem() == item) {
                itemSet.setAmount(amount);
            }
        }
        return rebelRepository.save(rebel);
    }

    public void delete(Integer id) {
        if (!rebelRepository.existsById(id)) {
            throw new RebelNotFoundException();
        }
        rebelRepository.deleteById(id);
    }

    private Rebel merge(Rebel rebelOld, Rebel rebelNew) {
        rebelNew.setId(rebelOld.getId());
        rebelNew.setName(rebelOld.getName());
        if (rebelNew.getAge() <= 0) {
            rebelNew.setAge(rebelOld.getAge());
        }
        rebelNew.setGenre(rebelOld.getGenre());
        if (rebelNew.getPosition() == null) {
            rebelNew.setPosition(rebelOld.getPosition());
        } else {
            rebelNew.getPosition().setId(rebelOld.getPosition().getId());
        }
        rebelNew.setInventory(rebelOld.getInventory());
        if (rebelNew.getBetrayerReportsCount() <= 0) {
            rebelNew.setBetrayerReportsCount(rebelOld.getBetrayerReportsCount());
        }
        return rebelNew;
    }
}
