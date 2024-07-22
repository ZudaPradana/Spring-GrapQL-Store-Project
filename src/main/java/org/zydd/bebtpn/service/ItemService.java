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

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ResponGetAllData<Items> getAllData(String itemName, Boolean isAvailable, int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Items> itemsPage;
        if (itemName != null && isAvailable != null) {
            itemsPage = itemRepository.findByItemNameContainingIgnoreCaseAndIsAvailable(itemName, isAvailable, pageable);
        } else if (itemName != null) {
            itemsPage = itemRepository.findByItemNameContainingIgnoreCase(itemName, pageable);
        } else if (isAvailable != null) {
            itemsPage = itemRepository.findByIsAvailable(isAvailable, pageable);
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
        ResponHeader header;
        try {
            Items newItem = new Items(
                    request.getItemName(),
                    request.getItemDescription(),
                    request.getStock() != null ? request.getStock() : 0,
                    request.getPrice() != null ? request.getPrice() : 0
            );
            itemRepository.save(newItem);
            header = ResponHeaderMessage.getRequestSuccess();
            header.setMessage("Success Create Item");
        } catch (Exception e) {
            header = ResponHeaderMessage.getBadRequestError();
            header.setMessage(e.getMessage());
        }
        return header;
    }

    @Transactional
    public ResponHeader updateItem(String itemId ,RequestItemCreateUpdate request) {
        Long id = Long.parseLong(itemId);
        Optional<Items> existingItem = itemRepository.findById(id);

        if (existingItem.isPresent()) {
            Items item = existingItem.get();

            if (request.getItemName() != null) {
                item.setItemName(request.getItemName());
            }
            if (request.getStock() != null) {
                item.setStock(request.getStock());
                item.setLastRestock(LocalDateTime.now());
            }
            if (request.getPrice() != null) {
                item.setPrice(request.getPrice());
            }
            if (request.getItemDescription() != null) {
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
