package com.example.demo.controller;

import com.example.demo.model.Person;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private List<Person> persons = new ArrayList<>();


    // GET запрос для получения списка всех людей
    @GetMapping
    public List<Person> getAllPersons() {
        return persons;
    }

    // POST запрос для добавления нового человека
    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        persons.add(person);
        return new ResponseEntity<>("Person added successfully", HttpStatus.ACCEPTED);
    }
}