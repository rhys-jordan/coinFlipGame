import java.awt.image.PackedColorModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ServerSocket server;
    protected String uri = "jdbc:sqlite:userDatabase.db";
    protected Connection connection = null;

    public static void main(String [] args) {
        Server s = new Server();
    }

    public Server(){
        try{
            server = new ServerSocket(5000);
            while(true){
                System.out.println("server> waiting for new client to connect..");
                Socket conn = server.accept();
                System.out.println("server> connected to client " + conn);
                createDatabase();
                ClientHandler clientSock = new ClientHandler(conn);
                new Thread(clientSock).start();
            }

        } catch (IOException e) {
            try {
                server.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void createDatabase() {
        try {
            connection = DriverManager.getConnection(uri);
            System.out.println("server> success connecting to database");
            String cmd = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY," +
                    "username STRING," +
                    "password STRING," +
                    "balance DOUBLE," +
                    "loggedIn BOOLEAN);";
            connection.createStatement().executeUpdate(cmd);
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
