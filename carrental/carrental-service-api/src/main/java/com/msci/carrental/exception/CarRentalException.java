package com.msci.carrental.exception;

/**
 * Exception class for car rental service errors. Contains a textual error code value to identify
 * the error uniquely.
 *
 */
public class CarRentalException extends RuntimeException {
    /** Textual error code value. */
    private String errorCode;

    /** Ctor. */
    public CarRentalException() {
        super();
    }

    /**
     * Ctor.
     * 
     * @param errorCode
     *            error code value
     * @param errorMessage
     *            exception message value
     */
    public CarRentalException(final String errorCode, final String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
