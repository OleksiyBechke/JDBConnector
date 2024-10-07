package org.example.app.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    public static Connection connect() {

        Properties props = new Properties();
        Connection connection = null;

        try {
            props.load(DatabaseConnector.class.getResourceAsStream("/db/jdbc.properties"));
            connection = DriverManager.getConnection(
                    props.getProperty("dbDriver") + props.getProperty("dbName"),
                    props.getProperty("username"), props.getProperty("password"));
            System.out.println("Підключення до бази даних успішне");
        } catch (SQLException | IOException e) {
            System.out.println("Помилка підключення: " + e.getMessage());
        }
        return connection;
    }

    public static void executeQuery(Connection connection, String sqlQuery) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQuery);
            System.out.println("Запит виконано успішно");
        } catch (SQLException e) {
            System.out.println("Помилка виконання запиту: " + e.getMessage());
        }
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("З'єднання з базою даних закрито");
            } catch (SQLException e) {
                System.out.println("Помилка закриття з'єднання: " + e.getMessage());
            }
        }
    }
}
