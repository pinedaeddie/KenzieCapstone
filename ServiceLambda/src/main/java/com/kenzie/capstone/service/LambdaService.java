package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.BookingDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.BookingData;
import com.kenzie.capstone.service.model.BookingRecord;
import javax.inject.Inject;
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
        bookingDao.storeBookingData(bookingRecord);
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

        return bookingDao.updateBookingData(bookingRecord);
    }

    public boolean deleteBookings(String bookingId) {

        // Checking if ID is null or empty
        if (bookingId == null || bookingId.isEmpty()) {
            throw new InvalidDataException("Request must contain a valid list of Booking IDs");
        }

        return bookingDao.deleteBookingById(bookingId);
    }
}