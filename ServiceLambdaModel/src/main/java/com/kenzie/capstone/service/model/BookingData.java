package com.kenzie.capstone.service.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class BookingData {

    private String id;
    private String bookingId;
    private String patientName;
    private String patientLastName;
    private String providerName;
    private String gender;
    private boolean reminderSent;
    private String appointmentDate;
    private String appointmentTime;
    private LocalDateTime bookingTime;

    public BookingData(String id, String bookingId, String patientName, String patientLastName, String providerName, String gender,
                       boolean reminderSent, String appointmentDate, String appointmentTime, LocalDateTime bookingTime) {
        this.id = id;
        this.bookingId = bookingId;
        this.patientName = patientName;
        this.patientLastName = patientLastName;
        this.providerName = providerName;
        this.gender = gender;
        this.reminderSent = reminderSent;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.bookingTime = bookingTime;
    }

    public BookingData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingData)) return false;
        BookingData that = (BookingData) o;
        return reminderSent == that.reminderSent && Objects.equals(id, that.id) && Objects.equals(bookingId, that.bookingId) && Objects.equals(patientName, that.patientName) && Objects.equals(patientLastName, that.patientLastName) && Objects.equals(providerName, that.providerName) && Objects.equals(gender, that.gender) && Objects.equals(appointmentDate, that.appointmentDate) && Objects.equals(appointmentTime, that.appointmentTime) && Objects.equals(bookingTime, that.bookingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookingId, patientName, patientLastName, providerName, gender, reminderSent, appointmentDate, appointmentTime, bookingTime);
    }

    @Override
    public String toString() {
        return "BookingData{" +
                "id='" + id + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientLastName='" + patientLastName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", gender='" + gender + '\'' +
                ", reminderSent=" + reminderSent +
                ", appointmentDate='" + appointmentDate + '\'' +
                ", appointmentTime='" + appointmentTime + '\'' +
                ", bookingTime=" + bookingTime +
                '}';
    }
}
