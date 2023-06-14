package controller;

import Database.DatabaseHandler;
import animations.Shaker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.User;
import sample.todo_app.Main;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField signupFirstName;

    @FXML
    private TextField signupLastName;

    @FXML
    private TextField signupUserName;

    @FXML
    private PasswordField signupPassword;

    @FXML
    private Button signupButton;
    @FXML
    private Button loginBackButton;

    @FXML
    void initialize() {

        signupButton.setOnAction(actionEvent -> {
            if(!signupFirstName.getText().equals("") && !signupLastName.getText().equals("") &&
                    !signupUserName.getText().equals("") && !signupPassword.getText().equals("")){
                //jeśli wszystkie pola są wypełnione
                createUser();

                loginBackButton.getScene().getWindow().hide();
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
            }else {
                //jeśli jakieś pola nie są wypełnione
                if (signupFirstName.getText().equals("")){
                    Shaker fnameShaker = new Shaker(signupFirstName);
                    fnameShaker.shake();
                }
                if (signupLastName.getText().equals("")){
                    Shaker lnameShaker = new Shaker(signupLastName);
                    lnameShaker.shake();
                }
                if (signupUserName.getText().equals("")){
                    Shaker unameShaker = new Shaker(signupUserName);
                    unameShaker.shake();
                }
                if (signupPassword.getText().equals("")){
                    Shaker passShaker = new Shaker(signupPassword);
                    passShaker.shake();
                }
            }
        });

        loginBackButton.setOnAction(actionEvent -> {
            loginBackButton.getScene().getWindow().hide();
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

    private void createUser(){

        DatabaseHandler databaseHandler = new DatabaseHandler();

        String firstName = signupFirstName.getText();
        String lastName = signupLastName.getText();
        String userName = signupUserName.getText();
        String password = signupPassword.getText();

        //nowy obiekt klasy User przekazujący dane o użytkowniku
        User user = new User(firstName, lastName, userName, password);

        databaseHandler.signUpUser(user);

    }

}
