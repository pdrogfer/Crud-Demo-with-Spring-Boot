package com.pgf.cruddemo.dao;

import com.pgf.cruddemo.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

    private final EntityManager entityManager;

    @Autowired
    public EmployeeDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Employee> findAll() {

        // create query
        TypedQuery<Employee> query = entityManager.createQuery("from Employee", Employee.class);

        // execute query
        List<Employee> employeesResult = query.getResultList();

        // return result
        return employeesResult;
    }

    @Override
    public Employee findById(int id) {

        Employee employee = entityManager.find(Employee.class, id);

        return employee;
    }

    // @Transactional is moved to the Service layer
    @Override
    public Employee save(Employee employee) {

        /*
        * if id == 0, it performs an insert/save. Else, an update.
        * It returns the new/updated employee
        */
        Employee dbEmployee = entityManager.merge(employee);

        return dbEmployee;
    }

    // @Transactional is moved to the Service layer
    @Override
    public void deleteById(int id) {

        Employee dbEmployee = entityManager.find(Employee.class, id);

        entityManager.remove(dbEmployee);
    }
}
