package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.BookingDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.BookingData;
import com.kenzie.capstone.service.model.BookingRecord;
import java.time.LocalDateTime;
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

        if (id == null || id.isEmpty()) {
            throw new InvalidDataException("Booking ID must be provided");
        }

        return bookingDao.getBookingById(id);
    }

    public void saveBooking(BookingData bookingData) {

        String bookingId = bookingData.getId();

        if (bookingId == null || bookingId.isEmpty()) {
            throw new InvalidDataException("Booking ID must be provided");
        }
        if (bookingData.getPatientName() == null) {
            throw new InvalidDataException("Patient name must be provided");
        }
        if (bookingData.getProviderName() == null || bookingData.getProviderName().isEmpty()) {
            throw new InvalidDataException("Provider name must be provided");
        }
        if (bookingData.getGender() == null || bookingData.getGender().isEmpty()) {
            throw new InvalidDataException("Gender must be provided");
        }

        BookingRecord bookingRecord = new BookingRecord();
        bookingRecord.setId(bookingData.getId());
        bookingRecord.setBookingId(bookingData.getBookingId());
        bookingRecord.setPatientName(bookingData.getPatientName() + " " + bookingData.getPatientLastName());
        bookingRecord.setProviderName(bookingData.getProviderName());
        bookingRecord.setGender(bookingData.getGender());
        bookingRecord.setReminderSent(false);
        bookingRecord.setCreatedAt(LocalDateTime.now());
        bookingRecord.setUpdatedAt(LocalDateTime.now());

        bookingDao.createBookingData(bookingRecord);
    }

    public BookingData updateBooking(String id, BookingData bookingData) {

        if (id == null || id.isEmpty()) {
            throw new InvalidDataException("Request must contain a valid Customer ID");
        }

        if (bookingData.getPatientName() == null || bookingData.getPatientName().isEmpty()
                || bookingData.getProviderName() == null || bookingData.getProviderName().isEmpty()) {
            throw new InvalidDataException("Booking data must contain patient name and provider name");
        }

        BookingRecord bookingRecord = bookingDao.getBookingById(id);

        if (bookingRecord == null) {
            throw new InvalidDataException("Booking ID does not exist");
        }

        bookingRecord.setId(id);
        bookingRecord.setBookingId(bookingData.getBookingId());
        bookingRecord.setPatientName(bookingData.getPatientName() + " " + bookingData.getPatientLastName());
        bookingRecord.setProviderName(bookingData.getProviderName());
        bookingRecord.setReminderSent(bookingData.isReminderSent());
        bookingRecord.setCreatedAt(LocalDateTime.now());
        bookingRecord.setUpdatedAt(LocalDateTime.now());
        bookingRecord.setGender(bookingData.getGender());

        BookingRecord updatedBookingRecord = bookingDao.updateBookingData(bookingRecord);

        BookingData updatedBookingData = new BookingData();
        updatedBookingData.setId(updatedBookingRecord.getId());
        updatedBookingData.setBookingId(updatedBookingRecord.getBookingId());
        updatedBookingData.setPatientName(updatedBookingRecord.getPatientName());
        updatedBookingData.setPatientLastName(updatedBookingRecord.getPatientName());
        updatedBookingData.setProviderName(updatedBookingRecord.getProviderName());
        updatedBookingData.setGender(updatedBookingRecord.getGender());
        updatedBookingData.setReminderSent(updatedBookingRecord.isReminderSent());

        return updatedBookingData;
    }

    public boolean deleteBookings(String bookingId) {

        if (bookingId == null || bookingId.isEmpty()) {
            throw new InvalidDataException("Request must contain a valid Booking ID");
        }
        return bookingDao.deleteBookingById(bookingId);
    }
}