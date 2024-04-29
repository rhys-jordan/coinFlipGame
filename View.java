import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class View {

    JTabbedPane jTabs;

    private boolean loggedIn = false;

    private JPanel loginPanel;

    private JTextField usernameTextField;
    private JTextField passwordTextField;

    private JButton loginButton;
    private JButton createAccountButton;

    private JRadioButton headsButton;
    private JRadioButton tailsButton;
    private ButtonGroup G1;

    private JButton flipButton = new JButton("FLIP COIN");

    private JTextField betTextField;
    private JTextField resultTextField;
    private JTextField currentBalanceTextField;

    private JTextField diceBetTextField;
    private JTextField diceResultTextField;
    private JTextField diceBalanceTextField;

    private JButton rollButton = new JButton("ROLL DICE");

    private JComboBox diceBox;

    String diceOptions[] = {"ONE","TWO","THREE","FOUR","FIVE"};

    DefaultListModel<String> model;
    private JList<String> leaderboardList;

    View() {
        JFrame jFrame = new JFrame();
        model = new DefaultListModel<>();
        jTabs = new JTabbedPane();
        jTabs.add("LOGIN/CREATE ACC", makeLoginTab());


        jFrame.add(jTabs);
        jFrame.setSize(600,500);
        jFrame.setLocationRelativeTo(null);
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

        usernameTextField = new JTextField(10);
        passwordTextField = new JTextField(10);

        loginButton = new JButton("LOGIN");
        createAccountButton = new JButton("CREATE ACCOUNT");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        loginPanel.add(new JLabel("USERNAME:"),gbc);

        gbc.gridx = 1;
        loginPanel.add(usernameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("PASSWORD"),gbc);

        gbc.gridx = 1;
        loginPanel.add(passwordTextField,gbc);

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


        betTextField = new JTextField(0);
        resultTextField = new JTextField(0);
        currentBalanceTextField = new JTextField(0);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gamePanel.add(new JLabel("COIN FLIP BET OPTIONS:"),gbc);

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
        gbc.ipadx = 200;
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


        gbc.gridx = 0;
        gbc.gridy = 5;
        gamePanel.add(new JLabel("BALANCE:"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gamePanel.add(currentBalanceTextField,gbc);

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
        gbc.insets = new Insets(10,160,10,10);
        leaderboardPanel.add(new JLabel("TOP 3 USERS WITH THE HIGHEST BALANCE:"),gbc);

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

    public JPanel makeDiceTab() {
        JPanel dicePanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        dicePanel.setLayout(new GridBagLayout());

        diceBetTextField = new JTextField(0);
        diceResultTextField = new JTextField(0);
        diceBalanceTextField = new JTextField(0);

        String diceOptions[] = {"ONE","TWO","THREE","FOUR","FIVE","SIX"};

        diceBox = new JComboBox(diceOptions);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(10,10,10,10);
        dicePanel.add(new JLabel("DICE ROLL BET OPTIONS:"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        dicePanel.add(diceBox,gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dicePanel.add(new JLabel("BET AMOUNT:"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.ipadx = 200;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        dicePanel.add(diceBetTextField,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(10,10,10,10);
        dicePanel.add(rollButton,gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        dicePanel.add(new JLabel("BET RESULT:"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.ipadx = 200;
        gbc.insets = new Insets(10,10,10,10);
        dicePanel.add(diceResultTextField,gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        dicePanel.add(new JLabel("BALANCE:"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.ipadx = 200;
        gbc.insets = new Insets(10,10,10,10);
        dicePanel.add(diceBalanceTextField,gbc);

        return dicePanel;
    }

    void setLoginButtonListener(ActionListener aL) {
        loginButton.addActionListener(aL);
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public String getPassword() {
        return passwordTextField.getText();
    }

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
            betType = "HEADS";
        }
        else if (tailsButton.isSelected()) {
            betType = "TAILS";
        }
        else {
            betType = "NO BUTTON SELECTED";
        }
        return betType;
    }

    public void setResultTextField(String outcome) {
        resultTextField.setText(outcome);
    }

    public void setCurrentBalanceTextField(Double bal) {
        currentBalanceTextField.setText(String.valueOf(bal));
    }

    public String getDiceBetAmount() {
        return diceBetTextField.getText();
    }

    public void setDiceResultTextField(String outcome) {
        diceResultTextField.setText(outcome);
    }

    public void setDiceBalanceTextField(Double bal) {
        diceBalanceTextField.setText(String.valueOf(bal));
    }

    void setRollDiceButtonListener(ActionListener aL) {
        rollButton.addActionListener(aL);
    }

    public String getDiceOption() {
        return (String) diceBox.getSelectedItem();
    }
}
