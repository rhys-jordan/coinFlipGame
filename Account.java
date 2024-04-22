import java.sql.*;

public class Account extends Server{
    private String username;
    private String password;
    private boolean loggedIn = false;
    private double balance;

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public int createAccount(String username, String password){
        if(username.isEmpty() || password.isEmpty()){
            return -1;
        }
        else if(verifyAccount(username) == 1){
            return 0;
        }
        else{
            try{
                connection = DriverManager.getConnection(uri);
                String insert = "INSERT INTO users (username, password, balance, loggedIn) " +
                        "VALUES (?,?,?,?);";
                PreparedStatement prepStmt = connection.prepareStatement(insert);
                prepStmt.setString(1,username);
                prepStmt.setString(2,password);
                prepStmt.setInt(3,100);
                prepStmt.setBoolean(4,false);
                prepStmt.executeUpdate();
                connection.close();
            }catch (SQLException ex){
                System.out.println("error chceck");
                ex.printStackTrace();
            }
            return 1;
        }
    }

    public int verifyAccount(String username){
        if(username.isEmpty()){
            return -1;
        }
        else {
            try {
                connection = DriverManager.getConnection(uri);
                String exist = "SELECT COUNT(id) " +
                        "FROM users " +
                        "WHERE users.username = ?;" ;
                PreparedStatement prepStmt = connection.prepareStatement(exist);
                prepStmt.setString(1, username);
                ResultSet results = prepStmt.executeQuery();
                if (results.next()) {
                    int count = results.getInt(1);
                    if(count == 1){
                        this.username = username;
                        return 1;
                    }
                }
                connection.close();
                return 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return 3;
            }

        }
    }

    public void login(String password){
        if(password.isEmpty() || username.isEmpty()){
            System.out.println("Please enter something in username field");
        }
        else{
            try {
                connection = DriverManager.getConnection(uri);
                String exist = "SELECT id " +
                        "FROM users " +
                        "WHERE users.username = ? AND users.password = ? AND users.loggedIn = ?;" ;
                PreparedStatement prepStmt = connection.prepareStatement(exist);
                prepStmt.setString(1, username);
                prepStmt.setString(2, password);
                prepStmt.setBoolean(3,false);

                ResultSet results = prepStmt.executeQuery();
                //System.out.println(results.next());
                //prepStmt.executeUpdate();

                if (results.next()) {
                    int id = results.getInt("id");
                    System.out.println("COunt " + id);
                    //this.password = password;
                    this.loggedIn = true;
                    /*
                    String cmd = "UPDATE users" +
                            "SET users.loggedIn = ?" +
                            "WHERE id = ?;";
                    PreparedStatement update = connection.prepareStatement(cmd);
                    update.setBoolean(1, true);
                    update.setInt(2, id);
                    update.executeUpdate();
                     */
                }
                connection.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public double getAccountBalance() {
        try {
            connection = DriverManager.getConnection(uri);
            //Statement stmt = connection.createStatement();
            String query = "SELECT users.balance " +
                    "FROM users " +
                    "WHERE users.username = ?;";

            PreparedStatement prepStmt = connection.prepareStatement(query);
            prepStmt.setString(1,username);
            ResultSet results = prepStmt.executeQuery();

            if(results.next()) {
                balance = results.getDouble("balance");
                connection.close();
                return balance;
            }
            else {
                System.out.println("Error: couldn't get balance");
                connection.close();
                return -1;
            }

        } catch (SQLException ex){
            ex.printStackTrace();
            return  -1;
        }
    }

    public void addBalance(double winnings, double currentBalance) {

        double newBalance = currentBalance + winnings;

        try {
            connection = DriverManager.getConnection(uri);
            //Statement stmt = connection.createStatement();
            System.out.println("new balance = " + newBalance + " username = " + username);

            String updateQuery = "UPDATE users " +
                    "SET users.balance = ? " +
                    "WHERE users.username = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(updateQuery);
            prepStmt.setDouble(1,newBalance);
            prepStmt.setString(2,username);
            prepStmt.executeUpdate();
            balance = newBalance;
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("error increasing balance");
        }
    }

    public void removeBalance(double losings, double currentBalance) {

        double newBalance = currentBalance - losings;

        try {
            connection = DriverManager.getConnection(uri);
            System.out.println("new balance = " + newBalance + " username = " + username);
            //Statement stmt = connection.createStatement();
            String updateQuery = "UPDATE users " +
                    "SET users.balance = ? " +
                    "WHERE users.username = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(updateQuery);
            prepStmt.setDouble(1,newBalance);
            prepStmt.setString(2,username);
            prepStmt.executeUpdate();
            balance = newBalance;
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("error decreasing balance");
        }
    }
}
