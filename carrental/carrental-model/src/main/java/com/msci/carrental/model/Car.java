package com.msci.carrental.model;

import com.msci.carrental.type.CarCategory;
import com.msci.carrental.type.Fuel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Database entity holding car details.
 *
 */
@Entity
@Table(name = "CAR")
public class Car {
    /** Unique VIN value identifying a car. */
    @Id
    @Column(name = "VIN")
    private String vin;
    /** Maker (company) of the car. */
    @Column(name = "MAKER")
    private String make;
    /** Model of the car. */
    @Column(name = "MODEL")
    private String model;
    /** Year of production. */
    @Column(name = "YEAR_OF_PRODUCTION")
    private int yearOfproduction;
    /** Type of fuel used. */
    @Column(name = "FUEL_TYPE")
    @Enumerated(EnumType.STRING)
    private Fuel fuelType;
    /** Plate value of the car. */
    @Column(name = "PLATE")
    private String plate;
    /** Category of the car. */
    @Column(name = "CAR_CATEGORY")
    @Enumerated(EnumType.STRING)
    private CarCategory category;

    public String getVin() {
        return vin;
    }

    public void setVin(final String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(final String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public int getYearOfproduction() {
        return yearOfproduction;
    }

    public void setYearOfproduction(final int yearOfmake) {
        yearOfproduction = yearOfmake;
    }

    public Fuel getFuelType() {
        return fuelType;
    }

    public void setFuelType(final Fuel fuelType) {
        this.fuelType = fuelType;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(final String plate) {
        this.plate = plate;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(final CarCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
