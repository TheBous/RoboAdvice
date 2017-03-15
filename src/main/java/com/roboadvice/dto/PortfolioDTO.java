package com.roboadvice.dto;

import com.roboadvice.model.Portfolio;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PortfolioDTO {

    private BigDecimal totalAmount;

    private BigDecimal bondsAmount;
    private BigDecimal stocksAmount;
    private BigDecimal forexAmount;
    private BigDecimal commoditiesAmount;

    private BigDecimal bondsPercentage;
    private BigDecimal stocksPercentage;
    private BigDecimal forexPercentage;
    private BigDecimal commoditiesPercentage;

    private LocalDate date;

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

    public void setAssetsClassPercentage(long id, BigDecimal value){
        if(id==1) this.bondsPercentage=value;
        if(id==2) this.stocksPercentage=value;
        if(id==3) this.forexPercentage=value;
        if(id==4) this.commoditiesPercentage=value;
    }

    public BigDecimal getAssetsClassPercentage(long id){
        if(id==1) return this.bondsPercentage;
        if(id==2) return this.stocksPercentage;
        if(id==3) return this.forexPercentage;
        return this.commoditiesPercentage;
    }


}
