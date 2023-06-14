package controller;

import Database.DatabaseHandler;
import animations.Shaker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Task;
import sample.todo_app.Main;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddItemFormController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField taskField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button saveTaskButton;

    @FXML
    private ImageView backButton;

    @FXML
    void initialize() {

        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            backButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("addItemWithExisting.fxml"));

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

        saveTaskButton.setOnAction(actionEvent -> {

            //jeśli pola nie są puste to wywołaj funkcje
            if(!taskField.getText().equals("") && !descriptionField.getText().equals("")){
                createTask();
                showExistingTaskScreen();
            }else {
                if (taskField.getText().equals("")){
                    Shaker taskShaker = new Shaker(taskField);
                    taskShaker.shake();
                }
                if (descriptionField.getText().equals("")){
                    Shaker descShaker = new Shaker(descriptionField);
                    descShaker.shake();
                }
            }
        });
    }



    private void createTask(){

        DatabaseHandler databaseHandler = new DatabaseHandler();
        LoginController loginController = new LoginController();

        String userid = loginController.getCurrentId();
        String taskName = taskField.getText();
        String description = descriptionField.getText();
        LocalDate currentDate = LocalDate.now();


        //nowy obiekt klasy Task
        Task task = new Task(currentDate, description, taskName, userid);

        databaseHandler.addTask(task);

    }

    private void showExistingTaskScreen(){
        saveTaskButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("addItemWithExisting.fxml"));
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
    }
}
