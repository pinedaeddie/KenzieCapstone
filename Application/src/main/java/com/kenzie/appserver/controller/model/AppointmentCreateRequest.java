package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AppointmentCreateRequest {

    @NotEmpty
    @JsonProperty("patientFirstName")
    private String patientFirstName;
    @NotEmpty
    @JsonProperty("patientLastName")
    private String patientLastName;
    @NotEmpty
    @JsonProperty("providerName")
    private String providerName;
    @NotNull
    @Future
    @JsonProperty("appointmentDateTime")
    private String appointmentDateTime;


    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(String appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }
}
