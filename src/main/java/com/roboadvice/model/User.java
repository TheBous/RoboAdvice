package com.roboadvice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user", indexes = {@Index(columnList = "email, password")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    /*@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Strategy> strategies;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Portfolio> portfolios;*/
}
