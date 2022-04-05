package com.applife.controller;

import com.applife.model.Employee;
import com.applife.service.EmployeeService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class EmployeeControllerTests {

    public static final String API_EMPLOYEES_PATH = "/api/employees";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    // EmployeeController'a hizmet eden servisi MockBean ile tanimliyoruz
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("Employee Controller createEmployee")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post(API_EMPLOYEES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }


    @DisplayName("Employee Controller getAllEmployees")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {

        // given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Furkan").lastName("Uzun").email("furkan@hotmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("deneme").lastName("deneme").email("deneme@hotmail.com").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_EMPLOYEES_PATH));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @DisplayName("Employee Controller getEmployeeById (positive)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_EMPLOYEES_PATH + "/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("Employee Controller getEmployeeById (negative)")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_EMPLOYEES_PATH + "/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }


    @DisplayName("Employee Controller updateEmployee (positive)")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("deneme")
                .lastName("deneme")
                .email("deneme@hotmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(put(API_EMPLOYEES_PATH + "/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("Employee Controller updateEmployee (negative)")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("deneme")
                .lastName("deneme")
                .email("deneme@hotmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(put(API_EMPLOYEES_PATH + "/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("Employee Controller deleteEmployee")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

        // given - precondition or setup
        long employeeId = 1;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(delete(API_EMPLOYEES_PATH + "/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
