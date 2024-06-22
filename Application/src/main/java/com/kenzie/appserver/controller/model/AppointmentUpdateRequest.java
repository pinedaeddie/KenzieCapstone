package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;

public class AppointmentUpdateRequest {

    @NotEmpty
    @JsonProperty("appointmentId")
    private String appointmentId;

    @NotEmpty
    @JsonProperty("patientFirstName")
    private String patientFirstName;

    @NotEmpty
    @JsonProperty("providerName")
    private String providerName;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
