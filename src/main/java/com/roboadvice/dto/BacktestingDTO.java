package com.roboadvice.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BacktestingDTO {

    private BigDecimal totalAmount;
    private LocalDate date;


}
