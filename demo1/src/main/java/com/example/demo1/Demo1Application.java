package com.example.demo1;

import com.example.demo1.entity.Department;
import com.example.demo1.entity.Employee;
import com.example.demo1.repository.DepartmentRepository;
import com.example.demo1.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Demo1Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Demo1Application.class, args);
        EmployeeRepository employeeRepository = context.getBean(EmployeeRepository.class);
        DepartmentRepository departmentRepository = context.getBean(DepartmentRepository.class);

        Department department = Department.builder()
                .name("Java")
                .build();

        department = departmentRepository.save(department);

        Employee employee1 = Employee.builder()
                .name("Ivan")
                .position("Middle")
                .department(department)
                .build();
        employeeRepository.save(employee1);

        Employee employee2 = Employee.builder()
                .name("Daniil")
                .position("Senior")
                .department(department)
                .build();
        employeeRepository.save(employee2);

    }

}
