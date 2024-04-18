import java.sql.*;

public class Account extends Server{
    private String username;
    private String password;
    private double balance;


    public String createAccount(String username, String password){
        if(username.isEmpty() || password.isEmpty()){
            return "Please enter something in both fields!";
        }
        else if( verifyAccount(username, password) == 1){
            return "Account exists please login";
        }
        else{
            try{
                connection = DriverManager.getConnection(uri);
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

    public int verifyAccount(String username, String password){
        if(username.isEmpty() || password.isEmpty()){
            return -1;
        }
        else {
            try {
                connection = DriverManager.getConnection(uri);
                Statement stmt = connection.createStatement();
                String exist = "SELECT * " +
                        "FROM users " +
                        "WHERE EXISTS" +
                        "(SELECT * FROM users WHERE " +
                        "users.username = ?);";
                PreparedStatement prepStmt = connection.prepareStatement(exist);
                prepStmt.setString(1, username);
                ResultSet results = prepStmt.executeQuery();
                //prepStmt.executeUpdate();
                if (results.next()) {
                    int count = results.getInt(1);
                    if(count == 1){
                        return 1;
                    }
                }
                connection.close();
                return 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

}
