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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController {
    public static String currentid;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Button loginButton;

    @FXML
    private Button loginSingUpButton;

    private DatabaseHandler databaseHandler;

    @FXML
    void initialize() {

        databaseHandler = new DatabaseHandler();

        loginButton.setOnAction(actionEvent -> {

            String loginText = loginUsername.getText().trim();
            String loginPwd = loginPassword.getText().trim();

            User user = new User();

            user.setUserName(loginText);
            user.setPassword(loginPwd);

            if(databaseHandler.getUser(user) == null){
                //obsługa niepoprawnego wprowadzenia danych
                System.out.println("Wpisz swoje dane");

                Shaker emptyUserName = new Shaker(loginUsername);
                Shaker emptyPassword = new Shaker(loginPassword);
                emptyUserName.shake();
                emptyPassword.shake();
            }else {
                ResultSet userRow = databaseHandler.getUser(user);

                int counter = 0;

                while (true){
                    try {
                        //jeżeli użytkownik istnieje ta instrukcja się wykona
                        if (!userRow.next()) break;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    counter++;
                    String name = null;
                    try {
                        //pobieranie danych o użytkowniku
                        name = userRow.getString("firstname");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        currentid = userRow.getString("userid");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Witaj " + name);
                }
                if(counter==1){
                    //jeżeli użytkownik z takim loginem i hasłem istnieje
                    showAddItemScreen();
                }else{
                    //nie istnieje
                    Shaker userNameShaker = new Shaker(loginUsername);
                    Shaker passwordShaker = new Shaker(loginPassword);
                    userNameShaker.shake();
                    passwordShaker.shake();
                }
            }
        });

        loginSingUpButton.setOnAction(actionEvent -> {

            //przejście do ekranu rejestracji
            loginSingUpButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("signup.fxml"));

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
            stage.showAndWait();

        });

    }

    private void showAddItemScreen(){
        //przejście do ekranu dodawania zadań po zalogowaniu
        loginSingUpButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        if(databaseHandler.checkTasks(currentid).equals("0")){
            loader.setLocation(Main.class.getResource("addItem.fxml"));
        }else{
            loader.setLocation(Main.class.getResource("addItemWithExisting.fxml"));
        }

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
        stage.showAndWait();
    }

    public String getCurrentId(){
        //pobieranie ID aktualnie zalogowanego użytkownika
        return currentid;
    }
}