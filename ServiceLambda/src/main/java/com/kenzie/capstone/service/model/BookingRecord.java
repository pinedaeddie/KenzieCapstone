package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.kenzie.capstone.service.util.LocalDateTimeConverter;
import java.time.LocalDateTime;
import java.util.Objects;

@DynamoDBTable(tableName = "LambdaBooking")
public class BookingRecord {

    private String id;
    private String bookingId;
    private String patientName;
    private String providerName;
    private String gender;
    private String status;
    private boolean reminderSent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime bookingTime;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "bookingId")
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    @DynamoDBAttribute(attributeName = "patientName")
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    @DynamoDBAttribute(attributeName = "providerName")
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @DynamoDBAttribute(attributeName = "gender")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @DynamoDBAttribute(attributeName = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @DynamoDBAttribute(attributeName = "reminderSent")
    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    @DynamoDBAttribute(attributeName = "createdAt")
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @DynamoDBAttribute(attributeName = "updatedAt")
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @DynamoDBAttribute(attributeName = "bookingTime")
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingRecord that = (BookingRecord) o;
        return reminderSent == that.reminderSent &&
                Objects.equals(id, that.id) &&
                Objects.equals(bookingId, that.bookingId) &&
                Objects.equals(patientName, that.patientName) &&
                Objects.equals(providerName, that.providerName) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(bookingTime, that.bookingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookingId, patientName, providerName, gender, status, reminderSent, createdAt, updatedAt, bookingTime);
    }

    @Override
    public String toString() {
        return "BookingRecord{" +
                "id='" + id + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                ", reminderSent=" + reminderSent +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", bookingTime=" + bookingTime +
                '}';
    }
}
