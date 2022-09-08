package com.jobrunr.dashboard.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jobrunr.dashboard.model.Employee;

public interface EmployeeRepository  extends JpaRepository<Employee, Long>{

	@Query("select e.id from Employee e")
    Stream<Long> getAllEmployeeIds();
}
