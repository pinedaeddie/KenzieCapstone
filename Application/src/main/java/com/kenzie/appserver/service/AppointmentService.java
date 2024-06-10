package com.kenzie.appserver.service;



import com.kenzie.appserver.controller.model.AppointmentCreateRequest;
import com.kenzie.appserver.controller.model.AppointmentResponse;
import com.kenzie.appserver.repositories.AppointmentRepository;
import com.kenzie.appserver.repositories.model.AppointmentRecord;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

        AppointmentResponse response = new AppointmentResponse();
        response.setAppointmentId(UUID.randomUUID().toString());
        response.setPatientFirstName(appointmentCreateRequest.getPatientFirstName());
        response.setPatientLastName(appointmentCreateRequest.getPatientLastName());
        response.setProviderName(appointmentCreateRequest.getProviderName());
        response.setAppointmentDate(appointmentCreateRequest.getAppointmentDateTime());

        appointmentRepository.save(requestToRecord(appointmentCreateRequest));
        return response;
    }

    public Optional<AppointmentRecord> getAppointmentById(String id) {
        return appointmentRepository.findById(id);
    }

    public Iterable<AppointmentRecord> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void deleteAppointmentById(String id) {
        appointmentRepository.deleteById(id);
    }

    public AppointmentRecord updateAppointment(AppointmentRecord appointmentRecord) {
        return appointmentRepository.save(appointmentRecord);
    }


    private AppointmentRecord requestToRecord (AppointmentCreateRequest appointmentCreateRequest){

        AppointmentRecord record = new AppointmentRecord();
        record.setAppointmentId(UUID.randomUUID().toString());
        record.setPatientFirstName(appointmentCreateRequest.getPatientFirstName());
        record.setPatientLastName(appointmentCreateRequest.getPatientLastName());
        record.setProviderName(appointmentCreateRequest.getProviderName());
        record.setAppointmentDate(appointmentCreateRequest.getAppointmentDateTime());
        return record;
    }
}
