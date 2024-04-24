import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Random;

public class User {

    private View view;
    private Client client;
    private Account account;
    private Leaderboard leaderboard;
    private boolean tabsMade = false;

    public User() {
        view = new View();
        client = new Client();
        account = new Account();
        leaderboard = new Leaderboard();

        view.setAddChangeListener(new jTabListener());
        view.setLoginButtonListener(new loginButtonListener());
        view.setFlipButtonListener(new flipButtonListener());
        view.setRollDiceButtonListener(new rollDiceButtonListener());
        view.setCreateAccountButtonListener(new createAccountButtonActionListener());
    }

    public String flipCoin() {
        Random rand = new Random();
        int outcome = rand.nextInt(2);

        if(outcome == 1) {
            return "HEADS";
        }
        else{
            return "TAILS";
        }
    }

    public  String rollDice() {
        Random rand = new Random();
        int outcome = rand.nextInt(6);
        if (outcome == 0) {
            return "ONE";
        }
        else if (outcome == 1) {
            return "TWO";
        }
        else if (outcome == 2) {
            return "THREE";
        }
        else if (outcome == 3) {
            return "FOUR";
        }
        else if (outcome == 4) {
            return "FIVE";
        }
        else if (outcome == 5) {
            return "SIX";
        }
        else {
            System.out.println("ERROR: couldnt flip coin");
            return null;
        }
    }

    public class jTabListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            // check if logged in?
            ArrayList<String> arrayList;

            // TODO CHANGE THIS
            arrayList = leaderboard.getTopThree();
            view.model.clear();

            for (String s : arrayList) {
                // sort arraylist(since already in arraylist, should maybe be a separate function?) then grab top 3 (probably outside of loop)
                // should reformat cmd to only get relevant information. i.e. only display username and balance (definitely wouldn't want to display password)
                view.model.addElement(s);
            }
        }
    }

    // GETS VALUES FROM BUTTON OPTIONS AND TEXT FIELD AND DOES ERROR CHECKING. NEED TO ADD SENDING TO DataBase
    class flipButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String betAmount = view.getBetAmount();
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
            String outcome = flipCoin();

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
        }
    }

    // GETS VALUES FROM USERNAME AND PASSWORD, SHOULD CHECK IF IN DB, IF TRUE THEN LOGGED IN
    class loginButtonListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();
            int validAccount = account.verifyAccount(username, password);

            if(account.getLoggedIn()) {
                JOptionPane.showMessageDialog(view.jTabs, "You are already logged in, exit game to logout");
            }
            else if(validAccount == 1){
                account.login(password);
                JOptionPane.showMessageDialog(view.jTabs, "You have successfully logged in! You now have access to the game tab!");
            }
            else if (validAccount == -1){
                JOptionPane.showMessageDialog(view.jTabs, "Please enter something in username field");
            }
            else{
                JOptionPane.showMessageDialog(view.jTabs, "Username or password incorrect, please try again or create an account");
            }

                // upon exit need to set boolean in database to false

            if(account.getLoggedIn() && !tabsMade) {
                view.jTabs.add("COIN GAME", view.makeGameTab());
                view.jTabs.add("DICE GAME",view.makeDiceTab());
                view.jTabs.add("LEADERBOARD", view.makeLeaderboardTab());
                tabsMade = true;
            }
            if(tabsMade) {
                // idk why this if check is here can prolly delete it
            }
        }
    }

    class createAccountButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();

            int validAccountCreated =  account.createAccount(username, password);
            //JOptionPane.showMessageDialog(view.jTabs, accountCreatedMsg);

            if(validAccountCreated == 1){
                JOptionPane.showMessageDialog(view.jTabs, "Account Created");
            }
            else if (validAccountCreated == -1){
                JOptionPane.showMessageDialog(view.jTabs, "Please enter something in both fields");
            }
            else if (validAccountCreated == 0){
                JOptionPane.showMessageDialog(view.jTabs, "Account already exists please login");
            }
            else{
                JOptionPane.showMessageDialog(view.jTabs, "Error please restart game");
            }
        }
    }

    //DICE GAME LOGIC
    class rollDiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String diceBetAmount = view.getDiceBetAmount();
            Double accountBalance = account.getAccountBalance();

            try {
                double bet = Double.parseDouble(diceBetAmount);
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
            String betOption = view.getDiceOption();
            String outcome = rollDice();
            //System.out.println("bet = " + betOption + " outcome = " + outcome);

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



            //System.out.println("bet = " + betOption);

        }
    }
}
