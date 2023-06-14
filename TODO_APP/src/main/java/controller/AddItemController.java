package controller;

import animations.Shaker;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.todo_app.Main;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddItemController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button logOutButton;

    @FXML
    private ImageView addButton;

    @FXML
    private Label notTaskLabel;

    @FXML
    void initialize() {

        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Shaker buttonShaker = new Shaker(addButton);
            buttonShaker.shake();

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), addButton);
            FadeTransition labelTransition = new FadeTransition(Duration.millis(1000), notTaskLabel);

            System.out.println("Kliknięto");
            addButton.relocate(0,20);
            notTaskLabel.relocate(0,85);
            addButton.setOpacity(0);
            notTaskLabel.setOpacity(0);

            fadeTransition.setFromValue(1f);
            fadeTransition.setToValue(0f);
            fadeTransition.setCycleCount(1);
            fadeTransition.setAutoReverse(false);
            fadeTransition.play();

            labelTransition.setFromValue(1f);
            labelTransition.setToValue(0f);
            labelTransition.setCycleCount(1);
            labelTransition.setAutoReverse(false);
            labelTransition.play();

            try {
                AnchorPane formPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("addItemForm.fxml")));
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
