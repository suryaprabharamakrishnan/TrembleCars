package com.trimbleCars.repository;

import com.trimbleCars.model.Car;
import com.trimbleCars.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByOwner(User user);

}
