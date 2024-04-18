import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Account extends Server{
    private String username;
    private String password;
    private double balance;


    public String createAccount(String username, String password){
        if(username.isEmpty() || password.isEmpty()){
            return "Please enter something in both fields!";
        }
        //To Do check if account exists
        else{
            try{
                connection = DriverManager.getConnection("jdbc:sqlite:userDatabase.db");
                Statement stmt = connection.createStatement();
                String insert = "INSERT INTO users (username, password, balance, loggedIn)" +
                        "VALUES (?,?,?,?);";
                PreparedStatement prepStmt = connection.prepareStatement(insert);
                //String add = "INSERT INTO cats (name) VALUES ('" + record + "');";
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
            return "Account Created";
        }
    }


}
