import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Leaderboard{
    protected String uri = "jdbc:sqlite:userDatabase.db";
    protected Connection connection = null;


    public String getTopThree() {
        String leader = "";
        try {
            Connection conn = DriverManager.getConnection(uri);
            String cmd = "SELECT users.username, users.balance FROM users " +
                    "ORDER BY users.balance DESC " +
                    "LIMIT 3;";
            ResultSet rs = conn.createStatement().executeQuery(cmd);
            while (rs.next()) {
                String username = rs.getString("username");
                int balance = rs.getInt("balance");
                leader = leader.concat(String.format("%s %d ", username,balance));
            }
            return leader;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
