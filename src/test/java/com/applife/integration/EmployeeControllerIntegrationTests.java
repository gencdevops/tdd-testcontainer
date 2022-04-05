package com.applife.integration;

import com.applife.base.AbstractContainerBaseTest;
import com.applife.model.Employee;
import com.applife.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.applife.controller.EmployeeControllerTests.API_EMPLOYEES_PATH;
import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }


    @DisplayName("Employee Controller createEmployee integration test")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();


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

    @DisplayName("Employee Controller getAllEmployees integration test ")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {

        // given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Furkan").lastName("Uzun").email("furkan@hotmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("deneme").lastName("deneme").email("deneme@hotmail.com").build());
        employeeRepository.saveAll(listOfEmployees);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_EMPLOYEES_PATH));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @DisplayName("Employee Controller getEmployeeById (positive) integration test ")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();

        employeeRepository.save(employee);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_EMPLOYEES_PATH + "/{id}", employee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("Employee Controller getEmployeeById (negative) integration test ")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_EMPLOYEES_PATH + "/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("Employee Controller updateEmployee (positive) integration test ")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {

        // given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("deneme")
                .lastName("deneme")
                .email("deneme@hotmail.com")
                .build();
        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(put(API_EMPLOYEES_PATH + "/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                // Burada body gonderiyoruz
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("Employee Controller updateEmployee (negative) integration test ")
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

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(put(API_EMPLOYEES_PATH + "/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("Employee Controller deleteEmployee integration test ")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

        // given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(delete(API_EMPLOYEES_PATH + "/{id}", savedEmployee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
