import java.awt.image.PackedColorModel;
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
    Account account;
    PlayGame game;
    Outcome outcome;


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

    public Server(){
        account = new Account();
        game = new PlayGame();
        outcome = new Outcome();
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
                processInput(msg);
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

    public void processInput(String msgFromClient){
        String[] splitMsg = msgFromClient.split(" ");
        System.out.println(splitMsg[0]);

        if(splitMsg[0].equals("login")){
            if(splitMsg.length < 3){
                sendToClient("-1");
            }
            else{
                String username = splitMsg[1];
                String password = splitMsg[2];
                int loggedin = account.login(username, password);
                System.out.println(loggedin);
                sendToClient(Integer.toString(loggedin));
            }
        }
        else if(splitMsg[0].equals("createAccount")){
            if(splitMsg.length < 3){
                sendToClient("-1");
            }
            else{
                String username = splitMsg[1];
                String password = splitMsg[2];
                int accountCreated = account.createAccount(username, password);
                System.out.println(accountCreated);
                sendToClient(Integer.toString(accountCreated));
            }
        }
        else if (splitMsg[0].equals("flipCoin")){
            if(splitMsg.length < 3){
                sendToClient("-1");
            }
            else{
                try {
                    double betAmount = Double.parseDouble(splitMsg[1]);
                    String betType = splitMsg[2];
                    String betOutcome = game.flipCoin();
                    int results = outcome.getResults(betType, betOutcome);
                    double currentBalance = account.getAccountBalance();
                    String username = account.getUsername();
                    String clientMsg;
                    if(results != -1){
                        if(results == 0){
                            betAmount = -1*betAmount;
                        }
                        currentBalance = outcome.updateBalance(betAmount, currentBalance, username);
                        account.setLocalBalance(currentBalance);
                        clientMsg = String.format("%d %s %g ", results, betOutcome, currentBalance);
                        sendToClient(clientMsg);
                    }
                    else{
                        System.out.println("Error");
                    }
                }catch(NumberFormatException ex){
                    sendToClient("-1");
                }
            }
        }
        else if (splitMsg[0].equals("rollDice")) {
            processRollDice(splitMsg[1],splitMsg[2]);
        }
    }


    public void processRollDice(String betAmount, String betType){
        try {
            double bet = Double.parseDouble(betAmount);
            String betOutcome = game.rollDice();
            int results = outcome.getResults(betType, betOutcome);
            double currentBalance = account.getAccountBalance();
            String username = account.getUsername();
            String clientMsg;
            if(results != -1){
                if(results == 0){
                    bet = -1*bet;
                }
                else if(results == 1){
                    bet = 3*bet;
                }
                currentBalance = outcome.updateBalance(bet, currentBalance, username);
                account.setLocalBalance(currentBalance);
                clientMsg = String.format("%d %s %g ", results, betOutcome, currentBalance);
                sendToClient(clientMsg);
            }
            else{
                System.out.println("Error");
            }
        }catch(NumberFormatException ex){
            sendToClient("-1");
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