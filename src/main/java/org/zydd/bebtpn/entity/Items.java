package org.zydd.bebtpn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false, name = "item_id")
    private int itemId;

    private String itemName;
    private String itemCode;
    private String itemDescription;
    private Integer stock;
    private Long price;
    private Boolean isAvailable;
    private LocalDateTime lastRestock;
    @OneToMany(mappedBy = "items", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Orders> orders;

    public Items(String itemName, String itemDescription, Integer stock, Long price) {
        this.itemName = itemName;
        this.itemCode = generateItemCode();
        this.itemDescription = itemDescription;
        this.stock = stock;
        this.price = price;
        this.isAvailable = itemAvailable(stock);
    }

    public void setStock(Integer stock) {
        this.stock = stock;
        this.isAvailable = stock > 0;
    }

    private Boolean itemAvailable(Integer Stock){
        this.stock = Stock;
        this.isAvailable = stock > 0;
        return isAvailable;
    }

    private String generateItemCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 5);
    }
}
