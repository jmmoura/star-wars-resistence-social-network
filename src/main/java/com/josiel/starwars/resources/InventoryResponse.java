package com.josiel.starwars.resources;

import com.josiel.starwars.model.ItemSet;
import com.josiel.starwars.model.Rebel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Setter
@Getter
public class InventoryResponse {
    private String name;
    private List<ItemSet> inventory;

    public static InventoryResponse fromDomain(final Rebel rebel) {
        InventoryResponse result = new InventoryResponse();
        BeanUtils.copyProperties(rebel, result);
        return result;
    }

}
