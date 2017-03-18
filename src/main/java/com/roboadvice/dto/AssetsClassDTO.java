package com.roboadvice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AssetsClassDTO {

    private BigDecimal bondsValue;
    private BigDecimal stocksValue;
    private BigDecimal forexValue;
    private BigDecimal commoditiesValue;

    private LocalDate date;
}
