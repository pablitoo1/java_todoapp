package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.todo_app.Main;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddItemWithExistingController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView addNewButton;

    @FXML
    private ImageView showTasksButton;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button logOutButton;

    @FXML
    void initialize() {

        addNewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                AnchorPane formPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("addItemForm.fxml")));
                rootPane.getChildren().setAll(formPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        showTasksButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                AnchorPane formPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("viewTasks.fxml")));
                rootPane.getChildren().setAll(formPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        logOutButton.setOnAction(actionEvent -> {
            logOutButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("login.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/assets/icon_main.png"))));
            stage.setTitle("TODO APP");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        });

    }
}
