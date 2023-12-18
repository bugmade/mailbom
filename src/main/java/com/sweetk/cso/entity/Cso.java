package com.sweetk.cso.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long csoNo;

    @Column(nullable = false, unique = true)
    private String csoCd;

    @Column(nullable = false)
    private String csoNm;

}
