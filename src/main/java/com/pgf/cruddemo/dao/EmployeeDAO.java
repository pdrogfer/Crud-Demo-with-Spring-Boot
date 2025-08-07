package com.pgf.cruddemo.dao;

import com.pgf.cruddemo.entity.Employee;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> findAll();
}
