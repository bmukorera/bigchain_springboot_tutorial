package com.bmukorera.tutorial.bigchainspringboot.repository;

import com.bmukorera.tutorial.bigchainspringboot.model.Car;

import java.util.List;

public interface CarRepository {
    Car save(Car car);
    List<Car> search(String searchKey);
}
