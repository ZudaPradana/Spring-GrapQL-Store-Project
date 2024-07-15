package org.zydd.bebtpn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestOrderCreate {
    private String customerId;
    private String itemId;
    private Long quantity;
}
