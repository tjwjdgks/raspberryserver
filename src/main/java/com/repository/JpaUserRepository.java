package com.repository;

import com.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public interface JpaUserRepository extends JpaRepository<UserAccount,Integer> {
    List<UserAccount> findByEmailAndPassword(String Email,String Password);
}
