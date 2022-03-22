package com.josiel.starwars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.josiel.starwars.model.Item;
import com.josiel.starwars.model.ItemSet;
import com.josiel.starwars.model.Rebel;
import com.josiel.starwars.service.RebelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportsController {

    @Autowired
    private final RebelService rebelService;

    @GetMapping("/betrayers")
    public ResponseEntity<String> getBetrayersPercent() {
        List<Rebel> rebels = rebelService.findAll();
        int rebelsAmount = rebels.size();
        int betrayersAmount = rebels.stream()
                .filter(rebel -> rebel.getBetrayerReportsCount() > 2)
                .collect(Collectors.toList())
                .size();

        if (rebelsAmount == 0) {
            return ResponseEntity.ok("There is no rebels registered yet!");
        }

        double betrayersPercent = betrayersAmount * 100 / rebelsAmount;

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode betrayersReport = mapper.createObjectNode();
        betrayersReport.put("betrayersPercent", betrayersPercent + "%");

        String json = "";
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(betrayersReport);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(json);
    }

    @GetMapping("/rebels")
    public ResponseEntity<String> getRebelsPercent() {
        List<Rebel> rebels = rebelService.findAll();
        int rebelsAmount = rebels.size();
        int notBetrayersAmount = rebels.stream()
                .filter(rebel -> rebel.getBetrayerReportsCount() < 3)
                .collect(Collectors.toList())
                .size();

        if (rebelsAmount == 0) {
            return ResponseEntity.ok("There is no rebels registered yet!");
        }

        double betrayersPercent = notBetrayersAmount * 100 / rebelsAmount;

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode betrayersReport = mapper.createObjectNode();
        betrayersReport.put("rebelsPercent", betrayersPercent + "%");

        String json = "";
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(betrayersReport);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(json);
    }

    @GetMapping("/resources-average")
    public ResponseEntity<String> getResourcesAverage() {
        List<Rebel> rebels = rebelService.findAll();
        List<Rebel> notBetrayers = rebels.stream()
                .filter(rebel -> rebel.getBetrayerReportsCount() < 3)
                .collect(Collectors.toList());

        if (notBetrayers.size() == 0) {
            return ResponseEntity.ok("There is no non-betrayer rebels registered!");
        }

        int arma = 0;
        int municao = 0;
        int agua = 0;
        int comida = 0;
        for (Rebel rebel : notBetrayers) {
            arma += rebel.getInventory().stream()
                    .filter(itemSet -> itemSet.getItem() == Item.ARMA)
                    .map(ItemSet::getAmount)
                    .reduce(0, Integer::sum);
            municao += rebel.getInventory().stream()
                    .filter(itemSet -> itemSet.getItem() == Item.MUNICAO)
                    .map(ItemSet::getAmount)
                    .reduce(0, Integer::sum);
            agua += rebel.getInventory().stream()
                    .filter(itemSet -> itemSet.getItem() == Item.AGUA)
                    .map(ItemSet::getAmount)
                    .reduce(0, Integer::sum);
            comida += rebel.getInventory().stream()
                    .filter(itemSet -> itemSet.getItem() == Item.COMIDA)
                    .map(ItemSet::getAmount)
                    .reduce(0, Integer::sum);
        }

        double armaAverage = arma / notBetrayers.size();
        double municaoAverage = municao / notBetrayers.size();
        double aguaAverage = agua / notBetrayers.size();
        double comidaAverage = comida / notBetrayers.size();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode betrayersReport = mapper.createObjectNode();
        betrayersReport.put("armaAverage", armaAverage);
        betrayersReport.put("municaoAverage", municaoAverage);
        betrayersReport.put("aguaAverage", aguaAverage);
        betrayersReport.put("comidaAverage", comidaAverage);

        String json = "";
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(betrayersReport);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(json);
    }

    @GetMapping("/lost-points")
    public ResponseEntity<String> getLostPoints() {
        List<Rebel> rebels = rebelService.findAll();
        List<Rebel> betrayers = rebels.stream()
                .filter(rebel -> rebel.getBetrayerReportsCount() > 2)
                .collect(Collectors.toList());

        int lostPoints = 0;
        for (Rebel rebel : betrayers) {
            lostPoints += rebel.getInventory().stream()
                    .map(itemSet -> itemSet.getItem().getPoints() * itemSet.getAmount())
                    .reduce(0, Integer::sum);
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode betrayersReport = mapper.createObjectNode();
        betrayersReport.put("lostPoints", lostPoints);

        String json = "";
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(betrayersReport);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(json);
    }
}
