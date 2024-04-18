import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
                    System.out.println("success connecting to database");
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
}
