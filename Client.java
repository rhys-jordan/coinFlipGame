import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class Client {

    private Socket socket;

    public Client() {
        try {
            System.out.println("Making new client");
            System.out.println("client> connecting to 127.0.0.1:5000...");
            socket = new Socket("127.0.0.1",5000);
            System.out.println("client> success connecting");

        } catch(IOException ex) {
            System.out.println("SERVER IS NOT UP");
            System.exit(0);
        }
    }

    public void sendToServer(String input){
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(input);
            System.out.println("client> sent request to server: " + input);
            writer.flush();
        }catch (IOException e){
            System.out.println("client> error: Cannot send to server");
        }
    }

    public String getFromServer(){
        try {
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverMsg = serverReader.readLine();
            System.out.println("client> server response: " + serverMsg);
            return serverMsg;
        }catch (IOException e){
            return "Cannot receive from server";
        }
    }
}
