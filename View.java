import javax.swing.*;
import java.awt.*;

public class View {

    private JTextField usernameTextFeild;
    private JTextField passwordTextFeild;

    private JButton loginButton;
    private JButton signinButton;

    JTabbedPane jTabs;

    View() {
        JFrame jFrame = new JFrame();
        jTabs = new JTabbedPane();
        jTabs.add("LOGIN/CREATE ACC", makeLoginTab());

        jFrame.add(jTabs);
        jFrame.setSize(400,500);
        jFrame.setVisible(true);
    }

    public JPanel makeLoginTab() {

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);

        usernameTextFeild = new JTextField(10);
        passwordTextFeild = new JTextField(10);

        loginButton = new JButton("LOGIN");
        signinButton = new JButton("SIGN UP");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
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
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(loginButton,gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(signinButton,gbc);

        // if logged in then return (for now just return anyway)
        return loginPanel;
    }

    public JPanel makeGameTab() {
        JPanel gamePanel = new JPanel();
        gamePanel .setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);


        return gamePanel;
    }

    public JPanel makeLeaderboardTab() {
        JPanel leaderboardPanel = new JPanel();

        return leaderboardPanel;
    }
}
