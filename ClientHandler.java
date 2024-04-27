import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private Account account;
    private PlayGame game;
    private Outcome outcome;
    private Leaderboard leaderboard;


    public ClientHandler(Socket socket){
        this.clientSocket = socket;
        this.account = new Account();
        this.game = new PlayGame();
        this.outcome = new Outcome();
        this.leaderboard = new Leaderboard();
    }

    @Override
    public void run() {
        readClient();
    }

    //reads information from client sends to method to be processed then sent to another method to be sent back to client
    public void readClient(){
        try {
            while(true){
                BufferedReader clientMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("waiting for client to send data");
                String msg = clientMessage.readLine();
                System.out.println(msg);
                System.out.println("received: " + msg);
                processInput(msg);
            }

        }catch (IOException e){
            try {
                clientSocket.close();
            }catch (IOException ex){
                System.out.println("Could not close server");
            }
        }
    }


    public void sendToClient(String response){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(clientSocket.getOutputStream());
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
            String betOutcome = game.flipCoin();
            int results = outcome.getResults(betType, betOutcome);
            double currentBalance = account.getAccountBalance();
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

         */

        //String output = arrayList.toString();
        System.out.println(leaders);
        if(!leaders.isEmpty()){
            sendToClient(leaders);
        }
    }
}
