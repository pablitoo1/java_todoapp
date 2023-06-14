package controller;

import Database.DatabaseHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.todo_app.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class viewTasksController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private ImageView backButton;

    @FXML
    void initialize() throws SQLException {

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


        LoginController loginController = new LoginController();
        DatabaseHandler databaseHandler = new DatabaseHandler();

        String userid = loginController.getCurrentId();

        //ilość zadań aktualnie zalogowanego użytkownika
        int tasks_number = Integer.parseInt(databaseHandler.checkTasks(userid));

        if(tasks_number == 0){
            Label no_tasks = new Label("Aktualnie nie masz zadań do wykonania!");

            Font noTasksFont = new Font("System", 18);

            no_tasks.setStyle("-fx-text-fill: #545454;");
            no_tasks.setFont(noTasksFont);

            mainPane.getChildren().add(no_tasks);

            AnchorPane.setTopAnchor(no_tasks, 160.0);
            AnchorPane.setLeftAnchor(no_tasks, 239.0);
        }

        //pobranie informacji o zadaniach zalogowanego użytkownika
        ResultSet taskRow = databaseHandler.getTasks(userid);
        String opis = null, nazwa = null;
        Date data;
        int task;

        for(int i=0; i<tasks_number; i++){

            taskRow.next();

            opis = taskRow.getString("description");
            nazwa = taskRow.getString("taskname");
            data = taskRow.getDate("datecreated");
            task = taskRow.getInt("taskid");

            Image edit = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/assets/icon_edit.png")));
            Image delete = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/assets/icon_delete.png")));
            Image emptyStar = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/assets/icon_emptystar2.png")));
            Image fillStar = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/assets/icon_fillstar2.png")));


            ImageView editImage = new ImageView(edit);
            ImageView deleteImage = new ImageView(delete);
            ImageView starImage = new ImageView(emptyStar);

            editImage.setFitWidth(25);
            editImage.setFitHeight(25);
            editImage.setPreserveRatio(true);
            editImage.setCursor(Cursor.HAND);
            deleteImage.setFitWidth(25);
            deleteImage.setFitHeight(25);
            deleteImage.setPreserveRatio(true);
            deleteImage.setCursor(Cursor.HAND);
            starImage.setFitWidth(25);
            starImage.setFitHeight(25);
            starImage.setPreserveRatio(true);
            starImage.setCursor(Cursor.HAND);

            Label tytul = new Label(nazwa);
            TextArea desc = new TextArea();
            Label dataUtw = new Label("Data utworzenia: " + data);


            Font fontTitle = new Font("Calibri", 16);
            tytul.setStyle("-fx-font-weight: bold;");
            tytul.setFont(fontTitle);

            dataUtw.setStyle("-fx-text-fill: #4054e8;");

            Font fontDesc = new Font("Calibri", 13);
            Border border = new Border(new BorderStroke(Color.DARKSLATEBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));
            Border borderGold = new Border(new BorderStroke(Color.GOLD, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.5)));
            desc.setPrefWidth(180);
            desc.setPrefHeight(70);
            desc.setText(opis);
            desc.setEditable(false);
            desc.setCache(false);
            desc.setFont(fontDesc);
            desc.setWrapText(true);
            desc.setCursor(Cursor.DEFAULT);
            desc.setFocusTraversable(false);

            mainPane.getChildren().add(desc);
            mainPane.getChildren().add(tytul);
            mainPane.getChildren().add(dataUtw);
            mainPane.getChildren().add(editImage);
            mainPane.getChildren().add(deleteImage);
            mainPane.getChildren().add(starImage);

            int tasknumber = task;
            boolean valueImportant;

            if(databaseHandler.checkImportant(tasknumber) == 0){
                //jeśli zadanie nie jest ważne
                valueImportant = true;
                starImage.setImage(emptyStar);
                desc.setBorder(border);
            }else{
                //jeśli zadanie jest ważne
                valueImportant = false;
                starImage.setImage(fillStar);
                desc.setBorder(borderGold);
            }

            //Obiekt AtomicBoolean ma specjalne metody, takie jak get(), set()
            AtomicBoolean isEmptyStar = new AtomicBoolean(valueImportant);

            starImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (isEmptyStar.get()) {
                    //jeśli zadanie nie jest ważne to ustaw na ważne
                    starImage.setImage(fillStar);
                    desc.setBorder(borderGold);
                    isEmptyStar.set(false);
                    databaseHandler.setImportant(true, tasknumber);
                } else {
                    //jeśli zadanie jest ważne to ustaw na nieważne
                    starImage.setImage(emptyStar);
                    desc.setBorder(border);
                    isEmptyStar.set(true);
                    databaseHandler.setImportant(false, tasknumber);
                }
            });

            deleteImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                //usuwanie zadania
                databaseHandler.deleteTask(tasknumber);
                AnchorPane formPane = null;
                try {
                    formPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("viewTasks.fxml")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mainPane.getChildren().setAll(formPane);
            });

            editImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                desc.setEditable(true);
                desc.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.ENTER) {
                            desc.setEditable(false);
                            //usuwanie znaku entera z opisu
                            String enterDeleted = desc.getText();
                            desc.deleteText(enterDeleted.length() - 1, enterDeleted.length());
                            String newDescription = desc.getText();
                            databaseHandler.editTask(newDescription, tasknumber);
                        }
                    }
                });
            });

            if(i<=2){
                //1 kolumna wyświetlanych zadań
                AnchorPane.setTopAnchor(desc, 30.0 + i * 120);
                AnchorPane.setLeftAnchor(desc, 20.0);

                AnchorPane.setTopAnchor(tytul, 10.0 + i * 120);
                AnchorPane.setLeftAnchor(tytul, 20.0);

                AnchorPane.setTopAnchor(editImage, 25.0 + i * 120);
                AnchorPane.setLeftAnchor(editImage, 210.0);

                AnchorPane.setTopAnchor(deleteImage, 55.0 + i * 120);
                AnchorPane.setLeftAnchor(deleteImage, 210.0);

                AnchorPane.setTopAnchor(starImage, 85.0 + i * 120);
                AnchorPane.setLeftAnchor(starImage, 210.0);

                AnchorPane.setTopAnchor(dataUtw, 102.0 + i * 120);
                AnchorPane.setLeftAnchor(dataUtw, 20.0);
            } else if (i>2 && i<=5) {
                //2 kolumna wyświetlanych zadań
                AnchorPane.setTopAnchor(desc, 30.0 + (i-3) * 120);
                AnchorPane.setLeftAnchor(desc, 270.0);

                AnchorPane.setTopAnchor(tytul, 10.0 + (i-3) * 120);
                AnchorPane.setLeftAnchor(tytul, 270.0);

                AnchorPane.setTopAnchor(editImage, 25.0 + (i-3) * 120);
                AnchorPane.setLeftAnchor(editImage, 460.0);

                AnchorPane.setTopAnchor(deleteImage, 55.0 + (i-3) * 120);
                AnchorPane.setLeftAnchor(deleteImage, 460.0);

                AnchorPane.setTopAnchor(starImage, 85.0 + (i-3) * 120);
                AnchorPane.setLeftAnchor(starImage, 460.0);

                AnchorPane.setTopAnchor(dataUtw, 102.0 + (i-3) * 120);
                AnchorPane.setLeftAnchor(dataUtw, 270.0);
            } else if (i>5) {
                //3 kolumna wyświetlanych zadań
                AnchorPane.setTopAnchor(desc, 30.0 + (i-6) * 120);
                AnchorPane.setLeftAnchor(desc, 510.0);

                AnchorPane.setTopAnchor(tytul, 10.0 + (i-6) * 120);
                AnchorPane.setLeftAnchor(tytul, 510.0);

                AnchorPane.setTopAnchor(editImage, 25.0 + (i-6) * 120);
                AnchorPane.setLeftAnchor(editImage, 700.0);

                AnchorPane.setTopAnchor(deleteImage, 55.0 + (i-6) * 120);
                AnchorPane.setLeftAnchor(deleteImage, 700.0);

                AnchorPane.setTopAnchor(starImage, 85.0 + (i-6) * 120);
                AnchorPane.setLeftAnchor(starImage, 700.0);

                AnchorPane.setTopAnchor(dataUtw, 102.0 + (i-6) * 120);
                AnchorPane.setLeftAnchor(dataUtw, 510.0);
            }
        }
    }
}
