package org.zydd.bebtpn.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zydd.bebtpn.dto.*;
import org.zydd.bebtpn.entity.Customers;
import org.zydd.bebtpn.entity.Items;
import org.zydd.bebtpn.entity.Orders;
import org.zydd.bebtpn.repository.CustomerRepository;
import org.zydd.bebtpn.repository.ItemRepository;
import org.zydd.bebtpn.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public ResponHeader createOrder(RequestOrderCreate request) {
        Long customerIdLong = Long.parseLong(request.getCustomerId());
        Long itemIdLong = Long.parseLong(request.getItemId());

        Customers customer = customerRepository.findById(customerIdLong)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Items item = itemRepository.findById(itemIdLong)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getStock() > request.getQuantity()) {
            Orders newOrder = Orders
                    .builder()
                    .orderDate(LocalDateTime.now())
                    .orderCode(UUID.randomUUID().toString().substring(0, 5))
                    .quantity(request.getQuantity())
                    .totalPrice(totalAmount(request.getQuantity(), item.getPrice()))
                    .customers(customer)
                    .items(item)
                    .build();
            orderRepository.save(newOrder);

            ResponHeader header = ResponHeaderMessage.getDataCreated();
            header.setMessage("Order created successfully");
            return header;
        } else {
            ResponHeader header = ResponHeaderMessage.getBadRequestError();
            header.setMessage("Insufficient stock for item");
            return header;
        }
    }


    private Long totalAmount(Long qty, Long price){
        return qty*price;
    }

    public ResponGetAllData<Orders> getAllOrder(String customerName, String itemName, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Orders> ordersPage;

        if (customerName != null && itemName != null) {
            ordersPage = orderRepository.findByCustomerNameAndItemName(customerName, itemName, pageable);
        } else if (customerName != null) {
            ordersPage = orderRepository.findByCustomerName(customerName, pageable);
        } else if (itemName != null) {
            ordersPage = orderRepository.findByItemName(itemName, pageable);
        } else {
            ordersPage = orderRepository.findAll(pageable);
        }

        ResponHeader header = ResponHeaderMessage.getRequestSuccess();
        return new ResponGetAllData<>(header, ordersPage.getContent());
    }

    public ResponGetData<Orders> getOrder(String orderId){
        Long id = Long.parseLong(orderId);
        Optional<Orders> order = orderRepository.findById(id);
        if (order.isPresent()) {
            Orders orderData = order.get();
            ResponHeader header = ResponHeaderMessage.getRequestSuccess();
            return new ResponGetData<>(header, orderData);
        }
        ResponHeader header = ResponHeaderMessage.getDataNotFound();
        return new ResponGetData<>(header, null);
    }

    public ResponHeader deleteOrder(String orderId){
        Long id = Long.parseLong(orderId);
        Optional<Orders> order = orderRepository.findById(id);
        if (order.isPresent()) {
            Orders orderData = order.get();
            orderRepository.delete(orderData);
            ResponHeader header = ResponHeaderMessage.getRequestSuccess();
            header.setMessage("Order deleted successfully");
            return header;
        }
        ResponHeader header = ResponHeaderMessage.getDataNotFound();
        header.setMessage("Order not found");
        return header;
    }

    @Transactional
    public ResponHeader updateOrder(String orderId, RequestOrderUpdate request) {
        Long id = Long.parseLong(orderId);
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (request.getItemId() != null) {
            Items item = itemRepository.findById(request.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            order.setItems(item);
            order.setTotalPrice(totalAmount(order.getQuantity(), item.getPrice()));
        }

        if (request.getQuantity() != null) {
            order.setQuantity(request.getQuantity());
            order.setTotalPrice(totalAmount(request.getQuantity(), order.getItems().getPrice()));
        }

        orderRepository.save(order);

        ResponHeader header = ResponHeaderMessage.getRequestSuccess();
        header.setMessage("Order updated successfully");
        return header;
    }
}
