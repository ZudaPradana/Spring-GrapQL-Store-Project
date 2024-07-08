package org.zydd.bebtpn.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RequestCustomerCreate {
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerCode;
    private String pic;
    private String username;
    private String password;
}
