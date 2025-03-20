package com.example.demo.controller;

import com.example.demo.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // Тест для GET-запроса
    @Test
    public void testGetAllPersons() {

        ResponseEntity<List> response = restTemplate.getForEntity("/persons", List.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    // Тест для POST-запроса
    @Test
    public void testAddPerson() {

        Person person = new Person(1, "John Doe", 30);


        ResponseEntity<String> response = restTemplate.postForEntity("/persons", person, String.class);


        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Person added successfully", response.getBody());
    }
}