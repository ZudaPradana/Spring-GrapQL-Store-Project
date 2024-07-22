package org.zydd.bebtpn.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zydd.bebtpn.dto.*;
import org.zydd.bebtpn.entity.Customers;
import org.zydd.bebtpn.repository.CustomerRepository;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private final MinioService minioService;
    private final CustomerRepository repository;

    @Autowired
    public CustomerService(MinioService minioService, CustomerRepository repository) {
        this.minioService = minioService;
        this.repository = repository;
    }

    public ResponGetAllData<Customers> getAllCustomers(String name, Boolean isActive, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Customers> customerPage;

        if (name != null && isActive != null) {
            customerPage = repository.findByCustomerNameContainingIgnoreCaseAndIsActive(name, isActive, pageable);
        } else if (name != null) {
            customerPage = repository.findByCustomerNameContainingIgnoreCase(name, pageable);
        } else if (isActive != null) {
            customerPage = repository.findByIsActive(isActive, pageable);
        } else {
            customerPage = repository.findAll(pageable);
        }

        ResponHeader header = ResponHeaderMessage.getRequestSuccess();

        return new ResponGetAllData<>(header, customerPage.getContent());
    }


    public ResponGetData getCustomerById(String id) {
        Long customerId = Long.parseLong(id);
        Optional<Customers> customerExisting = repository.findById(customerId);

        if (customerExisting.isPresent()) {
            Customers customer = customerExisting.get();
            customer.setPic(minioService.getObjectUrl(customer.getPic()));
            ResponHeader header = ResponHeaderMessage.getRequestSuccess();
            return new ResponGetData(header, customer);
        }

        ResponHeader header = ResponHeaderMessage.getDataNotFound();
        return new ResponGetData(header, null);
    }


    @Transactional
    public ResponHeader updateCustomer(String id, RequestCustomerCreateUpdate request) {
        Long customerId = Long.parseLong(id);
        Customers existingCustomer = repository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Update fields based on updatedCustomer
        if (request.getCustomerName() != null) {
            existingCustomer.setCustomerName(request.getCustomerName());
        }
        if (request.getCustomerAddress() != null) {
            existingCustomer.setCustomerAddress(request.getCustomerAddress());
        }
        if (request.getCustomerPhone() != null) {
            existingCustomer.setCustomerPhone(request.getCustomerPhone());
        }
        if (request.getPic() != null) {
            existingCustomer.setPic(request.getPic());
        }

        // Save the updated customer
        repository.save(existingCustomer);
        ResponHeader header = ResponHeaderMessage.getRequestSuccess();
        header.setMessage("Customer updated successfully");
        return header;
    }

    @Transactional
    public ResponHeader createCustomer(RequestCustomerCreateUpdate request) {
        Customers customer = Customers
                .builder()
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .customerAddress(request.getCustomerAddress())
                .isActive(request.getIsActive())
                .build();
        customer.setCustomerCode(UUID.randomUUID().toString().substring(0, 5));
        // Decode base64 image data if present
        if (request.getPic() != null) {
            try {
                // Remove data URI prefix if present
                String base64Data = request.getPic().replaceFirst("data:image/.*;base64,", "");

                // Decode base64 string into bytes
                byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

                // Generate file name (or use a UUID)
                String fileName = customer.getCustomerCode() + "_" + customer.getCustomerName() + ".jpg";

                // Upload to MinIO
                minioService.uploadFile(fileName, decodedBytes);

                // Set the file name to the customer entity
                customer.setPic(fileName);

            } catch (Exception e) {
                ResponHeader header = ResponHeaderMessage.getBadRequestError();
                header.setMessage(e.getMessage());
                return header;
            }
        }
        repository.save(customer);
        ResponHeader header = ResponHeaderMessage.getRequestSuccess();
        header.setMessage("Customer created successfully");
        return header;
    }

    @Transactional
    public ResponHeader deleteCustomer(String id) {
        Long customerId = Long.parseLong(id);
        Optional<Customers> customer = repository.findById(customerId);

        if (customer.isPresent()) {
            Customers deleteCustomer = customer.get();
            try {
                // Get the filename from the customer entity
                String fileName = deleteCustomer.getPic();

                // Delete the customer entity from the repository
                repository.delete(deleteCustomer);

                // Delete the file from MinIO bucket if the filename exists
                if (fileName != null && !fileName.isEmpty()) {
                    minioService.deleteFile(fileName);
                }

                ResponHeader header = ResponHeaderMessage.getRequestSuccess();
                header.setMessage("Customer deleted successfully");
                return header;
            } catch (DataIntegrityViolationException e) {
                // Handle exception
                return null;
            }
        }

        return ResponHeaderMessage.getDataNotFound();
    }


}

