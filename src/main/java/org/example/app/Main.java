package org.example.app;

import org.example.app.DAO.EmployeeDAO;
import org.example.app.database.DatabaseConnector;
import org.example.app.entity.Employee;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        DatabaseConnector dbConnector = new DatabaseConnector();

        Connection connection = dbConnector.connect();

        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS employees(
                    id INTEGER NOT NULL AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    age INTEGER NOT NULL,
                    position VARCHAR(255) NOT NULL,
                    salary DECIMAL (10, 2) NOT NULL,
                    PRIMARY KEY (id)
                );
                """;

        dbConnector.executeQuery(connection, createTableSQL);

        EmployeeDAO.create(connection, new Employee("Alex", 18, "manager", 1000.0));

        Optional<List<Employee>> employeesOptional = EmployeeDAO.read(connection);
        if (employeesOptional.isPresent()) {
            List<Employee> employees = employeesOptional.get();
            for (Employee employee : employees) {
                System.out.println(employee);
            }
        } else {
            System.out.println("Неможливо отримати дані про співробітників");
        }

        EmployeeDAO.update(connection, new Employee(1L, "Oleksii", 20, "administrator", 2000.0));

        EmployeeDAO.delete(connection, 1L);

        dbConnector.closeConnection(connection);
    }
}
