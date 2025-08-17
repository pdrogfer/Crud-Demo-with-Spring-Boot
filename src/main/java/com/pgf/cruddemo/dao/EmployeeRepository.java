package com.pgf.cruddemo.dao;

import com.pgf.cruddemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

// JPA interface to replace manual DAO implementation in latest commit
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // provides all CRUD methods, no need to create an implementation
}
