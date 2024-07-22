package org.zydd.bebtpn.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RequestCustomerCreateUpdate {
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String pic;
    private Boolean isActive;
}
