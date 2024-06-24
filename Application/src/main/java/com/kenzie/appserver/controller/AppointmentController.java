package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.AppointmentCreateRequest;
import com.kenzie.appserver.controller.model.AppointmentResponse;
import com.kenzie.appserver.repositories.model.AppointmentRecord;
import com.kenzie.appserver.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody AppointmentCreateRequest appointmentCreateRequest) {

        try {
            AppointmentResponse response = appointmentService.createAppointment(appointmentCreateRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable("id") String id) {

        try {
            AppointmentResponse response = appointmentService.getAppointmentById(id);
            if (response == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<AppointmentRecord>> getAllAppointments() {

        try {
            return ResponseEntity.ok(appointmentService.getAllAppointments());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve all appointments");
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<AppointmentRecord> updateAppointmentById(@PathVariable("id") String id, @RequestBody AppointmentCreateRequest appointmentCreateRequest) {

        if (appointmentCreateRequest.getPatientFirstName() == null || appointmentCreateRequest.getPatientFirstName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Patient Name");
        }

        AppointmentRecord updatedAppointment = appointmentService.updateAppointmentById(id, appointmentCreateRequest);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentRecord> deleteAppointmentById(@PathVariable("id") String id) {

        try {
            AppointmentRecord deletedRecord = appointmentService.deleteAppointmentById(id);
            return ResponseEntity.ok(deletedRecord);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}