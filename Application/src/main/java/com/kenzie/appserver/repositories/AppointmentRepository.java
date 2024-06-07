package com.kenzie.appserver.repositories;


import com.kenzie.appserver.repositories.model.AppointmentRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface AppointmentRepository extends CrudRepository<AppointmentRecord, String> {
}
