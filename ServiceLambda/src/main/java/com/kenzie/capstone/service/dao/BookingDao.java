package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.BookingRecord;

public class BookingDao {

    private final DynamoDBMapper mapper;

    public BookingDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public BookingRecord getBookingById(String id) {
        return mapper.load(BookingRecord.class, id);
    }

    public void storeBookingData(BookingRecord bookingRecord) {
        try {
            mapper.save(bookingRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id", new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("ID has already been used");
        }
    }

    public BookingRecord updateBookingData(BookingRecord bookingRecord) {
        try {
            mapper.save(bookingRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id", new ExpectedAttributeValue().withExists(true)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("ID does not exist");
        }
        return bookingRecord;
    }

    public boolean deleteBookingById(String id) {
        BookingRecord bookingRecord = mapper.load(BookingRecord.class, id);

        if (bookingRecord == null) {
            return false;
        }
        mapper.delete(bookingRecord);
        return true;
    }
}