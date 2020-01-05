package com.bmukorera.tutorial.bigchainspringboot.model;

import java.io.Serializable;

public class Car implements Serializable {

    private String model;
    private String color;
    private String chasisNumber;
    private String manufactureYear;


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getChasisNumber() {
        return chasisNumber;
    }

    public void setChasisNumber(String chasisNumber) {
        this.chasisNumber = chasisNumber;
    }

    public String getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Car{");
        sb.append("model='").append(model).append('\'');
        sb.append(", color='").append(color).append('\'');
        sb.append(", chasisNumber='").append(chasisNumber).append('\'');
        sb.append(", manufactureYear='").append(manufactureYear).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
