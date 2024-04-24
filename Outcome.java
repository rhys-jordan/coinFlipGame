import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class Outcome extends Server{

    public int getResults(String betOption, String outcome){
        if (outcome == null) {
            System.out.println("ERROR ROLLING DIE");
            return -1;
        }
        else if(Objects.equals(outcome, betOption)){
            System.out.println("YOU WIN!");
            return 1;
        }
        else{
            System.out.println("YOU LOOSE!");
            return 0;
        }
    }

    public void updateBalance(double bet, double currentBalance, String username){
        double newBalance = currentBalance + bet;

        try {
            connection = DriverManager.getConnection(uri);
            //Statement stmt = connection.createStatement();
            System.out.println("new balance = " + newBalance + " username = " + username);

            String updateQuery = "UPDATE users " +
                    "SET balance = ? " +
                    "WHERE username = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(updateQuery);
            prepStmt.setDouble(1,newBalance);
            prepStmt.setString(2,username);
            prepStmt.executeUpdate();
            //localBalance = newBalance;
            //connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("error increasing balance");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }
}

