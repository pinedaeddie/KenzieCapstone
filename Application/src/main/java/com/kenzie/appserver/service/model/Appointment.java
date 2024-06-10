package com.kenzie.appserver.service.model;

public class Appointment {
    private final String id;
    private final String patientName;
    private final String providerName;
    private final String appointmentTime;

    public Appointment(String id, String patientName, String providerName, String appointmentTime) {
        this.id = id;
        this.patientName = patientName;
        this.providerName = providerName;
        this.appointmentTime = appointmentTime;
    }

    public String getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }
}
