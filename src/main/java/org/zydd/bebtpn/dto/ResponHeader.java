package org.zydd.bebtpn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponHeader {
    private String code;
    private Boolean status;
    private String message;
}
