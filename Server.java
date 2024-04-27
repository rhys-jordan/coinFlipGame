import java.awt.image.PackedColorModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ServerSocket server;

    public static void main(String [] args) {
        Server s = new Server();
    }

    public Server(){
        try{
            server = new ServerSocket(5000);
            while(true){
                System.out.println("waiting for client to connect..");
                Socket conn = server.accept();
                System.out.println("connected to client " + conn);
                ClientHandler clientSock = new ClientHandler(conn);
                new Thread(clientSock).start();
            }

        } catch (IOException e) {
            try {
                server.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
