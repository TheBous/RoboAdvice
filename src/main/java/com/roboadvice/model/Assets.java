package com.roboadvice.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name="assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Assets {

    @Id
    private long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 7, scale = 4)
    private BigDecimal allocation_p;

    @Column(nullable = false)
    private String api_data_src;

    @Column(nullable = false)
    private int column_num;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assets_class_id")
    private AssetsClass assetsClass;

    /*@OneToMany(mappedBy = "assets")
    private List<ApiData> apiDatas;

    @OneToMany(mappedBy = "assets")
    private List<Portfolio> portfolios;*/




}
