package com.josiel.starwars.service;

import com.josiel.starwars.exception.RebelNotFoundException;
import com.josiel.starwars.model.ItemSet;
import com.josiel.starwars.model.Item;
import com.josiel.starwars.model.Rebel;
import com.josiel.starwars.model.User;
import com.josiel.starwars.repository.RebelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RebelService {

    @Autowired
    private final RebelRepository rebelRepository;

    @Autowired
    private final SecurityService securityService;

    public List<Rebel> findAll() {
        return rebelRepository.findAll();
    }

    public Page<Rebel> findAllByPage(Pageable pageable) {
        return rebelRepository.findAll(pageable);
    }

    public Rebel findById(Integer id) {
        Optional<Rebel> optionalRebel = rebelRepository.findById(id);
        if (optionalRebel.isPresent()) {
            return optionalRebel.get();
        }
        throw new RebelNotFoundException();
    }

    public Rebel findByUser(User user) {
        Optional<Rebel> optionalRebel = rebelRepository.findByUser(user);
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
        if (rebelNew.getName() == null && rebelNew.getName().isBlank()) {
            rebelNew.setName(rebelOld.getName());
        }
        if (rebelNew.getUser() == null) {
            rebelNew.setUser(rebelOld.getUser());
        }
        if (rebelNew.getAge() <= 0) {
            rebelNew.setAge(rebelOld.getAge());
        }
        if (rebelNew.getGenre() == '\u0000') {
            rebelNew.setGenre(rebelOld.getGenre());
        }
        if (rebelNew.getPosition() == null) {
            rebelNew.setPosition(rebelOld.getPosition());
        } else {
            rebelNew.getPosition().setId(rebelOld.getPosition().getId());
            User user = securityService.getCurrentUser();
            rebelNew.getPosition().setUpdatedByAdmin(user.getRole().toUpperCase() == "ADMIN");
        }
        rebelNew.setInventory(rebelOld.getInventory());
        rebelNew.setBetrayerReportsCount(rebelOld.getBetrayerReportsCount());
        return rebelNew;
    }
}
