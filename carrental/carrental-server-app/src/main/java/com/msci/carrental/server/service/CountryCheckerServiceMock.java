package com.msci.carrental.server.service;

import com.msci.carrental.CountryCheckerService;

import java.security.SecureRandom;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Mock {@link CountryCheckerService} implementation with just pausing the checker processes for
 * some time and returning the result of a check. The result is based on the number of countries
 * received.
 *
 */
@Component
public class CountryCheckerServiceMock implements CountryCheckerService {
    /** {@link Logger} instance. */
    private final Logger logger = LoggerFactory.getLogger(CountryCheckerService.class);
    /** Random value generator instance for generating thread pause values. */
    private final Random random = new SecureRandom();
    /** Minimal pausing time in milliseconds. */
    private static final int MIN_PAUSE_TIME_MS = 1000;
    /** Maximal pausing time in milliseconds. */
    private static final int MAX_PAUSE_TIME_LIMIT_MS = 5000;

    public boolean isCountriesAllowedForCar(final String vin, final String[] countries) {
        logger.info("Checking if given countries ({}) are allowed for car (VIN={})", countries,
                vin);
        int pauseTime = random.nextInt(MAX_PAUSE_TIME_LIMIT_MS);
        if (pauseTime < MIN_PAUSE_TIME_MS) {
            pauseTime = MIN_PAUSE_TIME_MS;
        }
        boolean isCarAllowed = false;
        if (countries == null || countries.length == 0) {
            isCarAllowed = true;
        } else {
            isCarAllowed = countries.length % 2 == 0;
        }
        try {
            Thread.sleep(pauseTime);
        } catch (final InterruptedException e) {
            logger.warn("Failed to pause checker process for {} ms due to an error: {}", pauseTime,
                    e.getMessage());
        }
        logger.info("Car (VIN={}) is{} allowed to be used in countries: {}", vin,
                isCarAllowed ? "" : " not", countries);
        return isCarAllowed;
    }
}
