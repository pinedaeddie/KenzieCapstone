package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.BookingDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.BookingData;
import com.kenzie.capstone.service.model.BookingRecord;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class LambdaService {

    private final BookingDao bookingDao;
    private final ExecutorService executor;

    @Inject
    public LambdaService(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
        this.executor = Executors.newCachedThreadPool();
    }
    public LambdaService(BookingDao bookingDao, ExecutorService executor) {
        this.bookingDao = bookingDao;
        this.executor = executor;
    }

    public BookingRecord getBookingData(String id) {

        // Checking if ID is null or empty
        if (id == null || id.isEmpty()) {
            throw new InvalidDataException("Booking ID must be provided");
        }

        return bookingDao.getBookingById(id);
    }

    public void saveBooking(BookingData bookingData) {

        // Checking if ID is null or empty
        String bookingId = bookingData.getId();
        if (bookingId == null || bookingId.isEmpty()) {
            throw new InvalidDataException("Booking ID must be provided");
        }

        BookingRecord bookingRecord = new BookingRecord();
        bookingRecord.setId(bookingData.getId());
        bookingRecord.setPatientName(bookingData.getPatientName());
        bookingRecord.setProviderName(bookingData.getProviderName());

        executor.submit(() -> {
            try {
                bookingDao.storeBookingData(bookingRecord);
            } catch (Exception e) {
                throw new RuntimeException("Failed to store booking data", e);
            }
        });
    }

    public BookingRecord updateBooking(String id, BookingData bookingData) {

        // Checking if ID is null or empty
        if (id == null || id.isEmpty()) {
            throw new InvalidDataException("Request must contain a valid Customer ID");
        }
        BookingRecord bookingRecord = new BookingRecord();
        bookingRecord.setId(id);
        bookingRecord.setPatientName(bookingData.getPatientName());
        bookingRecord.setProviderName(bookingData.getProviderName());

        try {
            return executor.submit(() -> bookingDao.updateBookingData(bookingRecord)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update booking", e);
        }
    }

    public boolean deleteBookings(List<String> bookingIds) {
        boolean allDeleted = true;

        // Checking if ID is null or empty
        if (bookingIds == null || bookingIds.isEmpty()) {
            throw new InvalidDataException("Request must contain a valid list of Booking IDs");
        }

        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (String bookingId : bookingIds) {
            if (bookingId == null || bookingId.isEmpty()) {
                throw new InvalidDataException("Booking ID cannot be null or empty");
            }

            tasks.add(() -> bookingDao.deleteBookingById(bookingId));
        }

        ExecutorService executor = Executors.newFixedThreadPool(bookingIds.size());

        try {
            List<Future<Boolean>> results = executor.invokeAll(tasks);

            for (Future<Boolean> result : results) {
                if (!result.get()) {
                    allDeleted = false;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error deleting bookings", e);
        } finally {
            executor.shutdown();
        }

        return allDeleted;
    }
}