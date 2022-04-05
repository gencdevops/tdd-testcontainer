package com.applife.repository;

import com.applife.base.AbstractContainerBaseTest;
import com.applife.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIntegrationTests extends AbstractContainerBaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {

        employeeRepository.deleteAll();

        employee = Employee.builder()
                .firstName("Furkan")
                .lastName("Uzun")
                .email("frknuzn34@hotmail.com")
                .build();
    }

    //    JUnit test for save employee operation
    @DisplayName("Save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        //        given - precondition or setup

        //        when - action or behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        //        then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //    JUnit test for
    @DisplayName("Find All Employee operation")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {

        //given - precondition or setup

        Employee employee1 = Employee.builder()
                .firstName("deneme")
                .lastName("deneme")
                .email("deneme@hotmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        //when - action or behaviour that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //    JUnit test for
    @DisplayName("Get Employee by id")
    @Test
    public void givenEmployee_whenFindById_thenReturnEmployeeObject() {

        // given - precondition or setup

        employeeRepository.save(employee);

        // when - action or behaviour that we are going test
        Employee employeeDb = employeeRepository.findById(employee.getId()).get();

        // then - verify the output
        assertThat(employeeDb).isNotNull();
    }


    //    JUnit test for get employee by email operation
    @DisplayName("Get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenEmployeeObject() {

        // given - precondition or setup

        employeeRepository.save(employee);

        // when - action or behaviour that we are going test
        Employee employeeDb = employeeRepository.findByEmail(employee.getEmail()).get();

        // then - verify the output
        assertThat(employeeDb).isNotNull();
    }

    //    JUnit test for
    @DisplayName("Update Employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // given - precondition or setup

        employeeRepository.save(employee);

        // when - action or behaviour that we are going test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("frknuzn34@gmail.com");
        savedEmployee.setLastName("Uzun1");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify the output

        assertThat(updatedEmployee.getEmail()).isEqualTo("frknuzn34@gmail.com");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Uzun1");

    }

    @DisplayName("Delete by id Employee Operation")
    @Test
    public void givenEmployeeObject_whenDeleteById_thenRemoveEmployee() {

        // given - precondition or setup

        employeeRepository.save(employee);
        // when - action or behaviour that we are going test

        employeeRepository.deleteById(employee.getId());

        Optional<Employee> deletedEmployee = employeeRepository.findById(employee.getId());

        // then - verify the output
        assertThat(deletedEmployee).isEmpty();
    }

    //    JUnit test for
    @DisplayName("Custom query using JPQL")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {

        // given - precondition or setup

        employeeRepository.save(employee);

        String firstName = "Furkan";
        String lastName = "Uzun";

        // when - action or behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Custom query using JPQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamed_thenReturnEmployeeObject() {

        // given - precondition or setup

        employeeRepository.save(employee);

        String firstName = "Furkan";
        String lastName = "Uzun";

        // when - action or behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLNamed(firstName, lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }


    //    JUnit test for
    @DisplayName("Custom query using native SQL with index(?)")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {

        // given - precondition or setup

        employeeRepository.save(employee);

        String firstName = "Furkan";
        String lastName = "Uzun";

        // when - action or behaviour that we are going test

        Employee savedEmployee = employeeRepository.findByNativeSQL(firstName, lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Custom query using native SQL with params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLWithParams_thenReturnEmployeeObject() {

        // given - precondition or setup

        employeeRepository.save(employee);

        String firstName = "Furkan";
        String lastName = "Uzun";

        // when - action or behaviour that we are going test

        Employee savedEmployee = employeeRepository.findByNativeSQLParams(firstName, lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }
}
