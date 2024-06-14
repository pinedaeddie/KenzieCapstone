package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.AppointmentCreateRequest;
import com.kenzie.appserver.controller.model.AppointmentResponse;
import com.kenzie.appserver.repositories.AppointmentRepository;
import com.kenzie.appserver.repositories.model.AppointmentRecord;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.BookingData;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final LambdaServiceClient lambdaServiceClient;

    public AppointmentService(AppointmentRepository appointmentRepository, LambdaServiceClient lambdaServiceClient) {
        this.appointmentRepository = appointmentRepository;
        this.lambdaServiceClient = lambdaServiceClient;
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
        lambdaServiceClient.scheduleBooking(fromRecordToBookingData(record));

        // Creating the response using the same appointmentId
        AppointmentResponse response = new AppointmentResponse();
        response.setAppointmentId(appointmentId);
        response.setPatientFirstName(appointmentCreateRequest.getPatientFirstName());
        response.setPatientLastName(appointmentCreateRequest.getPatientLastName());
        response.setProviderName(appointmentCreateRequest.getProviderName());
        response.setGender(appointmentCreateRequest.getGender());
        response.setAppointmentDate(appointmentCreateRequest.getAppointmentDate());
        response.setAppointmentTime(appointmentCreateRequest.getAppointmentTime());

        return response;
    }

    public Optional<AppointmentRecord> getAppointmentById(String appointmentId) {

        // Checking if ID is null
        if (appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }
        return appointmentRepository.findById(appointmentId);
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

        // Notifying the Lambda service about the appointment deletion
        lambdaServiceClient.deleteBooking(id);
    }

    public AppointmentRecord updateAppointment(String appointmentId, AppointmentCreateRequest appointmentCreateRequest) {

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

            // Notifying the Lambda service about the appointment update
            lambdaServiceClient.updateBooking(fromRecordToBookingData(updatedRecord));

            return updatedRecord;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found with id: " + appointmentId);
        }
    }

    private AppointmentRecord fromRequestToRecord(AppointmentCreateRequest appointmentCreateRequest) {

        if (appointmentCreateRequest == null) {
            throw new IllegalArgumentException("AppointmentCreateRequest cannot be null");
        }

        AppointmentRecord record = new AppointmentRecord();
        record.setPatientFirstName(appointmentCreateRequest.getPatientFirstName());
        record.setPatientLastName(appointmentCreateRequest.getPatientLastName());
        record.setProviderName(appointmentCreateRequest.getProviderName());
        record.setGender(appointmentCreateRequest.getGender());
        record.setAppointmentDate(appointmentCreateRequest.getAppointmentDate());
        record.setAppointmentTime(appointmentCreateRequest.getAppointmentTime());
        return record;
    }

    private BookingData fromRecordToBookingData(AppointmentRecord record) {

        if (record == null) {
            throw new IllegalArgumentException("Record cannot be null");
        }

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
