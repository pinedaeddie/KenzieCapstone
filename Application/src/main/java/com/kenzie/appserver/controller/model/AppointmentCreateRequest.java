package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @NotEmpty
    @JsonProperty("gender")
    private String gender;

    @NotNull
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("appointmentDate")
    private String appointmentDate;

    @NotNull
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @JsonProperty("appointmentTime")
    private String appointmentTime;


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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
