package com.service;


import com.repository.JpaUserRepository;


public class UserService {
    private JpaUserRepository jpaUserRepository;
    public UserService(JpaUserRepository jpaUserRepository){
        this.jpaUserRepository = jpaUserRepository;
    }
}
