package com.kenzie.capstone.service.model;

import java.util.Objects;

public class BookingData {

    private String id;
    private String patientName;
    private String providerName;
    private String gender;
    private String appointmentDate;
    private String appointmentTime;

    public BookingData() {
    }

    public BookingData(String id, String patientName, String providerName, String gender, String appointmentDate, String appointmentTime) {
        this.id = id;
        this.patientName = patientName;
        this.providerName = providerName;
        this.gender = gender;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingData)) return false;
        BookingData that = (BookingData) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(patientName, that.patientName) &&
                Objects.equals(providerName, that.providerName) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(appointmentDate, that.appointmentDate) &&
                Objects.equals(appointmentTime, that.appointmentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientName, providerName, gender, appointmentDate, appointmentTime);
    }

    @Override
    public String toString() {
        return "BookingData{" +
                "id='" + id + '\'' +
                ", patientName='" + patientName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", gender='" + gender + '\'' +
                ", appointmentDate='" + appointmentDate + '\'' +
                ", appointmentTime='" + appointmentTime + '\'' +
                '}';
    }
}
