package com.applife.service;

import com.applife.exception.ResourceNotFoundException;
import com.applife.model.Employee;
import com.applife.repository.EmployeeRepository;
import com.applife.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    //    Testimizin bagimli oldugu yer
    @Mock
    private EmployeeRepository employeeRepository;

    //    testimizi yazacagimiz yer
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);

        employee = Employee.builder()
                .id(1L)
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();
    }


    @DisplayName("EmployeeService saveEmployee Method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or behaviour that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("EmployeeService saveEmployee Method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenReturnThrowsException() {

        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        // when - action or behaviour that we are going test
        // employee service save employee cagirildiginda ResourceNotFoundException bekledigimizi belirtiyoruz
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // Then
        // employee repositorynin save metodunun asla kullanilmadigini dogruluyoruz
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("EmployeeService getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {

        // given - precondition or setup
        Employee employee2 = Employee.builder()
                .id(1L)
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();
        // employeeRepository i employee, employee2 donecek sekilde mockluyoruz
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

        // when - action or behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("EmployeeService getAllEmployees method negative scenario")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {

        // given - precondition or setup
        Employee employee2 = Employee.builder()
                .id(1L)
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();
        // employeeRepository i bos donecek sekilde mockluyoruz
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isEmpty();

    }

    @DisplayName("EmployeeService getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {

        // given - precondition or setup
        // employeeRepository i id 1 verildiginde yukarida ki employee i donecek sekilde mockluyoruz
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when - action or behaviour that we are going test
        Employee savedEmployee = employeeService.getEmployeeById(this.employee.getId()).get();

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("EmployeeService updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("frknuzn34@gmail.com");
        employee.setFirstName("deneme");

        // when - action or behaviour that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(this.employee);

        // then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("frknuzn34@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("deneme");
    }


    @DisplayName("EmployeeService deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {

        // given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(employee.getId());

        // when - action or behaviour that we are going test
        employeeService.deleteEmployee(employee.getId());

        // then - verify the output
        // times  deleteById metodunu 1 kere cagir anlamina geliyor
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }

}
