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
    //private static Socket conn;
    private static ServerSocket server;
    protected Connection connection = null;
    protected String uri = "jdbc:sqlite:userDatabase.db";
    Account account;
    PlayGame game;
    Outcome outcome;
    Leaderboard leaderboard;


    public static void main(String [] args) {
        try{
            Server s = new Server();
            server = new ServerSocket(5000);
            while(true){
                System.out.println("waiting for client to connect..");
                Socket conn = server.accept();
                System.out.println("connected to client " + conn);
                ClientHandler clientSock = new ClientHandler(conn);
                new Thread(clientSock).start();
                //s.readClient();
            }

        } catch (IOException e) {
            try {
                server.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
    /*
    public Server(){
        account = new Account();
        game = new PlayGame();
        outcome = new Outcome();
        leaderboard = new Leaderboard();
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
            }

        }catch (IOException e){
            try {
                conn.close();
            }catch (IOException ex){
                System.out.println("Could not close server");
            }
        }
    }


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

        if(splitMsg.length < 3){
            if(splitMsg[0].equals("leaderboard")){
                processLeaderboard();
            }
            else{
                sendToClient("-1");
            }
        }else{
            if (splitMsg[0].equals("login")) {
                processLogin(splitMsg[1], splitMsg[2]);
            } else if (splitMsg[0].equals("createAccount")) {
                processCreateAccount(splitMsg[1], splitMsg[2]);
            } else if (splitMsg[0].equals("flipCoin")) {
                processCoinFLip(splitMsg[1], splitMsg[2]);
            } else if (splitMsg[0].equals("rollDice")) {
                processRollDice(splitMsg[1], splitMsg[2]);
            }
        }
    }

    public void processLogin(String username, String password){
        int loggedin = account.login(username, password);
        System.out.println(loggedin);
        sendToClient(Integer.toString(loggedin));
    }

    public void processCreateAccount(String username, String password){
        int accountCreated = account.createAccount(username, password);
        System.out.println(accountCreated);
        sendToClient(Integer.toString(accountCreated));
    }

    public void processCoinFLip(String betAmount, String betType){
        try {
            double bet = Double.parseDouble(betAmount);
            double currentBalance = account.getAccountBalance();
            String betOutcome = game.flipCoin();
            int results = outcome.getResults(betType, betOutcome);

            if(bet > currentBalance) {
                sendToClient("-2");
                return;
            } else if (bet < 0) {
                sendToClient("-3");
                return;
            } else if (bet != (int)bet) {
                sendToClient("-4");
                return;
            }

            String username = account.getUsername();
            String clientMsg;
            if(results != -1){
                if(results == 0){
                    bet = -1*bet;
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


    public void processRollDice(String betAmount, String betType){
        try {
            double bet = Double.parseDouble(betAmount);
            String betOutcome = game.rollDice();
            int results = outcome.getResults(betType, betOutcome);
            double currentBalance = account.getAccountBalance();

            if(bet > currentBalance) {
                sendToClient("-2");
                return;
            } else if (bet < 0) {
                sendToClient("-3");
                return;
            } else if (bet != (int)bet) {
                sendToClient("-4");
                return;
            }

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

    public void processLeaderboard(){
        System.out.println("display leaderboard");
        String leaders;
        leaders = leaderboard.getTopThree();
        /*
        for (String s : arrayList) {
            // sort arraylist(since already in arraylist, should maybe be a separate function?) then grab top 3 (probably outside of loop)
            // should reformat cmd to only get relevant information. i.e. only display username and balance (definitely wouldn't want to display password)
            System.out.println(s);
        }



        //String output = arrayList.toString();
        System.out.println(leaders);
        if(!leaders.isEmpty()){
            sendToClient(leaders);
        }
    }
    */
}
