package com.domain;

import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Table(name="user_account")
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id",nullable = false)
    private int id;
    @Column(name = "email_test",nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
}
