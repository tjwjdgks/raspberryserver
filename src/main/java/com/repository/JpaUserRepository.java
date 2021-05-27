package com.repository;

import com.domain.UserAccount;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class JpaUserRepository {
    private final EntityManager em;

    public JpaUserRepository(EntityManager em) {
        this.em = em;
    }
    public UserAccount save(UserAccount userAccount){
        em.persist(userAccount);
        return userAccount;
    }
}
