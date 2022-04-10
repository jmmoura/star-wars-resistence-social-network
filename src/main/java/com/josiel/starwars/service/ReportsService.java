package com.josiel.starwars.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.josiel.starwars.model.Item;
import com.josiel.starwars.model.ItemSet;
import com.josiel.starwars.model.Rebel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportsService {

    @Autowired
    private final RebelService rebelService;


    public String getBetrayersPercent() {
        List<Rebel> rebels = rebelService.findAll();
        int rebelsAmount = rebels.size();
        int betrayersAmount = rebels.stream()
                .filter(rebel -> rebel.getBetrayerReportsCount() > 2)
                .collect(Collectors.toList())
                .size();

        if (rebelsAmount == 0) {
            return null;
        }

        double betrayersPercent = betrayersAmount * 100 / rebelsAmount;

        HashMap<String, String> itemsAverage = new HashMap<>();
        itemsAverage.put("betrayersPercent", String.format("%.1f%%", betrayersPercent));

        return generateJSON(itemsAverage);
    }

    public String getRebelsPercent() {
        List<Rebel> rebels = rebelService.findAll();
        int rebelsAmount = rebels.size();
        int nonBetrayersAmount = rebels.stream()
                .filter(rebel -> rebel.getBetrayerReportsCount() < 3)
                .collect(Collectors.toList())
                .size();

        if (rebelsAmount == 0) {
            return null;
        }

        double nonBetrayersPercent = nonBetrayersAmount * 100 / rebelsAmount;

        HashMap<String, String> itemsAverage = new HashMap<>();
        itemsAverage.put("rebelsPercent", String.format("%.1f%%", nonBetrayersPercent));

        return generateJSON(itemsAverage);
    }

    public String getResourcesAverage() {
        List<Rebel> rebels = rebelService.findAll();
        List<Rebel> notBetrayers = rebels.stream()
                .filter(rebel -> rebel.getBetrayerReportsCount() < 3)
                .collect(Collectors.toList());

        if (notBetrayers.size() == 0) {
            return null;
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

        return generateJSON(itemsAverage);
    }

    public String getLostPoints() {
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

        HashMap<String, String> itemsAverage = new HashMap<>();
        itemsAverage.put("lostPoints", String.format("%d", lostPoints));

        return generateJSON(itemsAverage);
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