package com.josiel.starwars.controller;

import com.josiel.starwars.model.ItemOffer;
import com.josiel.starwars.model.Rebel;
import com.josiel.starwars.service.ItemOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/offer-item")
public class ItemOfferController {

    @Autowired
    private final ItemOfferService itemOfferService;

    @GetMapping
    public ResponseEntity<List<ItemOffer>> findAll() {
        return ResponseEntity.ok(itemOfferService.findAll());
    }

    @GetMapping("/{proposerId}/{receiverId}")
    public ResponseEntity<ItemOffer> findById(@PathVariable Integer proposerId, @PathVariable Integer receiverId) {
        return ResponseEntity.ok(itemOfferService.findByProposerIdAndReceiverId(proposerId, receiverId));
    }

    @PostMapping
    public ResponseEntity<ItemOffer> save(@Valid @RequestBody ItemOffer itemOffer) {
        return ResponseEntity.ok(itemOfferService.save(itemOffer));
    }
}
