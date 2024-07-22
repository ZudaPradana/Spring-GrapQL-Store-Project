package org.zydd.bebtpn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.zydd.bebtpn.dto.RequestItemCreateUpdate;
import org.zydd.bebtpn.dto.ResponGetAllData;
import org.zydd.bebtpn.dto.ResponGetData;
import org.zydd.bebtpn.dto.ResponHeader;
import org.zydd.bebtpn.service.ItemService;

@Controller
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @QueryMapping
    public ResponGetAllData<?> getAllItem(@Argument String itemName, @Argument Boolean isAvailable,@Argument int page,@Argument int size) {
        return itemService.getAllData(itemName, isAvailable, page, size);
    }

    @QueryMapping
    public ResponGetData<?> getItem(@Argument String itemId) {
        return itemService.getItem(itemId);
    }

    @MutationMapping
    public ResponHeader createItem(@Argument("createItem") RequestItemCreateUpdate requestItemCreateUpdate) {
        return itemService.createItem(requestItemCreateUpdate);
    }

    @MutationMapping
    public ResponHeader updateItem(@Argument String id,@Argument("updateItem") RequestItemCreateUpdate requestItemCreateUpdate) {
        return itemService.updateItem(id ,requestItemCreateUpdate);
    }

    @MutationMapping
    public ResponHeader deleteItem(@Argument String id) {
        return itemService.deleteItem(id);
    }
}
