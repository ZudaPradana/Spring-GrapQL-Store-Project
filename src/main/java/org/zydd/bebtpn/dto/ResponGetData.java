package org.zydd.bebtpn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zydd.bebtpn.entity.Customers;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponGetData {
    private ResponHeader header;
    private Customers data;
}
