import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class Client {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Client() {
        try {
            System.out.println("client> connecting to 127.0.0.1:5000...");
            socket = new Socket("127.0.0.1",5000);
            System.out.println("client> success connecting");

            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(socket.getOutputStream());
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    // need to implement updates and checks
    public ArrayList<String> searchData(String cmd) {
        ArrayList<String> arrayList = new ArrayList<String>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:userDatabase.db");
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
