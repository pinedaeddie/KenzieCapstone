// File path: src/main/java/com/kenzie/capstone/service/AppointmentService.java

package com.kenzie.capstone.service;

import com.kenzie.capstone.service.model.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment scheduleAppointment(String userId, String appointmentDetails);
    List<Appointment> getAppointments();
    Appointment updateAppointment(String appointmentId, String newDetails);
    void deleteAppointment(String appointmentId);
}
