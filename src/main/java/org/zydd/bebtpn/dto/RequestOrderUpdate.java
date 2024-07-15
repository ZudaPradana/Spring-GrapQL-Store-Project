package org.zydd.bebtpn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestOrderUpdate {
    private Long quantity;
    private Long itemId;
}
