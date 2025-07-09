import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class HRManagementApp extends Application {

    private final String URL = "jdbc:mysql://localhost:3306/hrdb";
    private final String USER = "root";
    private final String PASSWORD = "";

    // Employee UI components
    private TableView<EmployeeData> employeeTable = new TableView<>();
    private ObservableList<EmployeeData> employeeList = FXCollections.observableArrayList();

    private TextField idField = new TextField();
    private TextField nameField = new TextField();
    private TextField emailField = new TextField();
    private TextField roleField = new TextField();
    private TextField salaryField = new TextField();

    // Admin UI components
    private TableView<AdminData> adminTable = new TableView<>();
    private ObservableList<AdminData> adminList = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab empTab = new Tab("Employees", createEmployeePane());
        Tab adminTab = new Tab("Admins", createAdminPane());

        tabPane.getTabs().addAll(empTab, adminTab);

        stage.setScene(new Scene(tabPane, 900, 550));
        stage.setTitle("HR Management System");
        stage.show();

        loadEmployeeData();
        loadAdminData();
    }

    private VBox createEmployeePane() {
        TableColumn<EmployeeData, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()).asObject());

        TableColumn<EmployeeData, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        TableColumn<EmployeeData, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));

        TableColumn<EmployeeData, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRole()));

        TableColumn<EmployeeData, Double> salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getSalary()).asObject());

        employeeTable.getColumns().setAll(idCol, nameCol, emailCol, roleCol, salaryCol);

        idField.setPromptText("ID");
        nameField.setPromptText("Name");
        emailField.setPromptText("Email");
        roleField.setPromptText("Role");
        salaryField.setPromptText("Salary");

        HBox inputBox = new HBox(10, idField, nameField, emailField, roleField, salaryField);

        Button view = new Button("View");
        Button insert = new Button("Insert");
        Button update = new Button("Update");
        Button delete = new Button("Delete");

        view.setOnAction(e -> loadEmployeeData());
        insert.setOnAction(e -> insertEmployee());
        update.setOnAction(e -> updateEmployee());
        delete.setOnAction(e -> deleteEmployee());

        HBox controls = new HBox(10, view, insert, update, delete);
        Label footer = new Label("Developer: Niraj Bhandari | ID: 23093760 | Date: " + java.time.LocalDate.now());

        VBox layout = new VBox(15, employeeTable, inputBox, controls, footer);
        layout.setStyle("-fx-padding: 20");
        return layout;
    }

    private VBox createAdminPane() {
        TableColumn<AdminData, Integer> idCol = new TableColumn<>("Admin ID");
        idCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()).asObject());

        TableColumn<AdminData, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        TableColumn<AdminData, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));

        TableColumn<AdminData, String> passCol = new TableColumn<>("Password");
        passCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPassword()));

        adminTable.getColumns().setAll(idCol, nameCol, emailCol, passCol);
        adminTable.setItems(adminList);

        Button refresh = new Button("Refresh");
        refresh.setOnAction(e -> loadAdminData());

        VBox layout = new VBox(10, adminTable, refresh);
        layout.setStyle("-fx-padding: 20");
        return layout;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void loadEmployeeData() {
        employeeList.clear();
        String sql = "SELECT * FROM employee";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employeeList.add(new EmployeeData(
                        rs.getInt("emp_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getDouble("salary")
                ));
            }
            employeeTable.setItems(employeeList);
        } catch (SQLException e) {
            showAlert("Load Error", e.getMessage());
        }
    }

    private void insertEmployee() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String role = roleField.getText().trim();
        String salaryStr = salaryField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || role.isEmpty() || salaryStr.isEmpty()) {
            showAlert("Input Error", "All fields except ID are required");
            return;
        }

        try {
            double salary = Double.parseDouble(salaryStr);
            String sql = "INSERT INTO employee (name, email, role, salary) VALUES (?, ?, ?, ?)";

            try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, role);
                stmt.setDouble(4, salary);
                stmt.executeUpdate();
                showAlert("Success", "Employee inserted");
                clearFields();
                loadEmployeeData();
            }
        } catch (Exception e) {
            showAlert("Insert Error", e.getMessage());
        }
    }

    private void updateEmployee() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleField.getText().trim();
            double salary = Double.parseDouble(salaryField.getText().trim());

            String sql = "UPDATE employee SET name=?, email=?, role=?, salary=? WHERE emp_id=?";
            try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, role);
                stmt.setDouble(4, salary);
                stmt.setInt(5, id);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    showAlert("Success", "Employee updated");
                    clearFields();
                    loadEmployeeData();
                } else {
                    showAlert("Error", "Employee not found");
                }
            }
        } catch (Exception e) {
            showAlert("Update Error", e.getMessage());
        }
    }

    private void deleteEmployee() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String sql = "DELETE FROM employee WHERE emp_id=?";
            try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    showAlert("Success", "Employee deleted");
                    clearFields();
                    loadEmployeeData();
                } else {
                    showAlert("Error", "No such employee");
                }
            }
        } catch (Exception e) {
            showAlert("Delete Error", e.getMessage());
        }
    }

    private void loadAdminData() {
        adminList.clear();
        String sql = "SELECT * FROM admin";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                adminList.add(new AdminData(
                        rs.getInt("admin_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            showAlert("Admin Load Error", e.getMessage());
        }
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        emailField.clear();
        roleField.clear();
        salaryField.clear();
    }

    