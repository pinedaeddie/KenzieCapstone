package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.BookingData;

public class LambdaServiceClient {

    private static final String CREATE_BOOKING_ENDPOINT = "booking/create";
    private static final String GET_BOOKING_ENDPOINT = "booking/{id}";
    private static final String UPDATE_BOOKING_ENDPOINT = "booking/update";
    private static final String DELETE_BOOKING_ENDPOINT = "booking/delete/{id}";

    private final ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public void createBooking(BookingData bookingData) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(bookingData);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.postEndpoint(CREATE_BOOKING_ENDPOINT, request);

        try {
            mapper.readValue(response, BookingData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
    }

    public void getBooking(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_BOOKING_ENDPOINT.replace("{id}", id));

        try {
            mapper.readValue(response, new TypeReference<>(){});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
    }

    public void updateBooking(BookingData bookingData) {
        EndpointUtility endpointUtility = new EndpointUtility();

        String request;
        try {
            request = mapper.writeValueAsString(bookingData);
            System.out.println("Serialized BookingData: " + request);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e.getMessage());
        }

        String response;
        try {
            response = endpointUtility.postEndpoint(UPDATE_BOOKING_ENDPOINT, request);
            System.out.println("Response from Lambda: " + response);
        } catch (Exception e) {
            throw new ApiGatewayException("Error while calling Lambda service: " + e.getMessage());
        }

        try {
            mapper.readValue(response, BookingData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to deserialize response: " + e.getMessage());
        }
    }

    public boolean deleteBooking(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;

        try {
            request = mapper.writeValueAsString(id);
        } catch(JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }

        String response = endpointUtility.postEndpoint(DELETE_BOOKING_ENDPOINT.replace("{id}", id), request);
        boolean outcome;
        try {
            outcome = mapper.readValue(response, Boolean.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return outcome;
    }
}