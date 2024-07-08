package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.BookingDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.BookingData;
import com.kenzie.capstone.service.model.BookingRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingServiceTest {

    private BookingDao bookingDao;
    private LambdaService lambdaService;

    @BeforeAll
    void setup() {
        this.bookingDao = mock(BookingDao.class);
        this.lambdaService = new LambdaService(bookingDao);
    }


    /**  ------------------------------------------------------------------------
     *   LambdaService.getBookingData
     *   ------------------------------------------------------------------------ **/
    @Test
    void testGetBookingData_ValidId() {
        // GIVEN
        String validId = "valid-id";
        BookingRecord expectedBookingRecord = new BookingRecord();
        expectedBookingRecord.setId(validId);
        when(bookingDao.getBookingById(validId)).thenReturn(expectedBookingRecord);

        // WHEN
        BookingRecord result = lambdaService.getBookingData(validId);

        // THEN
        assertNotNull(result);
        assertEquals(validId, result.getId());
        verify(bookingDao).getBookingById(validId);
    }

    @Test
    void testGetBookingData_EmptyId_ThrowsInvalidDataException() {
        // GIVEN
        String emptyId = "";

        // WHEN / THEN
        assertThrows(InvalidDataException.class, () -> lambdaService.getBookingData(emptyId));
        verify(bookingDao, never()).getBookingById(emptyId);
    }

    @Test
    void testGetBookingData_NullId() {
        // WHEN / THEN
        assertThrows(InvalidDataException.class, () -> lambdaService.getBookingData(null));
    }

    @Test
    void testGetBookingData_NonExistingId() {
        // GIVEN
        String nonExistingId = "non-existing-id";
        when(bookingDao.getBookingById(nonExistingId)).thenReturn(null);

        // WHEN
        BookingRecord result = lambdaService.getBookingData(nonExistingId);

        // THEN
        assertNull(result);
    }

    @Test
    void testGetBookingData_ValidId_ReturnsBookingRecord() {
        // GIVEN
        String validId = "id";
        BookingRecord expectedRecord = new BookingRecord();
        expectedRecord.setId(validId);
        expectedRecord.setPatientName("John Doe");

        when(bookingDao.getBookingById(validId)).thenReturn(expectedRecord);

        // WHEN
        BookingRecord result = lambdaService.getBookingData(validId);

        // THEN
        assertNotNull(result);
        assertEquals(validId, result.getId());
        assertEquals(expectedRecord.getPatientName(), result.getPatientName());
        verify(bookingDao).getBookingById(validId);
    }


    /**  ------------------------------------------------------------------------
     *   LambdaService.saveBooking
     *   ------------------------------------------------------------------------ **/

    @Test
    void testSaveBooking_ValidData() {
        // GIVEN
        BookingData bookingData = new BookingData();
        bookingData.setId("valid-booking-id");
        bookingData.setPatientName("John Doe");
        bookingData.setProviderName("Dr. Smith");
        bookingData.setGender("Male");

        // WHEN
        lambdaService.saveBooking(bookingData);

        // THEN
        verify(bookingDao, times(1)).createBookingData(any(BookingRecord.class));
    }

    @Test
    void testSaveBooking_NullId() {
        // GIVEN
        BookingData bookingData = new BookingData();

        // WHEN / THEN
        assertThrows(InvalidDataException.class, () -> lambdaService.saveBooking(bookingData));
    }

    @Test
    void testSaveBooking_NullPatientName() {
        // GIVEN
        BookingData bookingData = new BookingData();
        bookingData.setId("valid-booking-id");
        bookingData.setProviderName("Dr. Smith");

        // WHEN / THEN
        assertThrows(InvalidDataException.class, () -> lambdaService.saveBooking(bookingData));
    }

    @Test
    void testSaveBooking_EmptyProviderName() {
        // GIVEN
        BookingData bookingData = new BookingData();
        bookingData.setId("valid-booking-id");
        bookingData.setPatientName("");

        // WHEN / THEN
        assertThrows(InvalidDataException.class, () -> lambdaService.saveBooking(bookingData));
    }



    /**  ------------------------------------------------------------------------
     *   LambdaService.updateBooking
     *   ------------------------------------------------------------------------ **/

    @Test
    void testUpdateBooking_ValidData() {
        // GIVEN
        String validId = "valid-id";
        BookingData updatedData = new BookingData();
        updatedData.setId(validId);
        updatedData.setPatientName("Jane Doe");
        updatedData.setProviderName("Dr. Smith");
        updatedData.setGender("Female");
        updatedData.setReminderSent(true);

        BookingRecord existingRecord = new BookingRecord();
        existingRecord.setId(validId);
        existingRecord.setPatientName("Existing Patient");
        existingRecord.setProviderName("Existing Provider");
        existingRecord.setGender("Existing Gender");
        existingRecord.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingRecord.setUpdatedAt(LocalDateTime.now());
        existingRecord.setReminderSent(false);

        ArgumentCaptor<BookingRecord> captor = ArgumentCaptor.forClass(BookingRecord.class);

        when(bookingDao.getBookingById(validId)).thenReturn(existingRecord);
        when(bookingDao.updateBookingData(any(BookingRecord.class))).thenReturn(existingRecord);

        // WHEN
        BookingData bookingData = lambdaService.updateBooking(validId, updatedData);

        // THEN
        assertNotNull(bookingData);
        assertEquals(validId, bookingData.getId());
        verify(bookingDao).updateBookingData(captor.capture());
        BookingRecord capturedRecord = captor.getValue();
        assertEquals(validId, capturedRecord.getId(), "The id passed to bookingDao.updateBookingData() should match");
    }

    @Test
    void testUpdateBooking_NullId() {
        // GIVEN
        BookingData updatedData = new BookingData();

        // WHEN / THEN
        assertThrows(InvalidDataException.class, () -> lambdaService.updateBooking(null, updatedData));
        verify(bookingDao, never()).updateBookingData(any());
    }

    @Test
    void testUpdateBooking_EmptyData() {
        // GIVEN
        String validId = "valid-id";
        BookingData emptyData = new BookingData();

        // WHEN / THEN
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> lambdaService.updateBooking(validId, emptyData));
        assertEquals("Booking data must contain patient name and provider name", exception.getMessage());
        verify(bookingDao, never()).updateBookingData(any(BookingRecord.class));
    }


    /**  ------------------------------------------------------------------------
     *   LambdaService.deleteBookings
     *   ------------------------------------------------------------------------ **/

    @Test
    void testDeleteBookings_ValidId() {
        // GIVEN
        String validId = "valid-id";
        when(bookingDao.deleteBookingById(validId)).thenReturn(true);

        // WHEN
        boolean result = lambdaService.deleteBookings(validId);

        // THEN
        assertTrue(result);
        verify(bookingDao, times(1)).deleteBookingById(validId);
    }

    @Test
    void testDeleteBookings_NonExistingId() {
        // GIVEN
        String nonExistingId = "non-existing-id";
        when(bookingDao.deleteBookingById(nonExistingId)).thenReturn(false);

        // WHEN
        boolean result = lambdaService.deleteBookings(nonExistingId);

        // THEN
        assertFalse(result);
        verify(bookingDao, times(1)).deleteBookingById(nonExistingId);
    }

    @Test
    void testDeleteBookings_MultipleIds() {
        // GIVEN
        List<String> bookingIds = Arrays.asList("id1", "id2", "id3");
        when(bookingDao.deleteBookingById(anyString())).thenReturn(true);

        // WHEN
        boolean allDeleted = bookingIds.stream().allMatch(lambdaService::deleteBookings);

        // THEN
        assertTrue(allDeleted);
        for (String id : bookingIds) {
            verify(bookingDao, times(1)).deleteBookingById(id);
        }
    }
    @Test
    void testDeleteBookings_NullId_ThrowsInvalidDataException() {
        // WHEN / THEN
        assertThrows(InvalidDataException.class, () -> lambdaService.deleteBookings(null));
        verify(bookingDao, never()).deleteBookingById(anyString());
    }

    @Test
    void testDeleteBookings_EmptyId_ThrowsInvalidDataException() {
        // WHEN / THEN
        assertThrows(InvalidDataException.class, () -> lambdaService.deleteBookings(""));
        verify(bookingDao, never()).deleteBookingById(anyString());
    }
}