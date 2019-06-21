package com.msci.carrental;

import com.msci.carrental.exception.CarRentalException;
import com.msci.carrental.model.Booking;
import com.msci.carrental.model.Car;

import java.util.Date;
import java.util.List;

/**
 * Service interface for car rental related operations.
 *
 */
public interface CarRentalService {
    /**
     * Lists all cars available for rental in the moment.
     *
     * @return (potentially empty) {@link List} of available cars
     */
    List<Car> findAvailableCars();

    /**
     * Gets the details of a car identified by it's unique VIN number.
     *
     * @param vin
     *            VIN number value
     * @return Details of the car, or <code>null</code> if there is no car for the VIN number
     */
    Car getCarDetails(String vin);

    /**
     * Books the rental of a car for the given time period. Checks if the car is available in that
     * time period and is allowed to go to the specified countries.
     *
     * @param vin
     *            VIN number value
     * @param fromDate
     *            booking start date
     * @param toDate
     *            booking end date
     * @param foreignCountries
     *            Array of (foreign) country names the car will be driven in. Should be
     *            <code>null</code> for domestic use only!
     * @return Details of successful booking
     * @throws CarRentalException
     *             If a validation error occurs (no car for VIN number, invalid date range), the car
     *             is already booked for that time or it's not allowed to be driven in the countries
     *             given.
     */
    Booking bookCar(String vin, Date fromDate, Date toDate, String[] foreignCountries)
            throws CarRentalException;
}
