package com.example.societymanager.service;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class Retrieve {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/society_management";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "pgadmin4";

    public List<Person> queryUser() throws SQLException {
        List<Person> persons = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT name, contact_no, wing, flat_no FROM person";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Person person = new Person();
                    person.setName(resultSet.getString("name"));
                    person.setContactNo(resultSet.getString("contact_no"));
                    person.setWing(resultSet.getString("wing"));
                    person.setFlatNo(resultSet.getString("flat_no"));
                    persons.add(person);
                }
            }
        }
        return persons;
    }
}
