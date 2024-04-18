import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Server {
    Connection connection = null;

    public static void main(String[] args) {
        new Server().startServer();
    }

    public void startServer() {
        try {
            System.out.println("server> waiting for client to connect");
            ServerSocket serverSocket = new ServerSocket(5000);
            while (true) {
                Socket conn = serverSocket.accept();
                System.out.println("server> connected to Socket: " + conn);
                // another try catch to connect to db
                try {
                    connection = DriverManager.getConnection("jdbc:sqlite:userDatabase.db");
                    System.out.println("server> success connecting to database");
                    String cmd = "CREATE TABLE IF NOT EXISTS users (" +
                            "id INTEGER PRIMARY KEY," +
                            "username STRING," +
                            "password STRING," +
                            "balance INTEGER," +
                            "loggedIn BOOLEAN);";
                    connection.createStatement().executeUpdate(cmd);
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createUser(String username, String password){
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:userDatabase.db");
            Statement stmt = connection.createStatement();
            String insert = "INSERT INTO users (username, password, balance, loggedIn)" +
                    "VALUES (?,?,?,?);";
            PreparedStatement prepStmt = connection.prepareStatement(insert);
            //String add = "INSERT INTO cats (name) VALUES ('" + record + "');";
            prepStmt.setString(1,username);
            prepStmt.setString(2,password);
            prepStmt.setInt(3,100);
            prepStmt.setBoolean(4,false);
            prepStmt.executeUpdate();
            connection.close();
        }catch (SQLException ex){
            System.out.println("error chceck");
            ex.printStackTrace();
        }
    }
}
