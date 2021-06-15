package com.repository;

import com.domain.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTemperatureRepository extends JpaRepository<Temperature,Integer> {
    List<Temperature> findAllByEmail(String Email);
}
