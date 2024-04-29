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
            view.model.clear();

            String servermsg = String.format("leaderboard ");
            client.sendToServer(servermsg);
            String leaders = client.getFromServer();
            //System.out.println(leaders);
            String[] leaderList = leaders.split(" ");
            if(leaderList.length < 6) {
                view.model.addElement("ERROR: not enough users to display top 3 users");
            }
            else {
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
            String resultingString = null;
            String results = null;

            if(serverResults.length < 3){
                 results = serverResults[0];
                if (results.equals("-1")){
                    resultingString = "ERROR: please enter an integer input";
                } else if (results.equals("-2")) {
                    resultingString = "ERROR: you cannot bet more than is in your account";
                } else if (results.equals("-3")) {
                    resultingString = "ERROR: you cannot enter a negative bet";
                } else if (results.equals("-4")) {
                    resultingString = "ERROR: please enter a round number i.e. no decimals";
                } else if (results.equals("-5")) {
                    resultingString = "YOU SUCK AT GAMBLING, you have lost all your money, here's 50 free dollars for your addiction :)";
                    view.setCurrentBalanceTextField((double)50);
                }

                view.setResultTextField(resultingString);
            }
            else {
                results = serverResults[0];
                String betOutcome = serverResults[1];
                String currentBalance = serverResults[2];
                //System.out.println(coinfliped);

                if (results.equals("1")) {
                    resultingString = "Result = " + betOutcome + ", you win " + betAmount + " dollars!";
                } else if (results.equals("0")) {
                    resultingString = "Result = " + betOutcome + ", you lost " + betAmount + " dollars :(";
                }
                view.setResultTextField(resultingString);
                view.setCurrentBalanceTextField(Double.valueOf(currentBalance));
            }
        }
    }

    // GETS VALUES FROM USERNAME AND PASSWORD, SHOULD CHECK IF IN DB, IF TRUE THEN LOGGED IN
    class loginButtonListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();
            //System.out.println("LOGIN BUTTON HIT");
            if(tabsMade) {
                JOptionPane.showMessageDialog(view.jTabs, "You are already logged in");
            }
            else {
                String servermsg = String.format("login %s %s ", username, password);

                client.sendToServer(servermsg);
                //System.out.println("client> Sent");
                String loggedin = client.getFromServer();

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
                //System.out.println(validAccountCreated);
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
            String results = null;
            String resultingString = null;

            if(serverResults.length < 3){
                results = serverResults[0];

                if(results.equals("-1")) {
                    resultingString = "ERROR: please enter an integer input";
                } else if (results.equals("-2")) {
                    resultingString = "ERROR: you cannot bet more than is in your account";
                } else if (results.equals("-3")) {
                    resultingString = "ERROR: you cannot enter a negative bet";
                } else if (results.equals("-4")) {
                    resultingString = "ERROR: please enter a round number i.e. no decimals";
                } else if (results.equals("-5")) {
                    resultingString = "YOU SUCK AT GAMBLING, you have lost all your money, here's 50 free dollars for your addiction :)";
                    view.setDiceBalanceTextField((double)50);
                }

                view.setDiceResultTextField(resultingString);
            }

            else {
                results = serverResults[0];
                String betOutcome = serverResults[1];
                String currentBalance = serverResults[2];
                //System.out.println(diceRolled);
                if(results.equals("1")){
                    try {

                    } catch (NumberFormatException ex) {
                        System.out.println("ERROR, couldnt convert bet string to int");
                    }
                    //betAmount = 3*betAmount;
                    resultingString = "Result = " + betOutcome + ", you win " + betAmount + " dollars!";
                }
                else if(results.equals("0")){
                    resultingString = "Result = " + betOutcome + ", you lost " + betAmount + " dollars :(";
                }
                view.setDiceResultTextField(resultingString);
                view.setDiceBalanceTextField(Double.valueOf(currentBalance));
            }
        }
    }
}
