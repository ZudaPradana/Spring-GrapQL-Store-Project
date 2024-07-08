package org.zydd.bebtpn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private Long stock;
    private BigDecimal price;
    private Boolean isAvailable;
    private LocalDateTime lastRestock;
    @OneToMany(mappedBy = "items", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Orders> orders;

    public void setStock(Long stock) {
        this.stock = stock;
        this.isAvailable = stock > 0;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }
}
