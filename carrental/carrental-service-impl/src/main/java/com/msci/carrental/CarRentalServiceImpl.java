package com.msci.carrental;

import com.msci.carrental.exception.CarRentalErrorCodes;
import com.msci.carrental.exception.CarRentalException;
import com.msci.carrental.model.Booking;
import com.msci.carrental.model.Car;
import com.msci.carrental.repository.BookingRepository;
import com.msci.carrental.repository.CarRepository;
import com.msci.carrental.type.CarUsage;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation class of {@link CarRentalService}.
 *
 */
@Component
public class CarRentalServiceImpl implements CarRentalService {
    /** {@link Logger} instance. */
    private final Logger logger = LoggerFactory.getLogger(CarRentalService.class);
    /** Country checker service bean. */
    @Autowired
    private CountryCheckerService countryCheckerService;
    /** Car repository bean. */
    @Autowired
    private CarRepository carRepository;
    /** Booking repository bean. */
    @Autowired
    private BookingRepository bookingRepository;

    public List<Car> findAvailableCars() {
        final List<String> bookedCarsAtDate = bookingRepository.findAllBookedCarsAtDate(new Date());
        logger.debug("Found {} cars currently booked: {}", bookedCarsAtDate.size(),
                bookedCarsAtDate);
        List<Car> nonBookedCarsAtDate;
        if (bookedCarsAtDate.isEmpty()) {
            nonBookedCarsAtDate = carRepository.findAll();
        } else {
            nonBookedCarsAtDate = carRepository.findAllNonListedCars(bookedCarsAtDate);
        }
        logger.debug("Found {} cars available currently: {}", nonBookedCarsAtDate.size(),
                nonBookedCarsAtDate);
        return nonBookedCarsAtDate;
    }

    public Car getCarDetails(final String vin) {
        return carRepository.findOne(vin);
    }

    public Booking bookCar(
            final String vin,
            final Date fromDate,
            final Date toDate,
            final String[] countries) throws CarRentalException {
        validateBooking(vin, fromDate, toDate, countries);

        final Booking booking = new Booking();
        booking.setBookedCar(getCarDetails(vin));
        booking.setFromDate(fromDate);
        booking.setToDate(toDate);
        booking.setUsage(
                countries == null || countries.length == 0 ? CarUsage.DOMESTIC : CarUsage.FOREIGN);

        return bookingRepository.save(booking);
    }

    /**
     * Validates booking data details. Every validation error results in a
     * {@link CarRentalException}.
     *
     * @param vin
     *            car VIN value
     * @param fromDate
     *            booking opening date
     * @param toDate
     *            booking ending date
     * @param countries
     *            array of (foreign) country names to be checked. Can be <code>null</code> value.
     * @throws CarRentalException
     *             If any validation fails. Appropriate error code and error message is set for
     *             every validation error.
     */
    private void validateBooking(
            final String vin,
            final Date fromDate,
            final Date toDate,
            final String[] countries) throws CarRentalException {
        if (StringUtils.isBlank(vin)) {
            throw new CarRentalException(CarRentalErrorCodes.ERROR_BOOKING_MISSING_VIN,
                    "Missing VIN value");
        }

        if (fromDate == null) {
            throw new CarRentalException(CarRentalErrorCodes.ERROR_BOOKING_MISSING_FROM_DATE,
                    "Missing rental opening date value");
        }

        if (toDate == null) {
            throw new CarRentalException(CarRentalErrorCodes.ERROR_BOOKING_MISSING_TO_DATE,
                    "Missing rental ending date value");
        }

        if (toDate.before(fromDate)) {
            throw new CarRentalException(CarRentalErrorCodes.ERROR_BOOKING_TO_DATE_BEFORE_FROM_DATE,
                    "Rental ending date (" + toDate + ") if earlier than opening date (" + fromDate
                            + ")");
        }

        final Date today = new Date();
        if (fromDate.before(today)) {
            throw new CarRentalException(CarRentalErrorCodes.ERROR_BOOKING_INVALID_FROM_DATE,
                    "Rental opening date can't be earlier than today");
        }

        final Car carDetails = getCarDetails(vin);
        if (carDetails == null) {
            throw new CarRentalException(CarRentalErrorCodes.ERROR_BOOKING_NONEXISTENT_CAR,
                    "No car was found for VIN=" + vin);
        }

        final List<Booking> booking =
                bookingRepository.findBookingOfCarInDatePeriod(vin, fromDate, toDate);
        if (booking != null && !booking.isEmpty()) {
            throw new CarRentalException(CarRentalErrorCodes.ERROR_BOOKING_CAR_IS_BOOKED,
                    "Car (VIN=" + vin + ") is booked in time period: " + fromDate + " - " + toDate);
        }

        final boolean isCarAllowedToCountries =
                countryCheckerService.isCountriesAllowedForCar(vin, countries);
        if (!isCarAllowedToCountries) {
            throw new CarRentalException(
                    CarRentalErrorCodes.ERROR_BOOKING_FORBIDDEN_FOREIGN_COUNTRY_USAGE,
                    "Car (VIN=" + vin + ") is not allowed to be used in foreign countries: "
                            + Arrays.toString(countries));
        }
    }
}
