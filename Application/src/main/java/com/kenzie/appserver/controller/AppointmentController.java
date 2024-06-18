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
    public ResponseEntity<AppointmentRecord> getAppointmentById(@PathVariable("id") String id) {

        try {
            return appointmentService.getAppointmentById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<AppointmentRecord>> getAllAppointments() {

        try {
            return ResponseEntity.ok(appointmentService.getAllAppointments());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentRecord> deleteAppointmentById(@PathVariable("id") String id) {

        try {
            appointmentService.deleteAppointmentById(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<AppointmentRecord> updateAppointmentById(@PathVariable("id") String id, @RequestBody AppointmentCreateRequest appointmentCreateRequest) {

        if (appointmentCreateRequest.getPatientFirstName() == null || appointmentCreateRequest.getPatientFirstName().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Patient Name");
        }

        try {
            AppointmentRecord record = appointmentService.updateAppointmentById(id, appointmentCreateRequest);
            return ResponseEntity.created(URI.create("/appointments/" + record.getAppointmentId())).body(record);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
