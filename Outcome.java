import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class Outcome extends Account{

    protected String uri = "jdbc:sqlite:userDatabase.db";
    protected Connection connection = null;

    public int getResults(String betOption, String outcome){
        if (outcome == null) {
            System.out.println("server> ERROR CALCULATING OUTCOME");
            return -1;
        }
        else if(Objects.equals(outcome, betOption)){
            return 1;
        }
        else{
            return 0;
        }
    }

    public double updateBalance(double bet, double currentBalance, String username){
        double newBalance = currentBalance + bet;

        try {
            connection = DriverManager.getConnection(uri);

            String updateQuery = "UPDATE users " +
                    "SET balance = ? " +
                    "WHERE username = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(updateQuery);
            prepStmt.setDouble(1,newBalance);
            prepStmt.setString(2,username);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("server> error increasing balance");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return newBalance;
        }
    }
}