package com.josiel.starwars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.josiel.starwars.model.Item;
import com.josiel.starwars.model.ItemSet;
import com.josiel.starwars.model.Rebel;
import com.josiel.starwars.service.RebelService;
import com.josiel.starwars.service.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportsController {

    @Autowired
    private final ReportsService reportsService;

    @GetMapping("/betrayers")
    public ResponseEntity<String> getBetrayersPercent() {
        String betrayersPercent = reportsService.getBetrayersPercent();

        if (betrayersPercent == null) {
            return ResponseEntity.ok("There is no rebels registered yet!");
        }

        return ResponseEntity.ok(betrayersPercent);
    }

    @GetMapping("/rebels")
    public ResponseEntity<String> getRebelsPercent() {
        String rebelsPercent = reportsService.getRebelsPercent();

        if (rebelsPercent == null) {
            return ResponseEntity.ok("There is no rebels registered yet!");
        }

        return ResponseEntity.ok(rebelsPercent);
    }

    @GetMapping("/resources-average")
    public ResponseEntity<String> getResourcesAverage() {
        String resourcesAverage = reportsService.getResourcesAverage();

        if (resourcesAverage == null) {
            return ResponseEntity.ok("There is no non-betrayer rebels registered!");
        }

        return ResponseEntity.ok(resourcesAverage);
    }

    @GetMapping("/lost-points")
    public ResponseEntity<String> getLostPoints() {
        return ResponseEntity.ok(reportsService.getLostPoints());
    }
}
