package com.sweetk.cso.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class PharmComp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pharmCompNo;
    private String pharmCompCd;
    private String pharmCompNm;
    private String bizRegNo;
    private String repNm;
    private String taxTypeCcd;
    private BigDecimal leastAmt;
    private String zipNo;
    private String addr;
    private String addrDtl;

}
