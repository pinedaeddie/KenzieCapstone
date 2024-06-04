package com.kenzie.appserver.service;



import com.kenzie.appserver.repositories.AppointmentRepository;
import com.kenzie.appserver.repositories.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Optional<Appointment> getAppointmentById(String id) {
        return appointmentRepository.findById(id);
    }

    public Iterable<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void deleteAppointmentById(String id) {
        appointmentRepository.deleteById(id);
    }

    public Appointment updateAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
}
