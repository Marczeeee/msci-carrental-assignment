package com.msci.carrental.rest;

import com.msci.carrental.model.Booking;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Result data class of a booking process.
 *
 */
public class BookingResult {
    /** Details of a successful booking. */
    private Booking booking;
    /** Result of a booking process. */
    private BookingProcessResult bookingProcessResult;
    /** Error code for failed booking. */
    private String errorCode;
    /** Error message of a failed booking process. */
    private String errorMessage;

    /** Ctor. */
    public BookingResult() {
        super();
    }

    /**
     * Ctor.
     *
     * @param booking
     *            booking object
     * @param bookingProcessResult
     *            booking result value
     */
    public BookingResult(final Booking booking, final BookingProcessResult bookingProcessResult) {
        this(booking, bookingProcessResult, null, null);
    }

    /**
     * Ctor.
     *
     * @param errorCode
     *            error code value
     * @param errorMessage
     *            error message value
     */
    public BookingResult(final String errorCode, final String errorMessage) {
        this(null, BookingProcessResult.FAILURE, errorCode, errorMessage);
    }

    /**
     * Ctor.
     * 
     * @param booking
     *            booking object
     * @param bookingProcessResult
     *            booking result value
     * @param errorCode
     *            error code value
     * @param errorMessage
     *            error message value
     */
    public BookingResult(
            final Booking booking,
            final BookingProcessResult bookingProcessResult,
            final String errorCode,
            final String errorMessage) {
        super();
        this.booking = booking;
        this.bookingProcessResult = bookingProcessResult;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(final Booking booking) {
        this.booking = booking;
    }

    public BookingProcessResult getBookingProcessResult() {
        return bookingProcessResult;
    }

    public void setBookingProcessResult(final BookingProcessResult bookingProcessResult) {
        this.bookingProcessResult = bookingProcessResult;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static enum BookingProcessResult {
        SUCCESS, FAILURE
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
