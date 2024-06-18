package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.AppointmentCreateRequest;
import com.kenzie.appserver.controller.model.AppointmentResponse;
import com.kenzie.appserver.repositories.AppointmentRepository;
import com.kenzie.appserver.repositories.model.AppointmentRecord;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.BookingData;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final LambdaServiceClient lambdaServiceClient;
    private final CacheStore cache;

    public AppointmentService(AppointmentRepository appointmentRepository, LambdaServiceClient lambdaServiceClient, CacheStore cache) {
        this.appointmentRepository = appointmentRepository;
        this.lambdaServiceClient = lambdaServiceClient;
        this.cache = cache;
    }

    public AppointmentResponse createAppointment(AppointmentCreateRequest appointmentCreateRequest) {

        if (appointmentCreateRequest == null) {
            throw new IllegalArgumentException("AppointmentCreateRequest cannot be null");
        }

        // Generating the appointmentId
        String appointmentId = UUID.randomUUID().toString();

        // Creating the AppointmentRecord and Set the appointmentId
        AppointmentRecord record = fromRequestToRecord(appointmentCreateRequest);
        record.setAppointmentId(appointmentId);

        // Saving the record to the repository
        appointmentRepository.save(record);

        // Notifying the Lambda service about the new appointment
        lambdaServiceClient.createBooking(fromRecordToBookingData(record));

        return fromRecordToResponse(record);
    }

    public Optional<AppointmentRecord> getAppointmentById(String appointmentId) {

        // Checking if ID is null
        if (appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }

        AppointmentRecord cachedRecord = cache.get(appointmentId);
        if (cachedRecord != null) {
            return Optional.of(cachedRecord);
        }

        AppointmentRecord recordFromBackendService = appointmentRepository
                .findById(appointmentId)
                .orElse(null);

        if (recordFromBackendService != null) {
            cache.add(recordFromBackendService.getAppointmentId(), recordFromBackendService);
        }

        // Notifying the Lambda service about the appointment
        lambdaServiceClient.getBooking(appointmentId);
        return Optional.ofNullable(recordFromBackendService);
    }

    public Iterable<AppointmentRecord> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void deleteAppointmentById(String id) {

        // Checking if ID is null
        if (id == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }
        appointmentRepository.deleteById(id);
        cache.evict(id);
        // Notifying the Lambda service about the appointment deletion
        lambdaServiceClient.deleteBooking(id);
    }

    public AppointmentRecord updateAppointmentById(String appointmentId, AppointmentCreateRequest appointmentCreateRequest) {

        // Checking if ID is null
        if (appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }

        Optional<AppointmentRecord> optionalRecord = appointmentRepository.findById(appointmentId);

        if (optionalRecord.isPresent()) {
            AppointmentRecord record = optionalRecord.get();
            record.setPatientFirstName(appointmentCreateRequest.getPatientFirstName());
            record.setPatientLastName(appointmentCreateRequest.getPatientLastName());
            record.setProviderName(appointmentCreateRequest.getProviderName());
            record.setGender(appointmentCreateRequest.getGender());
            record.setAppointmentDate(appointmentCreateRequest.getAppointmentDate());
            record.setAppointmentTime(appointmentCreateRequest.getAppointmentTime());

            AppointmentRecord updatedRecord = appointmentRepository.save(record);
            cache.evict(appointmentId);
            cache.add(record.getAppointmentId(), record);

            // Notifying the Lambda service about the appointment update
            lambdaServiceClient.updateBooking(fromRecordToBookingData(updatedRecord));

            return updatedRecord;
        } else {
            throw new IllegalArgumentException( "Appointment not found with id: " + appointmentId);
        }
    }

    private AppointmentRecord fromRequestToRecord(AppointmentCreateRequest appointmentCreateRequest) {

        AppointmentRecord record = new AppointmentRecord();
        record.setPatientFirstName(appointmentCreateRequest.getPatientFirstName());
        record.setPatientLastName(appointmentCreateRequest.getPatientLastName());
        record.setProviderName(appointmentCreateRequest.getProviderName());
        record.setGender(appointmentCreateRequest.getGender());
        record.setAppointmentDate(appointmentCreateRequest.getAppointmentDate());
        record.setAppointmentTime(appointmentCreateRequest.getAppointmentTime());
        return record;
    }

    private AppointmentResponse fromRecordToResponse(AppointmentRecord record) {

        AppointmentResponse response = new AppointmentResponse();
        response.setAppointmentId(record.getAppointmentId());
        response.setPatientFirstName(record.getPatientFirstName());
        response.setPatientLastName(record.getPatientLastName());
        response.setProviderName(record.getProviderName());
        response.setGender(record.getGender());
        response.setAppointmentDate(record.getAppointmentDate());
        response.setAppointmentTime(record.getAppointmentTime());
        return response;
    }

    private BookingData fromRecordToBookingData(AppointmentRecord record) {

        BookingData bookingData = new BookingData();
        bookingData.setId(record.getAppointmentId());
        bookingData.setPatientName(record.getPatientFirstName() + " " + record.getPatientLastName());
        bookingData.setProviderName(record.getProviderName());
        bookingData.setGender(record.getGender());
        bookingData.setAppointmentDate(record.getAppointmentDate());
        bookingData.setAppointmentTime(record.getAppointmentTime());
        return bookingData;
    }
}
