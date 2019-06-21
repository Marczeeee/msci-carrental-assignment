package com.msci.carrental.model;

import com.msci.carrental.type.CarUsage;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Database entity holding car rental booking data.
 *
 */
@Entity
@Table(name = "BOOKING")
public class Booking {
    /** Generated unique booking ID value. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bookingId;
    /** Start date of booking. */
    @Column(name = "FROM_DATE")
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    /** Ending date of booking. */
    @Column(name = "TO_DATE")
    @Temporal(TemporalType.DATE)
    private Date toDate;
    /** Type of car usage. */
    @Column(name = "USAGE_TYPE")
    @Enumerated(EnumType.STRING)
    private CarUsage usage;
    /** The car that was booked. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "BOOKED_CAR", nullable = false)
    private Car bookedCar;

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(final long bookingId) {
        this.bookingId = bookingId;
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

    public CarUsage getUsage() {
        return usage;
    }

    public void setUsage(final CarUsage usage) {
        this.usage = usage;
    }

    public Car getBookedCar() {
        return bookedCar;
    }

    public void setBookedCar(final Car bookedCar) {
        this.bookedCar = bookedCar;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
