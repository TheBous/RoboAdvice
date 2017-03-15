package com.roboadvice.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class StrategyDTO {

    private String name;
    private Boolean active;
    
    private LocalDate date;
    @NotNull
    private BigDecimal bonds_p;
    @NotNull
    private BigDecimal stocks_p;
    @NotNull
    private BigDecimal forex_p;
    @NotNull
    private BigDecimal commodities_p;

    public BigDecimal getPercentage(long id){
        if(id==1) return this.bonds_p;
        if(id==2) return this.stocks_p;
        if(id==3) return this.forex_p;
        return this.commodities_p;
    }

    public void setPercentage(long id, BigDecimal value){
        if(id==1) this.bonds_p=value;
        if(id==2) this.stocks_p=value;
        if(id==3) this.forex_p=value;
        if(id==4) this.commodities_p=value;
    }


}
