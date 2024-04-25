import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Socket conn;
    private static ServerSocket server;
    protected Connection connection = null;
    protected String uri = "jdbc:sqlite:userDatabase.db";


    public static void main(String [] args) {
        try{
            Server s = new Server();
            server = new ServerSocket(5000);
            while(true){
                System.out.println("waiting for client to connect..");
                conn = server.accept();
                System.out.println("connected to client " + conn);
                s.readClient();
            }

        } catch (IOException e) {
            try {
                server.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    //reads information from client sends to method to be processed then sent to another method to be sent back to client
    public void readClient(){
        try {
            while(true){
                BufferedReader clientMessage = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                System.out.println("waiting for client to send data");
                String msg = clientMessage.readLine();
                System.out.println(msg);
                System.out.println("received: " + msg);
                //sendToClient("Got message");
                /*
                System.out.println("received: " + msg);
                String output = processesInput(msg);
                sendToClient(output);

                 */
            }

        }catch (IOException e){
            try {
                conn.close();
            }catch (IOException ex){
                System.out.println("Could not close server");
            }
        }
    }



    //Sends info back to client
    public void sendToClient(String response){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(conn.getOutputStream());
            writer.println(response);
            writer.flush();

        } catch (IOException e) {
            System.out.println("Error with client ");
        }
    }
}


/*
public class Server {
    protected Connection connection = null;
    protected String uri = "jdbc:sqlite:userDatabase.db";
    private static Socket conn;
    private static ServerSocket serverSocket;


    public static void main(String[] args) {
        new Server().startServer();
    }

    public void startServer() {
        try {
            System.out.println("server> waiting for client to connect");
            serverSocket = new ServerSocket(5000);
            while (true) {
                System.out.println("waiting for client to connect..");
                conn = serverSocket.accept();
                System.out.println("server> connected to Socket: " + conn);
                readClient();
                // another try catch to connect to db

                /*
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
        } catch (IOException ex) {
            try {
                conn.close();
            }catch (IOException exp){
                System.out.println("Could not close server");
            }
        }
    }

    public void readClient(){
        try {
            while(true){
                BufferedReader clientMessage = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                System.out.println("waiting for client to send data");
                String msg = clientMessage.readLine();
                System.out.println(msg);

                //.out.println("received: " + msg);
                //String output = processesInput(msg);
                //sendToClient(output);
            }

        }catch (IOException e){
            try {
                conn.close();
            }catch (IOException ex){
                System.out.println("Could not close server");
            }
        }
    }



    public void createUser(String username, String password){
        try{
            connection = DriverManager.getConnection(uri);
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
            System.out.println("error check");
            ex.printStackTrace();
        }
    }

}

*/