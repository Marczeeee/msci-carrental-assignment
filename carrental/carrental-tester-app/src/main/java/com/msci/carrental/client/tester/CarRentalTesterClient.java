package com.msci.carrental.client.tester;

import com.msci.carrental.model.Car;
import com.msci.carrental.rest.BookingReservationDetails;
import com.msci.carrental.rest.BookingResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Main class for simple command line tester application for Car Rental service.
 *
 */
@SpringBootApplication
public class CarRentalTesterClient {
    /** {@link Logger} instance. */
    private final Logger logger = LoggerFactory.getLogger(CarRentalTesterClient.class);
    /** Thread pool for running booking queries to CarRental service. */
    private ExecutorService executor;
    /** Default number of parallel threads processing booking requests. */
    private static final int DEFAULT_THREAD_NR = 30;
    /** Default number of booking requests to be sent to server. */
    private static final int DEFAULT_REQUEST_NR = 100;
    /** Default host and port of the booking server. */
    private static final String DEFAULT_CALLING_HOST = "127.0.0.1:8080";

    /**
     * Main method.
     *
     * @param args
     *            command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(CarRentalTesterClient.class, args);
    }

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public ApplicationRunner run(final RestTemplate restTemplate) throws Exception {
        return new ApplicationRunner() {
            @Override
            public void run(final ApplicationArguments args) throws Exception {
                int threadNr = DEFAULT_THREAD_NR;
                int requestNr = DEFAULT_REQUEST_NR;
                String host = DEFAULT_CALLING_HOST;
                for (final String arg : args.getOptionNames()) {
                    logger.info("Processing application argument: " + arg);
                    if (arg.equals("threadNr")) {
                        threadNr = Integer.parseUnsignedInt(args.getOptionValues(arg).get(0));
                    } else if (arg.equals("requestNr")) {
                        requestNr = Integer.parseUnsignedInt(args.getOptionValues(arg).get(0));
                    } else if (arg.equals("serviceHost")) {
                        host = args.getOptionValues(arg).get(0);
                    }
                }

                executor = Executors.newFixedThreadPool(threadNr);
                final String callingHost = "http://" + host;
                final List<Future<?>> futures = new ArrayList<>(requestNr);
                for (int i = 0; i < requestNr; i++) {
                    futures.add(executor.submit(new CarBookingTask(callingHost, restTemplate)));
                }
                for (final Future<?> future : futures) {
                    if (!future.isDone() && !future.isCancelled()) {
                        future.get();
                    }
                }
                System.exit(0);
            }
        };
    }

    /**
     * {@link Runnable} implementation performing a (random) car booking request.
     *
     */
    private class CarBookingTask implements Runnable {
        /** Host where the car rental server is available. */
        private final String callingHost;
        /** Spring's {@link RestTemplate} for calling RESTFul car rental service. */
        private final RestTemplate restTemplate;

        /**
         * Ctor.
         *
         * @param callingHost
         *            calling host value
         * @param restTemplate
         *            rest template object
         */
        public CarBookingTask(final String callingHost, final RestTemplate restTemplate) {
            super();
            this.callingHost = callingHost;
            this.restTemplate = restTemplate;
        }

        @Override
        public void run() {
            // Load all available cars
            final ParameterizedTypeReference<List<Car>> typeRef =
                    new ParameterizedTypeReference<List<Car>>() {};
            final HttpEntity<?> requestEntity = new HttpEntity<Object>(null);
            final ResponseEntity<List<Car>> responseEntity = restTemplate.exchange(
                    callingHost + "/availableCars", HttpMethod.GET, requestEntity, typeRef);
            final List<Car> availableCars = responseEntity.getBody();

            final Random random = ThreadLocalRandom.current();

            // Randomly select an available car
            final int carNr = random.nextInt(availableCars.size());
            final Car car2Book = restTemplate.getForObject(
                    callingHost + "/carDetails/" + availableCars.get(carNr).getVin(), Car.class);
            logger.debug("Selected car for booking: {}", car2Book);

            // Create a random date range for booking
            final Date today = new Date();
            final int daysToAddForFromDate = random.nextInt(730);
            final int daysToAddForToDate = random.nextInt(10);
            // Create booking data object
            final BookingReservationDetails bookingReservationDetails =
                    new BookingReservationDetails();
            bookingReservationDetails.setVin(car2Book.getVin());
            final Date fromDate = DateUtils.addDays(today, daysToAddForFromDate);
            bookingReservationDetails.setFromDate(fromDate);
            bookingReservationDetails.setToDate(DateUtils.addDays(fromDate, daysToAddForToDate));
            // Create foreign countries array
            final int foreignCountriesArraySize = random.nextInt(10);
            final String[] foreignCountries =
                    createForeignCountriesArray(foreignCountriesArraySize);
            bookingReservationDetails.setForeignCountries(foreignCountries);

            // Send booking to server
            logger.debug("Sending booking request to server: {}", bookingReservationDetails);
            final BookingResult bookingResult = restTemplate.postForObject(callingHost + "/bookCar",
                    bookingReservationDetails, BookingResult.class);
            logger.info("Received booking result: {}", bookingResult);
        }

        /**
         * Generate an array of foreign country names. The country names aren't real names but
         * randomly generated numbers converted to strings.
         *
         * @param arraySize
         *            size of the array to be generated
         * @return array with generated country names, or <code>null</code> if the requests array
         *         size is 0
         */
        private String[] createForeignCountriesArray(final int arraySize) {
            if (arraySize == 0) {
                return null;
            }
            final String[] foreignCountriesArray = new String[arraySize];
            for (int i = 0; i < arraySize; i++) {
                foreignCountriesArray[i] = Long.toString(ThreadLocalRandom.current().nextLong());
            }
            return foreignCountriesArray;
        }
    }
}
