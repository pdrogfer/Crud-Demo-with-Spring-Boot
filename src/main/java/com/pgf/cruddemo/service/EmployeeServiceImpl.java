package com.pgf.cruddemo.service;

import com.pgf.cruddemo.dao.EmployeeRepository;
import com.pgf.cruddemo.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(int employeeId) {
        Optional<Employee> result = employeeRepository.findById(employeeId);
        Employee dbEmployee;
        if (result.isPresent()) {
            dbEmployee = result.get();
        } else {
            throw new RuntimeException("Employee with id: " + employeeId + " not found.");
        }
        return dbEmployee;
    }

    // no need to use @Transactional when using JPA
    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    // no need to use @Transactional when using JPA
    @Override
    public void deleteById(int id) {
        employeeRepository.deleteById(id);
    }
}
