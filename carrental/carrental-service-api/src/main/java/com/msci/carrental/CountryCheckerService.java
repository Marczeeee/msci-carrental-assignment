package com.msci.carrental;

/**
 *
 * Service interface for (external) country checker services.
 */
public interface CountryCheckerService {
    /**
     * Performs a check to decide if a certain car is allowed be taken to all of the countries
     * given.
     *
     * @param vin
     *            VIN of the car
     * @param countries
     *            array of countries
     * @return <code>true</code> if the car is allowed to go all of the countries,
     *         <code>false</code> if it isn't
     */
    boolean isCountriesAllowedForCar(String vin, String[] countries);
}
