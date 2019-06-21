package com.msci.carrental.server.controller;

import com.msci.carrental.CarRentalService;
import com.msci.carrental.exception.CarRentalException;
import com.msci.carrental.model.Booking;
import com.msci.carrental.model.Car;
import com.msci.carrental.rest.BookingReservationDetails;
import com.msci.carrental.rest.BookingResult;
import com.msci.carrental.rest.BookingResult.BookingProcessResult;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class providing RESTFul service for car rental related actions.
 *
 */
@RestController
public class CarRentalServiceController {
    /** {@link Logger} instance. */
    private final Logger logger = LoggerFactory.getLogger(CarRentalServiceController.class);
    /** {@link CarRentalService} bean. */
    @Autowired
    private CarRentalService carRentalService;

    /**
     * Lists all cars available for rental.
     *
     * @return {@link List} of cars
     */
    @RequestMapping(value = "/availableCars", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody List<Car> getAvailableCars() {
        logger.info("Requesting all currently ({}) available cars for rental", new Date());
        final List<Car> availableCars = carRentalService.findAvailableCars();
        logger.info("Returning {} available cars for booking", availableCars.size());
        if (logger.isDebugEnabled()) {
            logger.debug("Returning rental-available cars: {}", availableCars);
        }
        return availableCars;
    }

    /**
     * Returns the details of a specific car identified by VIN number.
     *
     * @param vin
     *            car VIN value
     * @return details of a car, or <code>null</code> if no car was found with the given VIN number
     */
    @RequestMapping(value = "/carDetails/{vin}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Car getCarDetails(@PathVariable final String vin) {
        logger.info("Requesting car details for VIN number: {}", vin);
        final Car carDetails = carRentalService.getCarDetails(vin);
        logger.info("Returning vin-based ({}) car details: {}", vin, carDetails);
        return carDetails;
    }

    /**
     * Performs a booking of a car for a give date period.
     *
     * @param bookingDetails
     *            object containing booking date
     * @return result of the booking process
     */
    @RequestMapping(value = "/bookCar", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody BookingResult bookCar(
            @RequestBody final BookingReservationDetails bookingDetails) {
        logger.info("Received car rental booking details: {}", bookingDetails);
        BookingResult bookingResult;
        try {
            final Booking booking =
                    carRentalService.bookCar(bookingDetails.getVin(), bookingDetails.getFromDate(),
                            bookingDetails.getToDate(), bookingDetails.getForeignCountries());
            bookingResult = new BookingResult(booking, BookingProcessResult.SUCCESS);
            logger.debug("Booking of car (VIN={}) for time period ({} - {}) was successful",
                    bookingDetails.getVin(), bookingDetails.getFromDate(),
                    bookingDetails.getToDate());
        } catch (final CarRentalException e) {
            logger.error("Booking of car (VIN={}) for time period ({} - {}) failed: {}",
                    bookingDetails.getVin(), bookingDetails.getFromDate(),
                    bookingDetails.getToDate(), e.getMessage());
            bookingResult = new BookingResult(e.getErrorCode(), e.getMessage());
        }

        logger.info("Returning booking result: {}", bookingResult);
        return bookingResult;
    }
}
