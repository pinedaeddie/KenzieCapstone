package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.AppointmentCreateRequest;
import com.kenzie.appserver.repositories.model.AppointmentRecord;
import com.kenzie.appserver.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    AppointmentService appointmentService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void createAppointment_CreateSuccessful() throws Exception {
        String patientFirstName = mockNeat.names().first().val();
        String patientLastName = mockNeat.names().last().val();
        String providerName = mockNeat.names().full().val();
        String gender = "Male";
        String appointmentDate = "2024-06-20";
        String appointmentTime = "10:30";

        AppointmentCreateRequest appointmentCreateRequest = new AppointmentCreateRequest();
        appointmentCreateRequest.setPatientFirstName(patientFirstName);
        appointmentCreateRequest.setPatientLastName(patientLastName);
        appointmentCreateRequest.setProviderName(providerName);
        appointmentCreateRequest.setGender(gender);
        appointmentCreateRequest.setAppointmentDate(appointmentDate);
        appointmentCreateRequest.setAppointmentTime(appointmentTime);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/appointments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(appointmentCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientFirstName").value(is(patientFirstName)))
                .andExpect(jsonPath("$.patientLastName").value(is(patientLastName)))
                .andExpect(jsonPath("$.providerName").value(is(providerName)))
                .andExpect(jsonPath("$.gender").value(is(gender)))
                .andExpect(jsonPath("$.appointmentDate").value(is(appointmentDate)))
                .andExpect(jsonPath("$.appointmentTime").value(is(appointmentTime)));
    }

    @Test
    public void getAppointmentById_Exists() throws Exception {
        String patientFirstName = mockNeat.names().first().val();
        String patientLastName = mockNeat.names().last().val();
        String providerName = mockNeat.names().full().val();
        String gender = "Female";
        String appointmentDate = "2024-06-21";
        String appointmentTime = "14:30";

        AppointmentCreateRequest appointmentCreateRequest = new AppointmentCreateRequest();
        appointmentCreateRequest.setPatientFirstName(patientFirstName);
        appointmentCreateRequest.setPatientLastName(patientLastName);
        appointmentCreateRequest.setProviderName(providerName);
        appointmentCreateRequest.setGender(gender);
        appointmentCreateRequest.setAppointmentDate(appointmentDate);
        appointmentCreateRequest.setAppointmentTime(appointmentTime);

        mapper.registerModule(new JavaTimeModule());

        String response = mvc.perform(post("/appointments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(appointmentCreateRequest)))
                .andReturn().getResponse().getContentAsString();

        AppointmentRecord appointmentRecord = mapper.readValue(response, AppointmentRecord.class);

        mvc.perform(get("/appointments/" + appointmentRecord.getAppointmentId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentId").value(is(appointmentRecord.getAppointmentId())))
                .andExpect(jsonPath("$.patientFirstName").value(is(patientFirstName)))
                .andExpect(jsonPath("$.patientLastName").value(is(patientLastName)))
                .andExpect(jsonPath("$.providerName").value(is(providerName)))
                .andExpect(jsonPath("$.gender").value(is(gender)))
                .andExpect(jsonPath("$.appointmentDate").value(is(appointmentDate)))
                .andExpect(jsonPath("$.appointmentTime").value(is(appointmentTime)));
    }

    @Test
    public void getAllAppointments_ReturnsAppointments() throws Exception {
        mvc.perform(get("/appointments")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void deleteAppointmentById_DeletesSuccessfully() throws Exception {
        String patientFirstName = mockNeat.names().first().val();
        String patientLastName = mockNeat.names().last().val();
        String providerName = mockNeat.names().full().val();
        String gender = "Female";
        String appointmentDate = "2024-06-21";
        String appointmentTime = "14:30";

        AppointmentCreateRequest appointmentCreateRequest = new AppointmentCreateRequest();
        appointmentCreateRequest.setPatientFirstName(patientFirstName);
        appointmentCreateRequest.setPatientLastName(patientLastName);
        appointmentCreateRequest.setProviderName(providerName);
        appointmentCreateRequest.setGender(gender);
        appointmentCreateRequest.setAppointmentDate(appointmentDate);
        appointmentCreateRequest.setAppointmentTime(appointmentTime);

        mapper.registerModule(new JavaTimeModule());

        String response = mvc.perform(post("/appointments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(appointmentCreateRequest)))
                .andReturn().getResponse().getContentAsString();

        AppointmentRecord appointmentRecord = mapper.readValue(response, AppointmentRecord.class);

        mvc.perform(delete("/appointments/" + appointmentRecord.getAppointmentId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateAppointment_UpdatesSuccessfully() throws Exception {
        String patientFirstName = mockNeat.names().first().val();
        String patientLastName = mockNeat.names().last().val();
        String providerName = mockNeat.names().full().val();
        String gender = "Female";
        String appointmentDate = "2024-06-21";
        String appointmentTime = "14:30";

        AppointmentCreateRequest appointmentCreateRequest = new AppointmentCreateRequest();
        appointmentCreateRequest.setPatientFirstName(patientFirstName);
        appointmentCreateRequest.setPatientLastName(patientLastName);
        appointmentCreateRequest.setProviderName(providerName);
        appointmentCreateRequest.setGender(gender);
        appointmentCreateRequest.setAppointmentDate(appointmentDate);
        appointmentCreateRequest.setAppointmentTime(appointmentTime);

        mapper.registerModule(new JavaTimeModule());

        String response = mvc.perform(post("/appointments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(appointmentCreateRequest)))
                .andReturn().getResponse().getContentAsString();

        AppointmentRecord appointmentRecord = mapper.readValue(response, AppointmentRecord.class);

        String updatedProviderName = mockNeat.names().full().val();
        appointmentCreateRequest.setProviderName(updatedProviderName);

        mvc.perform(put("/appointments/" + appointmentRecord.getAppointmentId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(appointmentCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.providerName").value(is(updatedProviderName)));
    }
}
