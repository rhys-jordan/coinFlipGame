import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Random;

public class User{

    private View view;
    private Client client;
    private boolean tabsMade = false;

    public User() {
        view = new View();
        client = new Client();

        view.setAddChangeListener(new jTabListener());
        view.setLoginButtonListener(new loginButtonListener());
        view.setFlipButtonListener(new flipButtonListener());
        view.setRollDiceButtonListener(new rollDiceButtonListener());
        view.setCreateAccountButtonListener(new createAccountButtonActionListener());
    }


    public class jTabListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            // check if logged in?
            ArrayList<String> arrayList;
            view.model.clear();

            String servermsg = String.format("leaderboard ");
            client.sendToServer(servermsg);
            String leaders = client.getFromServer();
            System.out.println(leaders);
            String[] leaderList = leaders.split(" ");
            String header = String.format("%s %25s %14s ","Rank", "Username", "Balance");
            String first = String.format("1 %30s %20s ", leaderList[0], leaderList[1]);
            String second = String.format("2 %30s %20s ", leaderList[2], leaderList[3]);
            String third = String.format("3 %30s %20s ", leaderList[4], leaderList[5]);
            view.model.addElement(header);
            view.model.addElement("-----------------------------------------------------------------------------------");
            view.model.addElement(first);
            view.model.addElement(second);
            view.model.addElement(third);



        }
    }

    // GETS VALUES FROM BUTTON OPTIONS AND TEXT FIELD AND DOES ERROR CHECKING. NEED TO ADD SENDING TO DataBase
    class flipButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String betAmount = view.getBetAmount();
            String betType = view.getBetType();

            String servermsg = String.format("flipCoin %s %s ", betAmount, betType);

            client.sendToServer(servermsg);
            String coinfliped = client.getFromServer();
            String[] serverResults = coinfliped.split(" ");
            if(serverResults.length < 3){
                System.out.println("Error");
            }
            else {
                String results = serverResults[0];
                String betOutcome = serverResults[1];
                String currentBalance = serverResults[2];
                System.out.println(coinfliped);
                String resultingString;
                if (results.equals("1")) {
                    resultingString = "Result = " + betOutcome + ", you win " + betAmount + " dollars!";
                } else if (results.equals("0")) {
                    resultingString = "Result = " + betOutcome + ", you lost " + betAmount + " dollars :(";
                } else {
                    resultingString = "ERROR Flipping coin";
                }
                view.setResultTextField(resultingString);
                view.setCurrentBalanceTextField(Double.valueOf(currentBalance));
            }


            /*
            Double accountBalance = account.getAccountBalance();


            try {
                double bet = Double.parseDouble(betAmount);
                if(bet != (int)bet) {
                    JOptionPane.showMessageDialog(view.jTabs, "please enter integer input or make sure input is an integer!");
                    return;
                }
                else if (bet > accountBalance) {
                    JOptionPane.showMessageDialog(view.jTabs, "you cannot bet more than you have in your account");
                    return;
                }
            } catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(view.jTabs, "please enter valid input for bet. must be a number");
                return;
            }

            String betType = view.getBetType();
            if(Objects.equals(betType, "NO BUTTON SELECTED")) {
                JOptionPane.showMessageDialog(view.jTabs, "Please select heads or tails for your bet!");
                return;
            }

            // then send betType into server/db for game logic
            String betOutcome = bet.flipCoin();
            int results = outcome.getResults(betType, betOutcome);
            double currentBalance = account.getAccountBalance();
            double bet = Double.parseDouble(betAmount);
            String username = account.getUsername();
            String sendOutcome;
            if(results == 1){
                sendOutcome = "Result = " + betOutcome + ", you win " + betAmount + " dollars!";

            }
            else if(results == 0){
                bet = bet *-1;
                sendOutcome = "Result = " + betOutcome + ", you lost " + betAmount + " dollars :(";
            }
            else{
                sendOutcome = "ERROR Flipping coin";
            }
            view.setResultTextField(sendOutcome);
            currentBalance = outcome.updateBalance(bet, currentBalance, username);
            account.setLocalBalance(currentBalance);
            view.setCurrentBalanceTextField(currentBalance);

             */

            /*
            if(Objects.equals(outcome, betType)) {
                String sendOutcome = "Result = " + outcome + ", you win " + betAmount + " dollars!";
                view.setResultTextField(sendOutcome);
                double bet = Double.parseDouble(betAmount);
                double currentBalance = account.getAccountBalance();
                account.addBalance(bet, currentBalance);
                currentBalance = account.getAccountBalance();
                view.setCurrentBalanceTextField(currentBalance);
            }
            else {
                String sendOutcome = "Result = " + outcome + ", you lost " + betAmount + " dollars :(";
                view.setResultTextField(sendOutcome);
                double bet = Double.parseDouble(betAmount);
                double currentBalance = account.getAccountBalance();
                account.removeBalance(bet, currentBalance);
                currentBalance = account.getAccountBalance();
                view.setCurrentBalanceTextField(currentBalance);
            }

             */
        }
    }

    // GETS VALUES FROM USERNAME AND PASSWORD, SHOULD CHECK IF IN DB, IF TRUE THEN LOGGED IN
    class loginButtonListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();
            System.out.println("LOGIN BUTTON HIT");
            if(tabsMade) {
                JOptionPane.showMessageDialog(view.jTabs, "You are already logged in");
            }
            else {

                String servermsg = String.format("login %s %s ", username, password);

                client.sendToServer(servermsg);
                System.out.println("Sent");
                String loggedin = client.getFromServer();

                //System.out.println(output);
                if (loggedin.equals("-1")) {
                    JOptionPane.showMessageDialog(view.jTabs, "Please enter something in password and username field");
                } else if (loggedin.equals("1")) {
                    tabsMade = true;
                    JOptionPane.showMessageDialog(view.jTabs, "You have successfully logged in! You now have access to the game tab!");
                } else {
                    JOptionPane.showMessageDialog(view.jTabs, "Password incorrect, please try again");
                }
                if (tabsMade) {
                    view.jTabs.add("COIN GAME", view.makeGameTab());
                    view.jTabs.add("DICE GAME", view.makeDiceTab());
                    view.jTabs.add("LEADERBOARD", view.makeLeaderboardTab());
                }
            }
        }
    }

    class createAccountButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();

            if(tabsMade) {
                JOptionPane.showMessageDialog(view.jTabs, "You are already logged in");
            }
            else {
                String servermsg = String.format("createAccount %s %s ", username, password);

                client.sendToServer(servermsg);
                String validAccountCreated = client.getFromServer();
                System.out.println(validAccountCreated);
                if (validAccountCreated.equals("1")) {
                    JOptionPane.showMessageDialog(view.jTabs, "Account Created");
                } else if (validAccountCreated.equals("-1")) {
                    JOptionPane.showMessageDialog(view.jTabs, "Please enter something in both fields");
                } else if (validAccountCreated.equals("0")) {
                    JOptionPane.showMessageDialog(view.jTabs, "Account already exists please login");
                } else {
                    JOptionPane.showMessageDialog(view.jTabs, "Error please restart game");
                }
            }
        }
    }

    //DICE GAME LOGIC
    class rollDiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String betAmount = view.getDiceBetAmount();
            String betType = view.getDiceOption();

            String servermsg = String.format("rollDice %s %s ", betAmount, betType);

            client.sendToServer(servermsg);

            String diceRolled = client.getFromServer();
            String[] serverResults = diceRolled.split(" ");
            String results = serverResults[0];
            String betOutcome = serverResults[1];
            String currentBalance = serverResults[2];
            System.out.println(diceRolled);
            String resultingString;
            if(results.equals("1")){
                //betAmount = 3*betAmount;
                resultingString = "Result = " + betOutcome + ", you win " + betAmount + " dollars!";
            }
            else if(results.equals("0")){
                resultingString = "Result = " + betOutcome + ", you lost " + betAmount + " dollars :(";
            }
            else{
                resultingString = "ERROR Flipping coin";
            }
            view.setDiceResultTextField(resultingString);
            view.setDiceBalanceTextField(Double.valueOf(currentBalance));



            /*
            try {
                double bet = Double.parseDouble(betAmount);
                if(bet != (int)bet) {
                    JOptionPane.showMessageDialog(view.jTabs, "please enter integer input or make sure input is an integer!");
                    return;
                }
                else if (bet > accountBalance) {
                    JOptionPane.showMessageDialog(view.jTabs, "you cannot bet more than you have in your account");
                    return;
                }
            } catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(view.jTabs, "please enter valid input for bet. must be a number");
                return;
            }
            String betType = view.getDiceOption();
            String betOutcome = bet.rollDice();
            int results = outcome.getResults(betType, betOutcome);

            double currentBalance = account.getAccountBalance();
            double bet = Double.parseDouble(betAmount);
            String username = account.getUsername();
            String sendOutcome;
            if(results == 1){
                bet = 3*bet;
                sendOutcome = "Result = " + betOutcome + ", you win " + bet + " dollars!";

            }
            else if(results == 0){
                bet = bet *-1;
                sendOutcome = "Result = " + betOutcome + ", you lost " + betAmount + " dollars :(";
            }
            else{
                sendOutcome = "ERROR Flipping coin";
            }
            view.setDiceResultTextField(sendOutcome);
            currentBalance = outcome.updateBalance(bet, currentBalance,username);
            account.setLocalBalance(currentBalance);
            view.setDiceBalanceTextField(currentBalance);

            //System.out.println("bet = " + betOption + " outcome = " + outcome);
            /*
            if (outcome == null) {
                System.out.println("ERROR ROLLING DIE");
            }

            if(Objects.equals(outcome, betOption)) {
                double bet = 3*(Double.parseDouble(diceBetAmount));
                String sendOutcome = "Result = " + outcome + ", you win " + (int)bet + " dollars!";
                view.setDiceResultTextField(sendOutcome);
                double currentBalance = account.getAccountBalance();
                account.addBalance(bet, currentBalance);
                currentBalance = account.getAccountBalance();
                view.setDiceBalanceTextField(currentBalance);
            }
            else {
                String sendOutcome = "Result = " + outcome + ", you lost " + diceBetAmount + " dollars :(";
                view.setDiceResultTextField(sendOutcome);
                double bet = Double.parseDouble(diceBetAmount);
                double currentBalance = account.getAccountBalance();
                account.removeBalance(bet, currentBalance);
                currentBalance = account.getAccountBalance();
                view.setDiceBalanceTextField(currentBalance);
            }

             */



            //System.out.println("bet = " + betOption);

        }
    }
}
