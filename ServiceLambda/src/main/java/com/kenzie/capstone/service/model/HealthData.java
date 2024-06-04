package com.kenzie.capstone.service.model;

import java.time.LocalDateTime;

public class HealthData {
    private String id;             // Unique identifier for the health data entry
    private String userId;         // ID of the user (patient) whose health data is being recorded
    private String metric;         // Type of health metric (e.g., heart rate, blood pressure)
    private String value;          // Value of the health metric
    private LocalDateTime timestamp; // Timestamp when the data was recorded

    public HealthData() {
    }

    public HealthData(String id, String userId, String metric, String value, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.metric = metric;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "HealthData{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", metric='" + metric + '\'' +
                ", value='" + value + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
