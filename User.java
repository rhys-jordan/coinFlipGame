import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class User {

    private View view;
    private Client client;

    public User() {
        view = new View();
        client = new Client();

        view.setAddChangeListener(new jTabListener());
        view.setLoginButtonListener(new loginButtonListener());
        view.setFlipButtonListener(new flipButtonListener());

    }

    public class jTabListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            // check if logged in?
            ArrayList<String> arrayList;

            String cmd = "SELECT * FROM users";
            arrayList = client.searchData(cmd);
            view.model.clear();
            for (String s : arrayList) {
                view.model.addElement(s);
            }
        }
    }

    // GETS VALUES FROM BUTTON OPTIONS AND TEXT FIELD AND DOES ERROR CHECKING. NEED TO ADD SENDING TO DataBase
    class flipButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String betAmount = view.getBetAmount();
            try {
                Integer.parseInt(betAmount);
            } catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(view.jTabs, "please enter integer input or make sure input is an integer!");
            }
            // send betAmount to server/database (should maybe be in try?)

            String betType = view.getBetType();
            if(Objects.equals(betType, "NO BUTTON SELECTED")) {
                JOptionPane.showMessageDialog(view.jTabs, "Please select heads or tails for your bet!");
            }
            else {
                // then send betType into server/db for game logic
            }

        }
    }

    // GETS VALUES FROM USERNAME AND PASSWORD, SHOULD CHECK IF IN DB, IF TRUE THEN LOGGED IN
    class loginButtonListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();

            if(Objects.equals(username, "") & Objects.equals(password, "")) {
                JOptionPane.showMessageDialog(view.jTabs, "Please enter something for your username and password!");
            }
            else if (Objects.equals(username, "")) {
                JOptionPane.showMessageDialog(view.jTabs, "Please enter something for your username!");
            }
            else if(Objects.equals(password, "")) {
                JOptionPane.showMessageDialog(view.jTabs, "Please enter something for your password!");
            }
            // have another else if to check if logged in = true, then dont log them in again. or can allow for it and just switch account info to new acct info
            else { // should probably change to else if? in order to check if in db
                //check if they already exist in db. then allow access to tabs (change a bool to true or something)

                JOptionPane.showMessageDialog(view.jTabs, "You have successfully logged in! You now have access to the game tab!");
            }
        }
    }
}
