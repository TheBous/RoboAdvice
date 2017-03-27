package com.roboadvice.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "portfolio", indexes = {@Index(columnList = "date, user_id")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false, precision = 15, scale = 5)
    private BigDecimal amount;
    @Column(nullable = false, precision = 15, scale = 5)
    private BigDecimal units;
    @Column(nullable = false, precision = 15, scale = 5)
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assets_id")
    private Assets assets;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assets_class_id")
    private AssetsClass assetsClass;

    public Portfolio(AssetsClass assetsClass, BigDecimal value, LocalDate date){
        this.assetsClass = assetsClass;
        this.value = value;
        this.date = date;
    }

    public Portfolio(BigDecimal amount, LocalDate date, User u){
        this.amount = amount;
        this.date = date;
        this.user = u;
    }

}
