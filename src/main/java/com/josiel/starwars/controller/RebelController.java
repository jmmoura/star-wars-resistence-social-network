package com.josiel.starwars.controller;

import com.josiel.starwars.model.ItemSet;
import com.josiel.starwars.model.Rebel;
import com.josiel.starwars.model.User;
import com.josiel.starwars.resources.InventoryResponse;
import com.josiel.starwars.service.RebelService;
import com.josiel.starwars.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private final SecurityService securityService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Rebel>> findAll() {
        return ResponseEntity.ok(rebelService.findAll());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/by-page")
    public ResponseEntity<List<Rebel>> findAllByPage(@PageableDefault(size = 5)
                                                         @SortDefault.SortDefaults({
                                                                 @SortDefault(sort = "name", direction = Sort.Direction.ASC)})
                                                                 Pageable pageable) {
        Page<Rebel> rebels = rebelService.findAllByPage(pageable);

        return ResponseEntity.ok(rebels.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Rebel> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(rebelService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/inventory")
    public ResponseEntity<InventoryResponse> findInventoryByRebelId(@PathVariable Integer id) {
        Rebel rebel = rebelService.findById(id);
        return ResponseEntity.ok(InventoryResponse.fromDomain(rebel));
    }

    @PreAuthorize("hasRole('ROLE_REBEL')")
    @GetMapping("/inventory")
    public ResponseEntity<InventoryResponse> findInventory() {
        Rebel rebel = getRebel();
        return ResponseEntity.ok(InventoryResponse.fromDomain(rebel));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Rebel> save(@Valid @RequestBody Rebel rebel) {
        return ResponseEntity.ok(rebelService.save(rebel));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Rebel> update(@Valid @RequestBody Rebel rebel, @PathVariable Integer id) {
        return ResponseEntity.ok(rebelService.update(rebel, id));
    }

    @PreAuthorize("hasRole('ROLE_REBEL')")
    @PutMapping("/report-position/")
    public ResponseEntity<Rebel> reportPosition(@Valid @RequestBody Rebel rebelNew) {
        Rebel rebel = getRebel();
        return ResponseEntity.ok(rebelService.update(rebelNew, rebel.getId()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/report-position/{id}")
    public ResponseEntity<Rebel> reportPosition(@Valid @RequestBody Rebel rebel, @PathVariable Integer id) {
        return ResponseEntity.ok(rebelService.update(rebel, id));
    }

    @PreAuthorize("hasRole('ROLE_REBEL')")
    @PutMapping("/report-betrayer/{id}")
    public ResponseEntity<Rebel> reportBetrayer(@PathVariable Integer id) {
        return ResponseEntity.ok(rebelService.reportBetrayer(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        rebelService.delete(id);
        return ResponseEntity.ok().build();
    }

    private Rebel getRebel() {
        User user = securityService.getCurrentUser();
        return rebelService.findByUser(user);
    }
}
