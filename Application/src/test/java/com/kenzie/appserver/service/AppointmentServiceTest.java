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

    @Test
    public void testCreateAppointment() {
        AppointmentCreateRequest request = new AppointmentCreateRequest();
        request.setPatientFirstName("John");
        request.setPatientLastName("Doe");
        request.setProviderName("Dr. Smith");
        request.setGender("Male");
        request.setAppointmentDate("2023-06-15");
        request.setAppointmentTime("10:00");

        ArgumentCaptor<AppointmentRecord> recordCaptor = ArgumentCaptor.forClass(AppointmentRecord.class);

        AppointmentResponse response = appointmentService.createAppointment(request);

        verify(appointmentRepository, times(1)).save(recordCaptor.capture());
        verify(lambdaServiceClient, times(1)).scheduleBooking(any(BookingData.class));

        assertNotNull(response.getAppointmentId());
        assertEquals(request.getPatientFirstName(), response.getPatientFirstName());
        assertEquals(request.getPatientLastName(), response.getPatientLastName());
        assertEquals(request.getProviderName(), response.getProviderName());
        assertEquals(request.getGender(), response.getGender());
        assertEquals(request.getAppointmentDate(), response.getAppointmentDate());
        assertEquals(request.getAppointmentTime(), response.getAppointmentTime());
    }

    @Test
    public void testGetAppointmentById_foundInCache() {
        String appointmentId = UUID.randomUUID().toString();
        AppointmentRecord cachedRecord = new AppointmentRecord();
        cachedRecord.setAppointmentId(appointmentId);

        when(cache.get(appointmentId)).thenReturn(cachedRecord);

        Optional<AppointmentRecord> result = appointmentService.getAppointmentById(appointmentId);

        assertTrue(result.isPresent());
        assertEquals(cachedRecord, result.get());
        verify(appointmentRepository, times(0)).findById(appointmentId);
    }

    @Test
    public void testGetAppointmentById_foundInRepository() {
        String appointmentId = UUID.randomUUID().toString();
        AppointmentRecord record = new AppointmentRecord();
        record.setAppointmentId(appointmentId);

        when(cache.get(appointmentId)).thenReturn(null);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(record));

        Optional<AppointmentRecord> result = appointmentService.getAppointmentById(appointmentId);

        assertTrue(result.isPresent());
        assertEquals(record, result.get());
        verify(cache, times(1)).add(appointmentId, record);
    }

    @Test
    public void testGetAppointmentById_notFound() {
        String appointmentId = UUID.randomUUID().toString();

        when(cache.get(appointmentId)).thenReturn(null);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        Optional<AppointmentRecord> result = appointmentService.getAppointmentById(appointmentId);

        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateAppointment() {
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

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(record));
        when(appointmentRepository.save(any(AppointmentRecord.class))).thenReturn(record);

        AppointmentRecord updatedRecord = appointmentService.updateAppointment(appointmentId, request);

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
        String appointmentId = UUID.randomUUID().toString();
        AppointmentCreateRequest request = new AppointmentCreateRequest();
        request.setPatientFirstName("John");
        request.setPatientLastName("Doe");
        request.setProviderName("Dr. Smith");
        request.setGender("Male");
        request.setAppointmentDate("2023-06-15");
        request.setAppointmentTime("10:00");

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.updateAppointment(appointmentId, request);
        });

        assertEquals("Appointment not found with id: " + appointmentId, exception.getMessage());
    }

    @Test
    public void testUpdateAppointment_nullId_throwsException() {
        AppointmentCreateRequest request = new AppointmentCreateRequest();
        request.setPatientFirstName("John");
        request.setPatientLastName("Doe");
        request.setProviderName("Dr. Smith");
        request.setGender("Male");
        request.setAppointmentDate("2023-06-15");
        request.setAppointmentTime("10:00");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.updateAppointment(null, request);
        });

        assertEquals("Appointment ID cannot be null", exception.getMessage());
    }

    @Test
    public void testDeleteAppointmentById() {
        String appointmentId = UUID.randomUUID().toString();

        doNothing().when(appointmentRepository).deleteById(appointmentId);
        doNothing().when(cache).evict(appointmentId);
        doNothing().when(lambdaServiceClient).deleteBooking(appointmentId);

        appointmentService.deleteAppointmentById(appointmentId);

        verify(appointmentRepository, times(1)).deleteById(appointmentId);
        verify(cache, times(1)).evict(appointmentId);
        verify(lambdaServiceClient, times(1)).deleteBooking(appointmentId);
    }

    @Test
    public void testDeleteAppointmentById_nullId_throwsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.deleteAppointmentById(null);
        });

        assertEquals("Appointment ID cannot be null", exception.getMessage());
    }
}