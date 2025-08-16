package com.pgf.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pgf.cruddemo.entity.Employee;
import com.pgf.cruddemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
// note: path section 'employees' should move here, since it is common to
// all requests
@RequestMapping("/api")
public class EmployeeRestController {

    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService, ObjectMapper objectMapper) {
        this.employeeService = employeeService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee findById(@PathVariable int employeeId) {
        Employee employeeResult = employeeService.findById(employeeId);

        if (employeeResult == null) {
            throw new RuntimeException("Employee with id=" + employeeId + " not found");
        }

        return employeeResult;
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee) {

        // just in case they pass an id in JSON, set id to 0
        // this is to force a save of new item instead of update
        employee.setId(0);

        Employee dbEmployee = employeeService.save(employee);

        return dbEmployee;
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employee) {

        Employee dbEmployee = employeeService.save(employee);

        return dbEmployee;
    }

    // use PatchMapping for partial updates of entities
    @PatchMapping("/employees/{employeeId}")
    public Employee patchEmployee(@PathVariable int employeeId,
                                  @RequestBody Map<String, Object> patchPayload) {

        Employee tempEmployee = employeeService.findById(employeeId);

        // throw exception if employee does not exist in database
        if (tempEmployee == null) {
            throw new RuntimeException("Employee id: " + employeeId + " not found");
        }

        // throw exception if request body contains "id" key
        if (patchPayload.containsKey("id")) {
            throw new RuntimeException("Employee id not allowed in request body - " + employeeId);
        }

        Employee patchedEmployee = apply(patchPayload, tempEmployee);

        Employee dbEmployee = employeeService.save(patchedEmployee);

        return dbEmployee;
    }

    // utility method to apply patch ot object
    private Employee apply(Map<String, Object> patchPayload, Employee tempEmployee) {

        // 1. Convert object data and patch payload to JSON nodes
        ObjectNode employeeNode = objectMapper.convertValue(tempEmployee, ObjectNode.class);
        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        // 2. Apply patch to JSON object
        employeeNode.setAll(patchNode);

        // 3. Convert back patched JSON object to object
        Employee patchedEmployee = objectMapper.convertValue(employeeNode, Employee.class);

        return patchedEmployee;
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {

        Employee tempEmployee = employeeService.findById(employeeId);

        // throw exception if employee does not exist in database
        if (tempEmployee == null) {
            throw new RuntimeException("Employee id: " + employeeId + " not found");
        }

        employeeService.deleteById(employeeId);

        return "Deleted employee with id: " + employeeId;
    }
}
