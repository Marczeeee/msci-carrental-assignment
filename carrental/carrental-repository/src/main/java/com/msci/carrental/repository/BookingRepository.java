package com.msci.carrental.repository;

import com.msci.carrental.model.Booking;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Jpa repository for {@link Booking} entities.
 *
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Lists all VIN's of cars booked at the given date.
     *
     * @param date
     *            date to be checked for bookings
     * @return (potentially empty) {@link List} of car VIN numbers
     */
    @Query("SELECT b.bookedCar FROM Booking b WHERE b.fromDate<=:date AND b.toDate>=:date")
    List<String> findAllBookedCarsAtDate(@Param("date") Date date);

    /**
     * Finds all bookings of a car within a date period.
     *
     * @param vin
     *            car VIN value
     * @param fromDate
     *            date period opening date
     * @param toDate
     *            date period ending date
     * @return (potentially empty) {@link List} of bookings of a car
     */
    @Query("SELECT b FROM Booking b WHERE b.bookedCar.vin=:vin AND "
            + "((b.fromDate<=:fromDate AND b.toDate>:fromDate) OR "
            + "(b.fromDate<=:toDate AND b.toDate>:toDate) OR "
            + "(b.fromDate>=:fromDate AND b.toDate<=:toDate))")
    List<Booking> findBookingOfCarInDatePeriod(
            @Param("vin") String vin,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate);
}
