import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AdminPage {

    // ✅ Correctly named inner class (renamed from AdminPage to Admin)
    public static class Admin {
        private final Integer adminId;
        private final String name;
        private final String email;
        private final String password;

        public Admin(Integer adminId, String name, String email, String password) {
            this.adminId = adminId;
            this.name = name;
            this.email = email;
            this.password = password;
        }

        public Integer getAdminId() { return adminId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    // ✅ Main method for launching from this class directly (optional)
    public static void main(String[] args) {
        javafx.application.Application.launch(DummyLauncher.class);
    }

    // ✅ Display method
    public void start(Stage stage) {
        TableView<Admin> table = new TableView<>();

        TableColumn<Admin, Integer> idCol = new TableColumn<>("Admin ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getAdminId()).asObject());

        TableColumn<Admin, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));

        TableColumn<Admin, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));

        TableColumn<Admin, String> passCol = new TableColumn<>("Password");
        passCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPassword()));

        table.getColumns().addAll(idCol, nameCol, emailCol, passCol);

        ObservableList<Admin> data = FXCollections.observableArrayList(
            new Admin(1, "Alice Johnson", "alice@company.com", "alice123"),
            new Admin(2, "Bob Smith", "bob@company.com", "bob123"),
            new Admin(3, "Charlie Brown", "charlie.brown@company.com", "charlie123"),
            new Admin(4, "Diana Prince", "diana.prince@company.com", "diana123")
        );
        table.setItems(data);

        Button create = new Button("Create");
        Button update = new Button("Update");
        Button delete = new Button("Delete");
        Button view = new Button("View");
        Button back = new Button("Back");

        // 🔄 Replace with your real dashboard navigation logic
        back.setOnAction(e -> {
            System.out.println("Back to Dashboard"); // placeholder
            // new DashboardPage().start(stage); // Uncomment if you have this class
        });

        HBox buttonBox = new HBox(10, create, update, delete, view, back);
        buttonBox.setStyle("-fx-padding: 10; -fx-alignment: center;");

        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(buttonBox);

        stage.setTitle("Admin Page");
        stage.setScene(new Scene(layout, 700, 350));
        stage.show();
    }
}
