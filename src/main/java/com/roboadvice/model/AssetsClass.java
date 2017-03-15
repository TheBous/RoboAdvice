package com.roboadvice.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "assets_class")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class AssetsClass {

    @Id
    private long id;

    @Column(nullable = false)
    private String name;

    /*@OneToMany(mappedBy = "assetsClass", fetch = FetchType.LAZY)
    private List<Assets> assets;

    @OneToMany(mappedBy = "assetsClass", fetch = FetchType.LAZY)
    private List<Strategy> strategies;

    @OneToMany(mappedBy = "assetsClass", fetch = FetchType.LAZY)
    private List<Portfolio> portfolios;*/
}
