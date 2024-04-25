import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Leaderboard{
    protected String uri = "jdbc:sqlite:userDatabase.db";
    protected Connection connection = null;


    public String getTopThree() {
        //LinkedHashMap<String, Integer> topThree = new LinkedHashMap<String, Integer>();
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<Integer> balances = new ArrayList<Integer>();
        String leader = "";

        try {
            Connection conn = DriverManager.getConnection(uri);
            String cmd = "SELECT users.username, users.balance FROM users " +
                    "ORDER BY users.balance DESC " +
                    "LIMIT 3;";
            ResultSet rs = conn.createStatement().executeQuery(cmd);
            while (rs.next()) {
                //int id = rs.getInt("id");
                String username = rs.getString("username");
                //String password = rs.getString("password");
                int balance = rs.getInt("balance");
                //boolean loggedIn = rs.getBoolean("loggedIn");
                //String leader = String.format("%s  %d ", username,balance);
                leader = leader.concat(String.format("%s %d ", username,balance));
                //System.out.println(leader);
                //arrayList.add(s);
                //arrayList.add(leader);
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
