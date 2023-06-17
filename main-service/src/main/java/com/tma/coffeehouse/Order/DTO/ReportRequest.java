package com.tma.coffeehouse.Order.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    String format;
    Date startDate;
    Date endDate;
}
