package com.josiel.starwars.controller;

import com.josiel.starwars.model.ItemOffer;
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

    @GetMapping("/{receiverId}")
    public ResponseEntity<ItemOffer> findByReceiverId(@PathVariable Integer receiverId) {
        return ResponseEntity.ok(itemOfferService.findByReceiverId(receiverId));
    }

    @PostMapping
    public ResponseEntity<ItemOffer> offerItem(@Valid @RequestBody ItemOffer itemOffer) {
        return ResponseEntity.ok(itemOfferService.offerItem(itemOffer));
    }
}
