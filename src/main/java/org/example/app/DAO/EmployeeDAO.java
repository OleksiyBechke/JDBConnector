package org.example.app.DAO;

import org.example.app.entity.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAO {
    private final static String TABLE_EMPLOYEES = "employees";

    public static void create(Connection connection, Employee employee) {
        String sql = "INSERT INTO " + TABLE_EMPLOYEES +
                " (name, age, position, salary) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, Integer.toString(employee.getAge()));
            pstmt.setString(3, employee.getPosition());
            pstmt.setString(4, Double.toString(employee.getSalary()));
            pstmt.executeUpdate();
            System.out.println("Нового співробітника додано успішно");
        } catch (SQLException e) {
            System.out.println("Помилка створення нового співробітника: " + e.getMessage());
        }
    }

    public static Optional<List<Employee>> read(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            List<Employee> list = new ArrayList<>();
            String sql = "SELECT id, name, age, position, salary FROM "
                    + TABLE_EMPLOYEES;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Employee(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getInt("age"),
                                rs.getString("position"),
                                rs.getDouble("salary")
                        )
                );
            }
            System.out.println("Дані про співробітників прочитано успішно");
            return Optional.of(list);
        } catch (SQLException e) {
            System.out.println("Помилка читання даних співробітників: " + e.getMessage());
            return Optional.empty();
        }
    }

    public static void update(Connection connection, Employee employee) {
        if (!isIdExists(connection, employee.getId())) {
            System.out.println("Немає даних про співробітника з id=" + employee.getId());
        } else {
            String sql = "UPDATE " + TABLE_EMPLOYEES +
                    " SET name = ?, age = ?, position = ?, salary = ?" +
                    " WHERE id = ?";
            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.setString(1, employee.getName());
                pst.setInt(2, employee.getAge());
                pst.setString(3, employee.getPosition());
                pst.setDouble(4, employee.getSalary());
                pst.setLong(5, employee.getId());
                pst.executeUpdate();
                System.out.println("Дані співробітника з id=" + employee.getId() + " оновлено успішно");
            } catch (SQLException e) {
                System.out.println("Помилка оновлення даних співробітника з id=" + employee.getId() + ": " + e.getMessage());
            }
        }
    }

    public static void delete(Connection connection, Long id) {
        if (!isIdExists(connection, id)) {
            System.out.println("Немає даних про співробітника з id=" + id);
        } else {
            String sql = "DELETE FROM " + TABLE_EMPLOYEES +
                    " WHERE id = ?";
            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.setLong(1, id);
                pst.executeUpdate();
                System.out.println("Співробітника з id=" + id + " видалено успішно");
            } catch (SQLException e) {
                System.out.println("Помилка видалення співробітника з id=" + id + ": " + e.getMessage());
            }
        }
    }

    private static boolean isIdExists(Connection connection, Long id) {
        String sql = "SELECT COUNT(id) FROM " + TABLE_EMPLOYEES +
                " WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }
}
