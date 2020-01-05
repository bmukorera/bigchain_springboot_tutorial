package com.bmukorera.tutorial.bigchainspringboot;

import com.bmukorera.tutorial.bigchainspringboot.model.Car;
import com.bmukorera.tutorial.bigchainspringboot.repository.CarRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;

import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
public class BigchainspringbootApplicationTests {

    CarRepositoryImpl carRepository;
    Car car;

    @Before
    public void setup(){
        car=new Car();
        car.setChasisNumber("123456");
        car.setColor(Color.BLUE.toString());
        car.setManufactureYear("2017");
        car.setModel("NISSAN");
        carRepository=new CarRepositoryImpl();
    }


    @Test
    public void shouldReturnSuccessIfInfomationIsValid(){
        carRepository.save(car);
    }

    @Test
    public void shouldReturnSuccessfulResultIfCarIsAvailable(){
        assertNotNull(carRepository.search("NISSAN"));
    }

}
