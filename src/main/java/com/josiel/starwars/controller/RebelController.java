package com.josiel.starwars.controller;

import com.josiel.starwars.model.ItemSet;
import com.josiel.starwars.model.Rebel;
import com.josiel.starwars.resources.InventoryResponse;
import com.josiel.starwars.service.RebelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/rebels")
public class RebelController {

    @Autowired
    private final RebelService rebelService;

    @GetMapping
    public ResponseEntity<List<Rebel>> findAll() {
        return ResponseEntity.ok(rebelService.findAll());
    }

    @GetMapping("/by-page")
    public ResponseEntity<List<Rebel>> findAllByPage(@PageableDefault(size = 5)
                                                         @SortDefault.SortDefaults({
                                                                 @SortDefault(sort = "name", direction = Sort.Direction.ASC)})
                                                                 Pageable pageable) {
        Page<Rebel> rebels = rebelService.findAllByPage(pageable);

        return ResponseEntity.ok(rebels.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rebel> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(rebelService.findById(id));
    }

    @GetMapping("/{id}/inventory")
    public ResponseEntity<InventoryResponse> findInventoryByRebelId(@PathVariable Integer id) {
        Rebel rebel = rebelService.findById(id);
        return ResponseEntity.ok(InventoryResponse.fromDomain(rebel));
    }

    @PostMapping
    public ResponseEntity<Rebel> save(@Valid @RequestBody Rebel rebel) {
        return ResponseEntity.ok(rebelService.save(rebel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rebel> update(@Valid @RequestBody Rebel rebel, @PathVariable Integer id) {
        return ResponseEntity.ok(rebelService.update(rebel, id));
    }

    @PutMapping("/report-position/{id}")
    public ResponseEntity<Rebel> reportPosition(@Valid @RequestBody Rebel rebel, @PathVariable Integer id) {
        return ResponseEntity.ok(rebelService.update(rebel, id));
    }

    @PutMapping("/report-betrayer/{id}")
    public ResponseEntity<Rebel> reportBetrayer(@PathVariable Integer id) {
        return ResponseEntity.ok(rebelService.reportBetrayer(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        rebelService.delete(id);
        return ResponseEntity.ok().build();
    }
}
