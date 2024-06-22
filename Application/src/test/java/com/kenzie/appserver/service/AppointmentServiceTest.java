package com.kenzie.appserver.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.AppointmentCreateRequest;
import com.kenzie.appserver.controller.model.AppointmentResponse;
import com.kenzie.appserver.repositories.AppointmentRepository;
import com.kenzie.appserver.repositories.model.AppointmentRecord;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.BookingData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AppointmentServiceTest {

    private AppointmentRepository appointmentRepository;
    private LambdaServiceClient lambdaServiceClient;
    private CacheStore cache;
    private AppointmentService appointmentService;

    @BeforeEach
    public void setup() {
        appointmentRepository = mock(AppointmentRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        cache = mock(CacheStore.class);
        appointmentService = new AppointmentService(appointmentRepository, lambdaServiceClient, cache);
    }

    /**  ------------------------------------------------------------------------
     *   AppointmentService.createAppointment
     *   ------------------------------------------------------------------------ **/

    @Test
    public void testCreateAppointment() {
        // GIVEN
        AppointmentCreateRequest request = new AppointmentCreateRequest();
        request.setPatientFirstName("John");
        request.setPatientLastName("Doe");
        request.setProviderName("Dr. Smith");
        request.setGender("Male");
        request.setAppointmentDate("2023-06-15");
        request.setAppointmentTime("10:00");

        // WHEN / THEN
        ArgumentCaptor<AppointmentRecord> recordCaptor = ArgumentCaptor.forClass(AppointmentRecord.class);
        AppointmentResponse response = appointmentService.createAppointment(request);

        verify(appointmentRepository, times(1)).save(recordCaptor.capture());
        verify(lambdaServiceClient, times(1)).createBooking(any(BookingData.class));

        assertNotNull(response.getAppointmentId());
        assertEquals(request.getPatientFirstName(), response.getPatientFirstName());
        assertEquals(request.getPatientLastName(), response.getPatientLastName());
        assertEquals(request.getProviderName(), response.getProviderName());
        assertEquals(request.getGender(), response.getGender());
        assertEquals(request.getAppointmentDate(), response.getAppointmentDate());
        assertEquals(request.getAppointmentTime(), response.getAppointmentTime());
    }

    @Test
    public void testCreateAppointment_SaveFailure() {
        // GIVEN
        AppointmentCreateRequest request = new AppointmentCreateRequest();
        request.setPatientFirstName("John");
        request.setPatientLastName("Doe");
        request.setProviderName("Dr. Smith");
        request.setGender("Male");
        request.setAppointmentDate("2023-06-15");
        request.setAppointmentTime("10:00");

        doThrow(RuntimeException.class).when(appointmentRepository).save(any(AppointmentRecord.class));

        // WHEN / THEN
        assertThrows(RuntimeException.class, () -> appointmentService.createAppointment(request));
        verify(appointmentRepository, times(1)).save(any(AppointmentRecord.class));
        verifyNoMoreInteractions(appointmentRepository);
    }


    /**  ------------------------------------------------------------------------
     *   AppointmentService.getAppointmentById
     *   ------------------------------------------------------------------------ **/

    @Test
    public void testGetAppointmentById_foundInCache() {
        // GIVEN
        String appointmentId = UUID.randomUUID().toString();
        AppointmentRecord cachedRecord = new AppointmentRecord();
        cachedRecord.setAppointmentId(appointmentId);

        // WHEN
        when(cache.get(appointmentId)).thenReturn(cachedRecord);
        AppointmentResponse result = appointmentService.getAppointmentById(appointmentId);

        // THEN
        assertNotNull(result);
        assertEquals(cachedRecord.getAppointmentId(), result.getAppointmentId());
        verify(appointmentRepository, times(0)).findById(appointmentId);
    }

    @Test
    public void testGetAppointmentById_foundInRepository() {
        // GIVEN
        String appointmentId = UUID.randomUUID().toString();
        AppointmentRecord record = new AppointmentRecord();
        record.setAppointmentId(appointmentId);

        // WHEN
        when(cache.get(appointmentId)).thenReturn(null);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(record));
        AppointmentResponse result = appointmentService.getAppointmentById(appointmentId);

        // THEN
        assertNotNull(result);
        assertEquals(record.getAppointmentId(), result.getAppointmentId());
        verify(cache, times(1)).add(appointmentId, record);
    }

    @Test
    public void testGetAppointmentById_notFound() {
        // GIVEN
        String appointmentId = UUID.randomUUID().toString();

        // WHEN / THEN
        when(cache.get(appointmentId)).thenReturn(null);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        AppointmentResponse result = appointmentService.getAppointmentById(appointmentId);
        assertNull(result);
    }


    /**  ------------------------------------------------------------------------
     *   AppointmentService.getAllAppointments
     *   ------------------------------------------------------------------------ **/

    @Test
    public void testGetAllAppointments_Success() {
        // GIVEN
        AppointmentRecord appointment1 = new AppointmentRecord();
        appointment1.setAppointmentId("1");
        appointment1.setPatientFirstName("John");

        AppointmentRecord appointment2 = new AppointmentRecord();
        appointment2.setAppointmentId("2");
        appointment2.setPatientFirstName("Jane");

        Iterable<AppointmentRecord> mockAppointments = List.of(appointment1, appointment2);
        when(appointmentRepository.findAll()).thenReturn(mockAppointments);

        // WHEN
        Iterable<AppointmentRecord> result = appointmentService.getAllAppointments();

        // THEN
        assertNotNull(result);
        assertEquals(mockAppointments, result);
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllAppointments_EmptyList() {
        // GIVEN
        when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

        // WHEN
        Iterable<AppointmentRecord> result = appointmentService.getAllAppointments();

        // THEN
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(appointmentRepository, times(1)).findAll();
    }


    /**  ------------------------------------------------------------------------
     *   AppointmentService.updateAppointmentById
     *   ------------------------------------------------------------------------ **/

    @Test
    public void testUpdateAppointment() {
        // GIVEN
        String appointmentId = UUID.randomUUID().toString();
        AppointmentCreateRequest request = new AppointmentCreateRequest();
        request.setPatientFirstName("John");
        request.setPatientLastName("Doe");
        request.setProviderName("Dr. Smith");
        request.setGender("Male");
        request.setAppointmentDate("2023-06-15");
        request.setAppointmentTime("10:00");

        AppointmentRecord record = new AppointmentRecord();
        record.setAppointmentId(appointmentId);

        // WHEN
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(record));
        when(appointmentRepository.save(any(AppointmentRecord.class))).thenReturn(record);
        AppointmentRecord updatedRecord = appointmentService.updateAppointmentById(appointmentId, request);

        // THEN
        assertNotNull(updatedRecord);
        assertEquals(appointmentId, updatedRecord.getAppointmentId());
        assertEquals(request.getPatientFirstName(), updatedRecord.getPatientFirstName());
        assertEquals(request.getPatientLastName(), updatedRecord.getPatientLastName());
        assertEquals(request.getProviderName(), updatedRecord.getProviderName());
        assertEquals(request.getGender(), updatedRecord.getGender());
        assertEquals(request.getAppointmentDate(), updatedRecord.getAppointmentDate());
        assertEquals(request.getAppointmentTime(), updatedRecord.getAppointmentTime());

        verify(cache, times(1)).evict(appointmentId);
        verify(cache, times(1)).add(appointmentId, updatedRecord);
        verify(lambdaServiceClient, times(1)).updateBooking(any(BookingData.class));
    }

    @Test
    public void testUpdateAppointment_notFound_throwsException() {
        // GIVEN
        String appointmentId = UUID.randomUUID().toString();
        AppointmentCreateRequest request = new AppointmentCreateRequest();
        request.setPatientFirstName("John");
        request.setPatientLastName("Doe");
        request.setProviderName("Dr. Smith");
        request.setGender("Male");
        request.setAppointmentDate("2023-06-15");
        request.setAppointmentTime("10:00");

        // WHEN / THEN
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.updateAppointmentById(appointmentId, request);
        });
        assertEquals("Appointment not found with id: " + appointmentId, exception.getMessage());
    }

    @Test
    public void testUpdateAppointment_nullId_throwsException() {
        // GIVEN
        AppointmentCreateRequest request = new AppointmentCreateRequest();
        request.setPatientFirstName("John");
        request.setPatientLastName("Doe");
        request.setProviderName("Dr. Smith");
        request.setGender("Male");
        request.setAppointmentDate("2023-06-15");
        request.setAppointmentTime("10:00");

        // WHEN / THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.updateAppointmentById(null, request);
        });
        assertEquals("Appointment ID cannot be null", exception.getMessage());
    }


    /**  ------------------------------------------------------------------------
     *   AppointmentService.deleteAppointmentById
     *   ------------------------------------------------------------------------ **/

    @Test
    public void testDeleteAppointmentById() {
        // GIVEN
        String appointmentId = UUID.randomUUID().toString();
        AppointmentRecord appointmentRecord = new AppointmentRecord();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointmentRecord));
        doNothing().when(appointmentRepository).deleteById(appointmentId);
        doNothing().when(cache).evict(appointmentId);
        when(lambdaServiceClient.deleteBooking(appointmentId)).thenReturn(true);

        // WHEN
        AppointmentRecord deletedRecord = appointmentService.deleteAppointmentById(appointmentId);

        // THEN
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).deleteById(appointmentId);
        verify(cache, times(1)).evict(appointmentId);
        verify(lambdaServiceClient, times(1)).deleteBooking(appointmentId);
        assertEquals(appointmentRecord, deletedRecord);
    }

    @Test
    public void testDeleteAppointmentById_nullId_throwsException() {
        // GIVEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.deleteAppointmentById(null);
        });

        // WHEN / THEN
        assertEquals("Appointment ID cannot be null", exception.getMessage());
    }
}