import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class User {

    private View view;
    private Client client;
    private Account account;
    private boolean tabsMade = false;

    public User() {
        view = new View();
        client = new Client();
        account = new Account();

        view.setAddChangeListener(new jTabListener());
        view.setLoginButtonListener(new loginButtonListener());
        view.setFlipButtonListener(new flipButtonListener());
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

    public class jTabListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            // check if logged in?
            ArrayList<String> arrayList;

            // TODO CHANGE THIS
            String cmd = "SELECT * FROM users";
            arrayList = client.searchData(cmd);
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
            int validAccount = account.verifyAccount(username);

            if(account.getLoggedIn()) {
                JOptionPane.showMessageDialog(view.jTabs, "You are already logged in");
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
                view.jTabs.add("GAME WINDOW", view.makeGameTab());
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
}
