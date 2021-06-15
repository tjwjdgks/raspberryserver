package com.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "temperature_id",nullable = false)
    private int id;
    @Column(name = "email",nullable = false)
    private String email;
    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "value", nullable = false)
    private float value;
}