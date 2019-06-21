package com.msci.carrental.test;

import com.msci.carrental.CarRentalService;
import com.msci.carrental.CarRentalServiceImpl;
import com.msci.carrental.exception.CarRentalErrorCodes;
import com.msci.carrental.exception.CarRentalException;
import com.msci.carrental.repository.BookingRepository;
import com.msci.carrental.repository.CarRepository;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Unit test class for {@link CarRentalService}.
 *
 */
public class CarRentalServiceTest {
    @InjectMocks
    private final CarRentalService carRentalService = new CarRentalServiceImpl();
    @Mock
    private CarRepository carRepository;
    @Mock
    private BookingRepository bookingRepository;

    private final Random random = new SecureRandom();

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAvailableCarsRepositoryInvocationsWithNoBookings() {
        Mockito.when(bookingRepository.findAllBookedCarsAtDate(Matchers.<Date> any())).thenReturn(
                Collections.EMPTY_LIST);

        carRentalService.findAvailableCars();
        Mockito.verify(carRepository).findAll();
    }

    @Test
    public void testFindAvailableCarsRepositoryInvocationsWithBookings() {
        Mockito.when(bookingRepository.findAllBookedCarsAtDate(Matchers.<Date> any())).thenReturn(
                Arrays.asList(Long.toString(random.nextLong())));

        carRentalService.findAvailableCars();
        Mockito.verify(carRepository).findAllNonListedCars(Matchers.anyList());
    }

    @Test
    public void testGetCarDetailsRepositoryInvocations() {
        final String vin = Long.toString(random.nextLong());
        carRentalService.getCarDetails(vin);
        Mockito.verify(carRepository).findOne(vin);
    }

    @Test
    public void testBookCarWithVinNull() {
        try {
            carRentalService.bookCar(null, new Date(), DateUtils.addDays(new Date(), 5), null);
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_MISSING_VIN, e.getErrorCode());
        }
    }

    @Test
    public void testBookCarWithVinEmpty() {
        try {
            carRentalService.bookCar("", new Date(), DateUtils.addDays(new Date(), 5), null);
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_MISSING_VIN, e.getErrorCode());
        }
    }

    @Test
    public void testBookCarWithNonExistingVin() {
        final String vin = Long.toString(random.nextLong());
        try {
            carRentalService.bookCar(vin, new Date(), DateUtils.addDays(new Date(), 5), null);
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_NONEXISTENT_CAR,
                    e.getErrorCode());
        }
        Mockito.verify(carRepository).findOne(vin);
    }

    @Test
    public void testBookCarWithFromDateNull() {
        try {
            carRentalService.bookCar(Long.toString(random.nextLong()), null,
                    DateUtils.addDays(new Date(), 5), null);
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_MISSING_FROM_DATE,
                    e.getErrorCode());
        }
    }

    @Test
    public void testBookCarWithToDateNull() {
        try {
            carRentalService.bookCar(Long.toString(random.nextLong()), new Date(), null, null);
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_MISSING_TO_DATE,
                    e.getErrorCode());
        }
    }

    @Test
    public void testBookCarWithToDateBeforeFromDate() {
        try {
            carRentalService.bookCar(Long.toString(random.nextLong()),
                    DateUtils.addDays(new Date(), 5), new Date(), null);
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_TO_DATE_BEFORE_FROM_DATE,
                    e.getErrorCode());
        }
    }

    @Test
    public void testBookCarWithFromDateBeforeToday() {
        try {
            carRentalService.bookCar(Long.toString(random.nextLong()),
                    DateUtils.addDays(new Date(), -5), DateUtils.addDays(new Date(), 5), null);
            Assert.fail();
        } catch (final CarRentalException e) {
            Assert.assertNotNull(e.getErrorCode());
            Assert.assertEquals(CarRentalErrorCodes.ERROR_BOOKING_INVALID_FROM_DATE,
                    e.getErrorCode());
        }
    }
}
