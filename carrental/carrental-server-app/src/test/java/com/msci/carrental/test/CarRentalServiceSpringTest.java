package com.msci.carrental.test;

import com.msci.carrental.CarRentalService;
import com.msci.carrental.exception.CarRentalErrorCodes;
import com.msci.carrental.exception.CarRentalException;
import com.msci.carrental.model.Booking;
import com.msci.carrental.model.Car;
import com.msci.carrental.server.Application;
import com.msci.carrental.type.CarUsage;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CarRentalServiceSpringTest {
    @Autowired
    private CarRentalService carRentalService;

    private final Random random = new SecureRandom();

    @Test
    public void testFindAvailableCars() {
        final List<Car> availableCars = carRentalService.findAvailableCars();
        Assert.assertNotNull(availableCars);
        Assert.assertFalse(availableCars.isEmpty());
    }

    @Test(expected = Exception.class)
    public void testGetCarDetailsWithNullVin() {
        carRentalService.getCarDetails(null);
    }

    @Test
    public void testGetCarDetailsWithEmptyVin() {
        final Car carDetails = carRentalService.getCarDetails("");
        Assert.assertNull(carDetails);
    }

    @Test
    public void testGetCarDetailsWithValidVin() {
        final List<Car> availableCars = carRentalService.findAvailableCars();
        for (final Car car : availableCars) {
            final Car carDetails = carRentalService.getCarDetails(car.getVin());
            Assert.assertNotNull(carDetails);
            Assert.assertEquals(car, carDetails);
        }
    }

    @Test
    public void testBookCarWithSuccessfulBooking() {
        final Car car = selectOneExistingCar();
        final Date fromDate = DateUtils.addDays(new Date(), 1);
        final Date toDate = DateUtils.addDays(new Date(), 2);
        final Booking booking = carRentalService.bookCar(car.getVin(), fromDate, toDate, null);
        Assert.assertNotNull(booking);
        Assert.assertNotNull(booking.getBookingId());
        Assert.assertTrue(booking.getBookingId() > 0);
        Assert.assertEquals(car, booking.getBookedCar());
        Assert.assertEquals(fromDate, booking.getFromDate());
        Assert.assertEquals(toDate, booking.getToDate());
        Assert.assertEquals(CarUsage.DOMESTIC, booking.getUsage());
    }

    @Test
    public void testBookCarWithCarAlreadyBookedSamePeriod() {
        final Car car = selectOneExistingCar();
        final Date fromDate = DateUtils.addDays(new Date(), 3);
        final Date toDate = DateUtils.addDays(new Date(), 4);
        Booking booking;
        try {
            booking = carRentalService.bookCar(car.getVin(), fromDate, toDate, null);
        } catch (final CarRentalException e) {
            booking = null;
            Assert.fail();
        }
        Assert.assertNotNull(booking);
        Assert.assertNotNull(booking.getBookingId());
        Assert.assertTrue(booking.getBookingId() > 0);
        Assert.assertEquals(car, booking.getBookedCar());
        Assert.assertEquals(fromDate, booking.getFromDate());
        Assert.assertEquals(toDate, booking.getToDate());
        Assert.assertEquals(CarUsage.DOMESTIC, booking.getUsage());
        try {
            carRentalService.bookCar(car.getVin(), fromDate, toDate, null);
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_CAR_IS_BOOKED, e.getErrorCode());
        }
    }

    @Test
    public void testBookCarWithCarAlreadyBookedOnPeriodStart() {
        final Car car = selectOneExistingCar();
        final Date fromDate = DateUtils.addDays(new Date(), 10);
        final Date toDate = DateUtils.addDays(new Date(), 15);
        Booking booking;
        try {
            booking = carRentalService.bookCar(car.getVin(), fromDate, toDate, null);
        } catch (final CarRentalException e) {
            booking = null;
            Assert.fail();
        }
        Assert.assertNotNull(booking);
        Assert.assertNotNull(booking.getBookingId());
        Assert.assertTrue(booking.getBookingId() > 0);
        Assert.assertEquals(car, booking.getBookedCar());
        Assert.assertEquals(fromDate, booking.getFromDate());
        Assert.assertEquals(toDate, booking.getToDate());
        Assert.assertEquals(CarUsage.DOMESTIC, booking.getUsage());
        try {
            carRentalService.bookCar(car.getVin(), DateUtils.addDays(fromDate, -2),
                    DateUtils.addDays(fromDate, 2), null);
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_CAR_IS_BOOKED, e.getErrorCode());
        }
    }

    @Test
    public void testBookCarWithCarAlreadyBookedOnPeriodEnd() {
        final Car car = selectOneExistingCar();
        final Date fromDate = DateUtils.addDays(new Date(), 20);
        final Date toDate = DateUtils.addDays(new Date(), 25);
        Booking booking;
        try {
            booking = carRentalService.bookCar(car.getVin(), fromDate, toDate, null);
        } catch (final CarRentalException e) {
            booking = null;
            Assert.fail();
        }
        Assert.assertNotNull(booking);
        Assert.assertNotNull(booking.getBookingId());
        Assert.assertTrue(booking.getBookingId() > 0);
        Assert.assertEquals(car, booking.getBookedCar());
        Assert.assertEquals(fromDate, booking.getFromDate());
        Assert.assertEquals(toDate, booking.getToDate());
        Assert.assertEquals(CarUsage.DOMESTIC, booking.getUsage());
        try {
            carRentalService.bookCar(car.getVin(), DateUtils.addDays(toDate, -2),
                    DateUtils.addDays(toDate, 2), null);
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_CAR_IS_BOOKED, e.getErrorCode());
        }
    }

    @Test
    public void testBookCarWithCarAlreadyBookedWithinPeriod() {
        final Car car = selectOneExistingCar();
        final Date fromDate = DateUtils.addDays(new Date(), 30);
        final Date toDate = DateUtils.addDays(new Date(), 35);
        Booking booking;
        try {
            booking = carRentalService.bookCar(car.getVin(), fromDate, toDate, null);
        } catch (final CarRentalException e) {
            booking = null;
            Assert.fail();
        }
        Assert.assertNotNull(booking);
        Assert.assertNotNull(booking.getBookingId());
        Assert.assertTrue(booking.getBookingId() > 0);
        Assert.assertEquals(car, booking.getBookedCar());
        Assert.assertEquals(fromDate, booking.getFromDate());
        Assert.assertEquals(toDate, booking.getToDate());
        Assert.assertEquals(CarUsage.DOMESTIC, booking.getUsage());
        try {
            carRentalService.bookCar(car.getVin(), DateUtils.addDays(fromDate, 1),
                    DateUtils.addDays(toDate, -1), null);
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_CAR_IS_BOOKED, e.getErrorCode());
        }
    }

    @Test
    public void testBookCarWithForeignCountriesAllowed() {
        final Car car = selectOneExistingCar();
        final Date fromDate = DateUtils.addDays(new Date(), 40);
        final Date toDate = DateUtils.addDays(new Date(), 45);
        final Booking booking = carRentalService.bookCar(car.getVin(), fromDate, toDate,
                new String[] {"Italy", "Austria"});
        Assert.assertNotNull(booking);
        Assert.assertNotNull(booking.getBookingId());
        Assert.assertTrue(booking.getBookingId() > 0);
        Assert.assertEquals(car, booking.getBookedCar());
        Assert.assertEquals(fromDate, booking.getFromDate());
        Assert.assertEquals(toDate, booking.getToDate());
        Assert.assertEquals(CarUsage.FOREIGN, booking.getUsage());
    }

    @Test
    public void testBookCarWithForeignCountriesDenied() {
        final Car car = selectOneExistingCar();
        final Date fromDate = DateUtils.addDays(new Date(), 50);
        final Date toDate = DateUtils.addDays(new Date(), 55);
        try {
            carRentalService.bookCar(car.getVin(), fromDate, toDate, new String[] {"Russia"});
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_FORBIDDEN_FOREIGN_COUNTRY_USAGE,
                    e.getErrorCode());
        }
    }

    private Car selectOneExistingCar() {
        final List<Car> availableCars = carRentalService.findAvailableCars();
        Assert.assertNotNull(availableCars);
        Assert.assertFalse(availableCars.isEmpty());
        return availableCars.get(random.nextInt(availableCars.size()));
    }
}
