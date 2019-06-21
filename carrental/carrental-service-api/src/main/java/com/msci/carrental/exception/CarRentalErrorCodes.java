package com.msci.carrental.exception;

/**
 * Possible error codes of car rental service exceptions.
 *
 */
public final class CarRentalErrorCodes {
    /** Hidden ctor. */
    private CarRentalErrorCodes() {}

    public static final String ERROR_BOOKING_MISSING_VIN = "carRental.missingVin";
    public static final String ERROR_BOOKING_MISSING_FROM_DATE = "carRental.missingFromDate";
    public static final String ERROR_BOOKING_MISSING_TO_DATE = "carRental.missingToDate";
    public static final String ERROR_BOOKING_TO_DATE_BEFORE_FROM_DATE =
            "carRental.toDateBeforeFromDate";
    public static final String ERROR_BOOKING_INVALID_FROM_DATE = "carRental.invalidFromDate";
    public static final String ERROR_BOOKING_NONEXISTENT_CAR = "carRental.nonExistentCar";
    public static final String ERROR_BOOKING_CAR_IS_BOOKED = "carRental.carIsBooked";
    public static final String ERROR_BOOKING_FORBIDDEN_FOREIGN_COUNTRY_USAGE =
            "carRental.forbiddenForeignCountryUsage";

}
