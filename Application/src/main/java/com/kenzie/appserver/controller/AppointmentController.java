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
import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentCreateRequest appointmentCreateRequest) {

        try {
            AppointmentResponse response = appointmentService.createAppointment(appointmentCreateRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
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
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<AppointmentRecord>> getAllAppointments() {

        try {
            return ResponseEntity.ok(appointmentService.getAllAppointments());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentRecord> updateAppointmentById(@PathVariable("id") String id, @RequestBody AppointmentCreateRequest appointmentCreateRequest) {

        if (id == null || id.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID cannot be null");
        }

        if (appointmentCreateRequest.getPatientFirstName() == null || appointmentCreateRequest.getPatientFirstName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Patient Name");
        }

        try{
            AppointmentRecord updatedAppointment = appointmentService.updateAppointmentById(id, appointmentCreateRequest);
            return ResponseEntity.ok(updatedAppointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentRecord> deleteAppointmentById(@PathVariable("id") String id) {

        try {
            AppointmentRecord deletedRecord = appointmentService.deleteAppointmentById(id);
            return ResponseEntity.ok(deletedRecord);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}