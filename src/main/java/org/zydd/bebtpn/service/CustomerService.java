package org.zydd.bebtpn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zydd.bebtpn.dto.RequestCustomerCreate;
import org.zydd.bebtpn.dto.RequestCustomerUpdate;
import org.zydd.bebtpn.entity.Customers;
import org.zydd.bebtpn.repository.CustomerRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class CustomerService {
    private final CustomerRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${spring.servlet.multipart.location}")
    private String photoLocation;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Page<Customers> getAllCustomers(String name, Boolean isActive, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);

        if (name != null && isActive != null) {
            return repository.findByCustomerNameContainingIgnoreCaseAndIsActive(name, isActive, pageable);
        } else if (name != null) {
            return repository.findByCustomerNameContainingIgnoreCase(name, pageable);
        } else if (isActive != null) {
            return repository.findByIsActive(isActive, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    public Customers getCustomerById(String id) {
        Long customerId = Long.parseLong(id);
        Customers customerExisting =  repository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        customerExisting.setPic(photoLocation+"/"+customerExisting.getPic());
        return customerExisting;
    }

    public Customers updateCustomer(String id, RequestCustomerUpdate request) {
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
        if (request.getCustomerCode() != null) {
            existingCustomer.setCustomerCode(request.getCustomerCode());
        }
        if (request.getCustomerPhone() != null) {
            existingCustomer.setCustomerPhone(request.getCustomerPhone());
        }
        if (request.getPic() != null) {
            existingCustomer.setPic(request.getPic());
        }

        // Save the updated customer
        return repository.save(existingCustomer);
    }

    public Customers createCustomer(RequestCustomerCreate request) {
        // Hash password using BCryptPasswordEncoder
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        Customers customer = Customers
                .builder()
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .customerCode(hashedPassword)
                .customerAddress(request.getCustomerAddress())
                .username(request.getUsername())
                .customerCode(request.getCustomerCode())
                .build();
        // Decode base64 image data if present
        if (request.getPic() != null) {
            try {
                // Remove data URI prefix if present
                String base64Data = request.getPic().replaceFirst("data:image/.*;base64,", "");

                // Decode base64 string into bytes
                byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

                // Generate file name (or use a UUID)
                String fileName = request.getCustomerCode() + "_" + request.getCustomerName() + ".jpg";

                // Specify the file path
                Path path = Paths.get("src/main/resources/photo/" + fileName);

                // Write the decoded bytes to file
                Files.write(path, decodedBytes);

                customer.setPic(fileName);

            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception if unable to save file
            }
        }

        return repository.save(customer);
    }

    public void deleteCustomer(String id) {
        Long customerId = Long.parseLong(id);
        Customers customer = repository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        repository.delete(customer);
    }
}

