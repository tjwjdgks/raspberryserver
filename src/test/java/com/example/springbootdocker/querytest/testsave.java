package com.example.springbootdocker.querytest;

import com.domain.UserAccount;
import com.repository.JpaUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class testsave {
    @Autowired
    JpaUserRepository jpaUserRepository;
    @Test
    public void insert(){
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail("test");
        userAccount.setName("tt");
        userAccount.setPassword("ttt");
        jpaUserRepository.save(userAccount);
    }
}
