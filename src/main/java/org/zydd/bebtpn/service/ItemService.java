package org.zydd.bebtpn.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zydd.bebtpn.dto.*;
import org.zydd.bebtpn.entity.Items;
import org.zydd.bebtpn.repository.ItemRepository;

import java.util.Optional;

@Service
public class ItemService {
    ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ResponGetAllData<Items> getAllData(String itemName, Boolean isAvailable, Float minPrice, Float maxPrice, int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Items> itemsPage;
        if (itemName != null && isAvailable != null && minPrice != null && maxPrice != null) {
            itemsPage = itemRepository.findByItemNameContainingIgnoreCaseAndIsAvailableAndPriceGreaterThanEqualAndPriceLessThanEqual(
                    itemName, isAvailable, minPrice, maxPrice, pageable);
        } else if (itemName != null && isAvailable != null) {
            itemsPage = itemRepository.findByItemNameContainingIgnoreCaseAndIsAvailable(itemName, isAvailable, pageable);
        } else if (itemName != null && minPrice != null && maxPrice != null) {
            itemsPage = itemRepository.findByItemNameContainingIgnoreCaseAndPriceGreaterThanEqualAndPriceLessThanEqual(
                    itemName, minPrice, maxPrice, pageable);
        } else if (isAvailable != null && minPrice != null && maxPrice != null) {
            itemsPage = itemRepository.findByIsAvailableAndPriceGreaterThanEqualAndPriceLessThanEqual(
                    isAvailable, minPrice, maxPrice, pageable);
        } else if (itemName != null) {
            itemsPage = itemRepository.findByItemNameContainingIgnoreCase(itemName, pageable);
        } else if (isAvailable != null) {
            itemsPage = itemRepository.findByIsAvailable(isAvailable, pageable);
        } else if (minPrice != null && maxPrice != null) {
            itemsPage = itemRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(minPrice, maxPrice, pageable);
        } else {
            itemsPage = itemRepository.findAll(pageable); // Fallback to all items
        }

        ResponHeader header = ResponHeaderMessage.getRequestSuccess();
        return new ResponGetAllData<>(header, itemsPage.getContent());
    }

    public ResponGetData getItem(String itemId) {
        Long id = Long.parseLong(itemId);
        Optional<Items> existingItem = itemRepository.findById(id);

        if (existingItem.isPresent()) {
            Items item = existingItem.get();
            ResponHeader header = ResponHeaderMessage.getRequestSuccess();
            return new ResponGetData(header, item);
        }

        ResponHeader header = ResponHeaderMessage.getDataNotFound();
        return new ResponGetData(header, null);
    }

    @Transactional
    public ResponHeader createItem(RequestItemCreateUpdate request) {
        try {
            Items newItem = new Items(
                    request.getItemName(),
                    request.getItemDescription(),
                    request.getItemStock(),
                    request.getItemPrice()
            );
            itemRepository.save(newItem);
        }catch (Exception e) {
            ResponHeader header = ResponHeaderMessage.getBadRequestError();
            header.setMessage(e.getMessage());
        }
        ResponHeader header = ResponHeaderMessage.getRequestSuccess();
        header.setMessage("Success Create Item");
        return header;
    }

    @Transactional
    public ResponHeader updateItem(String itemId ,RequestItemCreateUpdate request){
        Long id = Long.parseLong(itemId);
        Optional<Items> existingItem = itemRepository.findById(id);
        if (existingItem.isPresent()) {
            Items item = existingItem.get();
            if (request.getItemName() != null) {
                item.setItemName(request.getItemName());
            } else if (request.getItemStock() != null) {
                item.setStock(request.getItemStock());
            } else if (request.getItemPrice() != null) {
                item.setPrice(request.getItemPrice());
            } else if (request.getItemDescription() != null) {
                item.setItemDescription(request.getItemDescription());
            }
            itemRepository.save(item);
            ResponHeader header = ResponHeaderMessage.getRequestSuccess();
            header.setMessage("Success Update Item");
            return header;
        }
        return ResponHeaderMessage.getBadRequestError();
    }

    @Transactional
    public ResponHeader deleteItem(String itemId) {
        Long id = Long.parseLong(itemId);
        Optional<Items> existingItem = itemRepository.findById(id);
        if (existingItem.isPresent()) {
            itemRepository.delete(existingItem.get());
            return ResponHeaderMessage.getRequestSuccess();
        }
        return ResponHeaderMessage.getBadRequestError();
    }

}
