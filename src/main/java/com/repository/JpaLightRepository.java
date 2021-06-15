package com.repository;

import com.domain.Light;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaLightRepository extends JpaRepository<Light,Integer> {
    List<Light> findAllByEmail(String Email);
}
