
import java.sql.*;

public class Model {
    private Connection connection;
    private String uri;

    public Model(){
        connection = null;
        try{
            uri = "jdbc:sqlite:coinFlipUsers.db";
            connection = DriverManager.getConnection(uri);
            Statement stmt = connection.createStatement();

            String cmd = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY,"
                    + "username STRING,"
                    + "password STRING,"
                    + "balance DOUBLE"
                    + ");";
            stmt.execute(cmd);


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



}
