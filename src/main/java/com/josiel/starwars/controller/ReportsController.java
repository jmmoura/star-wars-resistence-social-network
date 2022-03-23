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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        HashMap<String, String> itemsAverage = new HashMap<>();
        itemsAverage.put("betrayersPercent", String.format("%.1f%", betrayersPercent));

        String json = generateJSON(itemsAverage);
        return ResponseEntity.ok(json);
    }

    @GetMapping("/rebels")
    public ResponseEntity<String> getRebelsPercent() {
        List<Rebel> rebels = rebelService.findAll();
        int rebelsAmount = rebels.size();
        int nonBetrayersAmount = rebels.stream()
                .filter(rebel -> rebel.getBetrayerReportsCount() < 3)
                .collect(Collectors.toList())
                .size();

        if (rebelsAmount == 0) {
            return ResponseEntity.ok("There is no rebels registered yet!");
        }

        double nonBetrayersPercent = nonBetrayersAmount * 100 / rebelsAmount;

        HashMap<String, String> itemsAverage = new HashMap<>();
        itemsAverage.put("rebelsPercent", String.format("%.1f%", nonBetrayersPercent));

        String json = generateJSON(itemsAverage);
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

        HashMap<String, String> itemsAverage = new HashMap<>();
        itemsAverage.put("armaAverage", String.format("%.1f", armaAverage));
        itemsAverage.put("municaoAverage", String.format("%.1f", municaoAverage));
        itemsAverage.put("aguaAverage", String.format("%.1f", aguaAverage));
        itemsAverage.put("comidaAverage", String.format("%.1f", comidaAverage));
        
        String json = generateJSON(itemsAverage);
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
    
    private String generateJSON(HashMap<String, String> itemsAverage) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode betrayersReport = mapper.createObjectNode();

        for (Map.Entry<String, String> entry :
                itemsAverage.entrySet()) {
            betrayersReport.put(entry.getKey(), entry.getValue());
        }

        String json = "";
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(betrayersReport);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }
}
