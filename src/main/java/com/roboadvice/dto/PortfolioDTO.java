package com.roboadvice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PortfolioDTO {

    private BigDecimal totalAmount;
    private LocalDate date;

    private BigDecimal bondsAmount;
    private BigDecimal stocksAmount;
    private BigDecimal forexAmount;
    private BigDecimal commoditiesAmount;

    public void setAssetsClassAmount(long id, BigDecimal value){
        if(id==1) this.bondsAmount=value;
        if(id==2) this.stocksAmount=value;
        if(id==3) this.forexAmount=value;
        if(id==4) this.commoditiesAmount=value;
    }

    public BigDecimal getAssetsClassAmount(long id){
        if(id==1) return this.bondsAmount;
        if(id==2) return this.stocksAmount;
        if(id==3) return this.forexAmount;
        return this.commoditiesAmount;
    }

}
