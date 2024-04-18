import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class View {

    JTabbedPane jTabs;

    private boolean loggedIn = false;

    private JPanel loginPanel;

    private JTextField usernameTextFeild;
    private JTextField passwordTextFeild;

    private JButton loginButton;
    private JButton createAccountButton;

    private JRadioButton headsButton;
    private JRadioButton tailsButton;
    private ButtonGroup G1;

    private JButton flipButton;

    private JTextField betTextField;
    private JTextField resultTextField;

    DefaultListModel<String> model;
    private JList<String> leaderboardList;

    View() {
        JFrame jFrame = new JFrame();
        model = new DefaultListModel<>();
        jTabs = new JTabbedPane();
        jTabs.add("LOGIN/CREATE ACC", makeLoginTab());
        jTabs.add("GAME WINDOW", makeGameTab());
        jTabs.add("LEADERBOARD", makeLeaderboardTab());

        jFrame.add(jTabs);
        jFrame.setSize(400,500);
        jFrame.setVisible(true);
    }

    public void setAddChangeListener(ChangeListener changeListener) {
        jTabs.addChangeListener(changeListener);
    }

    public JPanel makeLoginTab() {

        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);

        usernameTextFeild = new JTextField(10);
        passwordTextFeild = new JTextField(10);

        loginButton = new JButton("LOGIN");
        createAccountButton = new JButton("CREATE ACCOUNT");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        loginPanel.add(new JLabel("USERNAME:"),gbc);

        gbc.gridx = 1;
        loginPanel.add(usernameTextFeild, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("PASSWORD"),gbc);

        gbc.gridx = 1;
        loginPanel.add(passwordTextFeild,gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(loginButton,gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(createAccountButton,gbc);

        return loginPanel;
    }

    public JPanel makeGameTab() {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(10,10,10,10);

        headsButton = new JRadioButton();
        tailsButton = new JRadioButton();
        G1 = new ButtonGroup();
        flipButton = new JButton("FLIP COIN");

        betTextField = new JTextField(10);
        resultTextField = new JTextField(0);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gamePanel.add(new JLabel("BET OPTIONS:"),gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,20,10,0);
        gamePanel.add(new JLabel("HEADS:"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,10,10);
        gamePanel.add(headsButton,gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,10,30);
        gamePanel.add(new JLabel("TAILS:"),gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,10,10);
        gamePanel.add(tailsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gamePanel.add(new JLabel("BET AMOUNT:"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gamePanel.add(betTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.ipadx = 200;
        gamePanel.add(flipButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gamePanel.add(new JLabel("BET RESULT:"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gamePanel.add(resultTextField,gbc);

        G1.add(headsButton);
        G1.add(tailsButton);

        return gamePanel;
    }

    public JPanel makeLeaderboardTab() {
        JPanel leaderboardPanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        leaderboardPanel.setLayout(new GridBagLayout());
        gbc.insets = new Insets(10,10,10,10);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        leaderboardPanel.add(new JLabel("TOP 3 USERS WITH THE HIGHEST BALANCE:"),gbc);


        model.addElement("user 1: $1000");
        model.addElement("uasdadassaser 2: $7000");
        model.addElement("user 3: $500");


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10,50,50,50);
        gbc.anchor = GridBagConstraints.PAGE_END;

        leaderboardList = new JList<String>(model);

        leaderboardPanel.add(leaderboardList, gbc);

        return leaderboardPanel;
    }

    //STUFF FOR LOGIN TAB
    void setLoginButtonListener(ActionListener aL) {
        loginButton.addActionListener(aL);
    }
    public String getUsername() {
        return usernameTextFeild.getText();
    }

    public String getPassword() {
        return passwordTextFeild.getText();
    }

    // LISTENERS AND GETTERS(relevant functions) FOR GAME TAB
    void setFlipButtonListener(ActionListener aL) {
        flipButton.addActionListener(aL);
    }

    void setCreateAccountButtonListener(ActionListener aL) {
        createAccountButton.addActionListener(aL);
    }

    public String getBetAmount() {
        return betTextField.getText();
    }

    public String getBetType() {
        String betType;
        if (headsButton.isSelected()) {
            betType = "Heads";
        }
        else if (tailsButton.isSelected()) {
            betType = "Tails";
        }
        else {
            betType = "NO BUTTON SELECTED";
        }
        return betType;
    }
}
