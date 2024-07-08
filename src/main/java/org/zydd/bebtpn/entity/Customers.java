package org.zydd.bebtpn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customers {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_seq")
    @Column(unique = true, nullable = false, name = "customer_id")
    @SequenceGenerator(name = "customers_seq", sequenceName = "customers_seq", allocationSize = 1)
    private long customerId;

    private String customerName;
    private String customerAddress;
    private String customerCode;
    private String customerPhone;
    private Boolean isActive;
    private LocalDateTime lastOrderDate;
    private String pic;
    private String username;
    private String password;

    @OneToMany(mappedBy = "customers", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Orders> orders;
}
