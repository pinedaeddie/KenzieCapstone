package com.kenzie.appserver.repositories;


import com.kenzie.appserver.repositories.model.Appointment;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface AppointmentRepository extends CrudRepository<Appointment, String> {
}
