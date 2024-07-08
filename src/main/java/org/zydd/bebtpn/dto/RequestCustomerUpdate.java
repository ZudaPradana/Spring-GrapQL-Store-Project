package org.zydd.bebtpn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RequestCustomerUpdate {
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerCode;
    private String pic;
}
