package com.roboadvice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "strategy")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Strategy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private Boolean active;
    @Column(nullable = false, precision = 7, scale = 4)
    private BigDecimal percentage;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "assets_class_id")
    private AssetsClass assetsClass;

}
