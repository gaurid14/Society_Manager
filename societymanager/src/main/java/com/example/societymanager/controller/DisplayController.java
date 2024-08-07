package com.example.societymanager.controller;

import com.example.societymanager.service.Person;
import com.example.societymanager.service.Retrieve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DisplayController {

    @Autowired
    private Retrieve retrieve;

    @GetMapping("/person")
    public ResponseEntity<List<Person>> getAllPersons() {
        try {
            List<Person> persons = retrieve.queryUser();
            return ResponseEntity.ok(persons);
        } catch (SQLException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
