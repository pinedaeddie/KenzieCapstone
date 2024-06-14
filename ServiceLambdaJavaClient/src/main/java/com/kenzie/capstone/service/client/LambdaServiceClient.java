package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.BookingData;

public class LambdaServiceClient {

    private static final String CREATE_BOOKING_ENDPOINT = "booking/schedule";
    private static final String GET_BOOKING_ENDPOINT = "booking/{id}";
    private static final String UPDATE_BOOKING_ENDPOINT = "booking/update";
    private static final String DELETE_BOOKING_ENDPOINT = "booking/delete/{id}";
    private static final String SET_BOOKING_ENDPOINT = "booking/set";

    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public BookingData scheduleBooking(BookingData bookingData) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(bookingData);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.postEndpoint(CREATE_BOOKING_ENDPOINT, request);
        BookingData scheduledData;
        try {
            scheduledData = mapper.readValue(response, BookingData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return scheduledData;
    }

    public BookingData updateBooking(BookingData bookingData) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(bookingData);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.postEndpoint(UPDATE_BOOKING_ENDPOINT, request);
        BookingData updatedData;
        try {
            updatedData = mapper.readValue(response, BookingData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return updatedData;
    }

    public boolean deleteBooking(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(DELETE_BOOKING_ENDPOINT.replace("{id}", id), "");
        boolean outcome;
        try {
            outcome = mapper.readValue(response, Boolean.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return outcome;
    }


    public BookingData getBookingData(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_BOOKING_ENDPOINT.replace("{id}", id));
        BookingData bookingData;
        try {
            bookingData = mapper.readValue(response, BookingData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return bookingData;
    }

    public BookingData setBookingData(BookingData bookingData) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(bookingData);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.postEndpoint(SET_BOOKING_ENDPOINT, request);
        BookingData setData;
        try {
            setData = mapper.readValue(response, BookingData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return setData;
    }
}