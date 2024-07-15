package org.zydd.bebtpn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RequestItemCreateUpdate {
    private String itemName;
    private String itemDescription;
    private Long itemPrice;
    private Integer itemStock;
}