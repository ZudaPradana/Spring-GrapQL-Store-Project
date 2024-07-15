package org.zydd.bebtpn.dto;

import lombok.*;
import org.zydd.bebtpn.entity.Customers;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponGetAllData<T> {
    private ResponHeader header;
    private List<T> data;
}
