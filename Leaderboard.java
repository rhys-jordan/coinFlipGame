import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Leaderboard extends Server{


    public ArrayList<String> searchData() {
        ArrayList<String> arrayList = new ArrayList<String>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:userDatabase.db");
            String cmd = "SELECT * FROM users " +
                    "ORDER BY users.balance DESC " +
                    "LIMIT 3;";
            ResultSet rs = conn.createStatement().executeQuery(cmd);
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                int balance = rs.getInt("balance");
                boolean loggedIn = rs.getBoolean("loggedIn");
                String s = String.format("%3d %15s %15s %3d %3b",id,username,password,balance,loggedIn);
                arrayList.add(s);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return arrayList;
    }
}
