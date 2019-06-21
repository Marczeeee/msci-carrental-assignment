package com.msci.carrental.repository;

import com.msci.carrental.model.Car;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Jpa repository for {@link Car} entities.
 *
 */
@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    /**
     * Finds all cars which VIN value isn't in the given {@link List}.
     *
     * @param carVins
     *            non-null, non-empty list of excluded car VIN values.
     * @return (potentially empty) {@link List} of car VIN values
     */
    @Query(value = "SELECT c FROM Car c WHERE c.vin NOT IN :vinList")
    List<Car> findAllNonListedCars(@Param("vinList") List<String> carVins);
}
