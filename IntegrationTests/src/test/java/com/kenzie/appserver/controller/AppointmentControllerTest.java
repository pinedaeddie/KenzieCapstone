package com.kenzie.appserver.controller;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.AppointmentCreateRequest;
import com.kenzie.appserver.controller.model.AppointmentResponse;
import com.kenzie.appserver.controller.model.AppointmentUpdateRequest;
import com.kenzie.appserver.repositories.model.AppointmentRecord;
import com.kenzie.appserver.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    AppointmentService appointmentService;

    private static final MockNeat mockNeat = MockNeat.threadLocal();

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setup() {
        mapper.registerModule(new Jdk8Module());
    }

    @AfterEach
    public void cleanup() {
        appointmentService.deleteAllAppointments();
    }



    /**  ------------------------------------------------------------------------
     *   AppointmentController.createAppointment
     *   ------------------------------------------------------------------------ **/

    @Test
    public void createAppointment_CreateSuccessful() throws Exception {
        // GIVEN
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

        // WHEN
        ResultActions actions = mvc.perform(post("/appointments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(appointmentCreateRequest)))
                .andExpect(status().is2xxSuccessful());

        // THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        AppointmentResponse response = mapper.readValue(responseBody, AppointmentResponse.class);
        assertThat(response.getAppointmentId()).isNotEmpty().as("The ID is populated");
    }

    @Test
    public void createAppointment_NullRequest() throws Exception {
        //GIVEN
        AppointmentCreateRequest request = new AppointmentCreateRequest();
        request.setPatientFirstName("John");
        request.setPatientLastName("Doe");
        request.setProviderName("Dr. Smith");
        request.setGender("Male");
        request.setAppointmentDate("2023-06-15");
        request.setAppointmentTime("10:00");

        // WHEN/THEN
        mvc.perform(post("/appointments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }


    /**  ------------------------------------------------------------------------
     *   AppointmentController.getAppointmentById
     *   ------------------------------------------------------------------------ **/

    @Test
    public void getAppointmentById_NotFound() throws Exception {
        //GIVEN
        String nonExistentId = UUID.randomUUID().toString();

        //WHEN/THEN
        mvc.perform(get("/appointments/{id}", nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getAppointmentById_NullId() throws Exception {
        //GIVEN --> Null ID

        //WHEN/THEN
        mvc.perform(get("/appointments/{id}", "null")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getAppointmentById_Successfully() throws Exception {
        // GIVEN
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

        AppointmentResponse appointmentResponse = appointmentService.createAppointment(appointmentCreateRequest);

        // WHEN
        ResultActions actions = mvc.perform(get("/appointments/{id}", appointmentResponse.getAppointmentId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        // THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        AppointmentResponse response = mapper.readValue(responseBody, AppointmentResponse.class);

        assertThat(response.getAppointmentId()).isNotEmpty().as("The Appointment ID is populated");
        assertThat(response.getPatientFirstName()).isNotEmpty().as("The PatientFirstName is populated");
        assertThat(response.getPatientLastName()).isNotEmpty().as("The PatientLastName is populated");
        assertThat(response.getProviderName()).isNotEmpty().as("The ProviderName is populated");
    }


    /**  ------------------------------------------------------------------------
     *   AppointmentController.getAllAppointments
     *   ------------------------------------------------------------------------ **/

    @Test
    public void getAllAppointments_ReturnsAppointmentsSuccessfully() throws Exception {
        //WHEN/THEN
        mvc.perform(get("/appointments/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getAllAppointments_EmptyList() throws Exception {

        //WHEN/THEN
        mvc.perform(get("/appointments/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }


    /**  ------------------------------------------------------------------------
     *   AppointmentController.updateAppointmentById
     *   ------------------------------------------------------------------------ **/

    @Test
    public void updateAppointment_UpdateSuccessful() throws Exception {
        //GIVEN
        String patientFirstName = mockNeat.names().first().val();
        String patientLastName = mockNeat.names().last().val();
        String providerName = mockNeat.names().full().val();
        String gender = "Female";
        String appointmentDate = "2024-07-15";
        String appointmentTime = "14:14";

        AppointmentCreateRequest appointmentCreateRequest = new AppointmentCreateRequest();
        appointmentCreateRequest.setPatientFirstName(patientFirstName);
        appointmentCreateRequest.setPatientLastName(patientLastName);
        appointmentCreateRequest.setProviderName(providerName);
        appointmentCreateRequest.setGender(gender);
        appointmentCreateRequest.setAppointmentDate(appointmentDate);
        appointmentCreateRequest.setAppointmentTime(appointmentTime);

        AppointmentResponse appointmentResponse = appointmentService.createAppointment(appointmentCreateRequest);

        AppointmentUpdateRequest updateRequest = new AppointmentUpdateRequest();
        updateRequest.setAppointmentId(appointmentResponse.getAppointmentId());
        updateRequest.setPatientFirstName(mockNeat.names().first().val());
        updateRequest.setProviderName(mockNeat.names().full().val());

        //WHEN
        ResultActions actions = mvc.perform(put("/appointments/{id}".replace("{id}", appointmentResponse.getAppointmentId()))
                        .content(mapper.writeValueAsString(updateRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        // THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        AppointmentResponse response = mapper.readValue(responseBody, AppointmentResponse.class);
        assertThat(response.getAppointmentId()).isNotEmpty().as("The ID is populated");
        assertThat(response.getPatientFirstName()).isEqualTo(updateRequest.getPatientFirstName()).as("The name is correct");
    }

    @Test
    public void updateAppointmentById_EmptyId() throws Exception {
        //GIVEN
        String id = " ";
        AppointmentResponse response = new AppointmentResponse();
        response.setAppointmentId(" ");

        //WHEN/THEN
        mvc.perform(put("/appointments/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(response)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateAppointmentById_NotFound() throws Exception {
        //GIVEN
        String nonExistentId = UUID.randomUUID().toString();
        AppointmentCreateRequest appointmentCreateRequest = new AppointmentCreateRequest();

        //WHEN/THEN
        mvc.perform(put("/appointments/{id}", nonExistentId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(appointmentCreateRequest)))
                .andExpect(status().is4xxClientError());
    }


    /**  ------------------------------------------------------------------------
     *   AppointmentController.deleteAppointmentById
     *   ------------------------------------------------------------------------ **/

    @Test
    public void deleteAppointmentById_DeleteSuccessful() throws Exception {
        //GIVEN
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

        //WHEN
        String response = mvc.perform(post("/appointments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(appointmentCreateRequest)))
                .andReturn().getResponse().getContentAsString();

        AppointmentRecord appointmentRecord = mapper.readValue(response, AppointmentRecord.class);

        //THEN
        mvc.perform(delete("/appointments/{id}".replace("{id}", appointmentRecord.getAppointmentId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteAppointmentById_NullId() throws Exception {
        //GIVEN --> Null ID

        //WHEN/THEN
        mvc.perform(delete("/appointments/{id}",  "null")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteAppointmentById_NotFound() throws Exception {
        //GIVEN
        String nonExistentId = UUID.randomUUID().toString();

        //WHEN/Then
        mvc.perform(delete("/appointments/{id}", nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}