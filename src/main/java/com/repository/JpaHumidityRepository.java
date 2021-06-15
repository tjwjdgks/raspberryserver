package com.repository;

import com.domain.Humidity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaHumidityRepository extends JpaRepository<Humidity,Integer> {
    List<Humidity> findAllByEmail(String Email);
}
