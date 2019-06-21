package com.msci.carrental.rest;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Data object holding data of a car booking reservation.
 *
 */
public class BookingReservationDetails implements Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 1328231324498088372L;
    /** Car VIN number. */
    private String vin;
    /** Booking starting date. */
    private Date fromDate;
    /** Booking ending day date. */
    private Date toDate;
    /** List of (foreign) countries the car will be used in. */
    private String[] foreignCountries;

    public String getVin() {
        return vin;
    }

    public void setVin(final String vin) {
        this.vin = vin;
    }

    public Date getFromDate() {
        return fromDate != null ? new Date(fromDate.getTime()) : null;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate != null ? new Date(fromDate.getTime()) : null;
    }

    public Date getToDate() {
        return toDate != null ? new Date(toDate.getTime()) : null;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate != null ? new Date(toDate.getTime()) : null;
    }

    public String[] getForeignCountries() {
        return foreignCountries;
    }

    public void setForeignCountries(final String[] foreignCountries) {
        this.foreignCountries = foreignCountries;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
