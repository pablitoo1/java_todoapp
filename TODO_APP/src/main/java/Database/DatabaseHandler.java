package Database;

import model.Task;
import model.User;

import java.sql.*;


public class DatabaseHandler {

    //obiekt reprezentujący połączenie z bazą danych
    Connection dbConnection;

    Configs configs = new Configs();

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://"+ configs.dbHost + ":"
                + configs.dbPort + "/"
                + configs.dbName;

        //Klasa ta jest sterownikiem JDBC (Java Database Connectivity) dla bazy danych MySQL
        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString,configs.dbUser, configs.dbPassword);

        return dbConnection;
    }

    //Zapisywanie nowych użytkowników
    public void signUpUser(User user){
        String insert = "INSERT INTO "+Const.USERS_TABLE+"("+Const.USERS_FIRSTNAME+","
                +Const.USERS_LASTNAME+","+Const.USERS_USERNAME+","+Const.USERS_PASSWORD
                +") VALUES(?,?,?,?)";

        try {
            //obiekt PreparedStatement jest używany do wykonania zapytania SQL z parametrami.
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);

            //Ustawienie wartości parametrów w obiekcie PreparedStatement
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUserName());
            preparedStatement.setString(4, user.getPassword());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Obsługa przycisku logowania
    public ResultSet getUser(User user){

        //zmienna resultSet  będzie przechowywać wynik zapytania SQL jako zbiór wyników
        ResultSet resultSet = null;

        if(!user.getUserName().equals("") || !user.getPassword().equals("")){
            //sprawdzanie, czy wprowadzone dane się zgadzają z tymi w bazie
            String query = "SELECT * FROM " + Const.USERS_TABLE + " WHERE " + Const.USERS_USERNAME + "=? AND "
                    + Const.USERS_PASSWORD + "=?";

            try {
                PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, user.getPassword());

                resultSet = preparedStatement.executeQuery();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else{
            return null;
        }

        return resultSet;
    }

    //dodanie nowego zadania
    public void addTask(Task task){

        String insert = "INSERT INTO "+Const.TASKS_TABLE+"("+Const.TASKS_USERID+","
                +Const.TASKS_DATE+","+Const.TASKS_DESCRIPTION+","+Const.TASKS_NAME
                +", isImportant) VALUES(?,?,?,?,0)";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);

            preparedStatement.setString(1, task.getUserid());
            preparedStatement.setString(2, String.valueOf(task.getDatecreated()));
            preparedStatement.setString(3, task.getDescription());
            preparedStatement.setString(4, task.getTask());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //sprawdzanie liczby zadań aktualnie zalogowanego użytkownika
    public String checkTasks(String currentid){
        String numberOfTasks = null;

        //sprawdzenie, czy zalogowany użytkownik ma już jakieś zadania
        String query = "SELECT count(taskid) FROM " + Const.TASKS_TABLE + " WHERE " +
                Const.TASKS_USERID + " LIKE ?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);

            preparedStatement.setString(1, currentid);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                //index kolumny 1 bo zapytanie count zwraca jedną wartość posumowanych zadań
                numberOfTasks = resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return numberOfTasks;
    }


    //pobranie z bazy zadań dla aktualnie zalogowanego użytkownika
    public ResultSet getTasks(String currentid){

        ResultSet resultSet = null;

        String query = "SELECT * FROM " + Const.TASKS_TABLE + " WHERE " + Const.TASKS_USERID + " LIKE ?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);

            preparedStatement.setString(1, currentid);

            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return resultSet;
    }

    //edycja pola isImportant w bazie danych na podstawie zaznaczenia (bądź nie) gwiazdki
    public void setImportant(boolean isImportant, int currentTask){
        int important;

        if(isImportant){
            important = 1;
        }else{
            important = 0;
        }

        String update = "UPDATE " +Const.TASKS_TABLE + " SET isImportant=? WHERE taskid LIKE ?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);

            preparedStatement.setInt(1, important);
            preparedStatement.setInt(2, currentTask);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //sprawdzanie stanu isImportant w bazie
    public int checkImportant(int currentTask){

        int importantValue = 0;

        String query = "SELECT isImportant FROM " + Const.TASKS_TABLE + " WHERE taskid LIKE ?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);

            preparedStatement.setInt(1, currentTask);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                importantValue = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return importantValue;
    }

    //usuwanie konkretnego zadania z bazy
    public void deleteTask(int currentTask){

        String delete = "DELETE FROM "+ Const.TASKS_TABLE + " WHERE taskid LIKE ?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(delete);

            preparedStatement.setInt(1, currentTask);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //edycja konkretnego zadania
    public void editTask(String newDescription, int currentTask){

        String edit = "UPDATE " +Const.TASKS_TABLE + " SET " + Const.TASKS_DESCRIPTION +"=? WHERE taskid LIKE ?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(edit);

            preparedStatement.setString(1, newDescription);
            preparedStatement.setInt(2, currentTask);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}