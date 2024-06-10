package com.kenzie.appserver.service;



import com.kenzie.appserver.controller.model.AppointmentCreateRequest;
import com.kenzie.appserver.controller.model.AppointmentResponse;
import com.kenzie.appserver.repositories.AppointmentRepository;
import com.kenzie.appserver.repositories.model.AppointmentRecord;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final LambdaServiceClient lambdaServiceClient;

    @Autowired
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

        // Creating the response using the same appointmentId
        AppointmentResponse response = new AppointmentResponse();
        response.setAppointmentId(appointmentId);
        response.setPatientFirstName(appointmentCreateRequest.getPatientFirstName());
        response.setPatientLastName(appointmentCreateRequest.getPatientLastName());
        response.setProviderName(appointmentCreateRequest.getProviderName());
        response.setAppointmentDate(appointmentCreateRequest.getAppointmentDate());
        response.setAppointmentTime(appointmentCreateRequest.getAppointmentTime());

        return response;
    }

    public Optional<AppointmentRecord> getAppointmentById(String appointmentId) {

        if (appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }
        return appointmentRepository.findById(appointmentId);
    }

    public Iterable<AppointmentRecord> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void deleteAppointmentById(String id) {

        if (id == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }
        appointmentRepository.deleteById(id);
    }

    public AppointmentRecord updateAppointment(String appointmentId, AppointmentCreateRequest appointmentCreateRequest) {

        if (appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }

        Optional<AppointmentRecord> optionalRecord = appointmentRepository.findById(appointmentId);

        if (optionalRecord.isPresent()) {
            AppointmentRecord record = optionalRecord.get();
            record.setPatientFirstName(appointmentCreateRequest.getPatientFirstName());
            record.setPatientLastName(appointmentCreateRequest.getPatientLastName());
            record.setProviderName(appointmentCreateRequest.getProviderName());
            record.setAppointmentDate(appointmentCreateRequest.getAppointmentDate());
            record.setAppointmentTime(appointmentCreateRequest.getAppointmentTime());
            return appointmentRepository.save(record);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found with id: " + appointmentId);
        }
    }


    private AppointmentRecord fromRequestToRecord (AppointmentCreateRequest appointmentCreateRequest){

        if (appointmentCreateRequest == null) {
            throw new IllegalArgumentException("AppointmentCreateRequest cannot be null");
        }

        AppointmentRecord record = new AppointmentRecord();
        record.setPatientFirstName(appointmentCreateRequest.getPatientFirstName());
        record.setPatientLastName(appointmentCreateRequest.getPatientLastName());
        record.setProviderName(appointmentCreateRequest.getProviderName());
        record.setAppointmentDate(appointmentCreateRequest.getAppointmentDate());
        record.setAppointmentTime(appointmentCreateRequest.getAppointmentTime());
        return record;
    }
}
