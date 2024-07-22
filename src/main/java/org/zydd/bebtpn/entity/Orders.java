package org.zydd.bebtpn.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false, name = "order_id")
    private Long orderId;

    private String orderCode;
    private LocalDateTime orderDate;
    private Long totalPrice;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonProperty("customerName")
    private Customers customers;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonProperty("itemName")
    private Items items;
    private Long quantity;


    @JsonGetter("customerName")
    public String getCustomerName() {
        return customers != null ? customers.getCustomerName() : null;
    }

    @JsonGetter("itemName")
    public String getItemName() {
        return items != null ? items.getItemName() : null;
    }
}
