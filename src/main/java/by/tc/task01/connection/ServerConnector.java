package by.tc.task01.connection;



import by.tc.task01.application.ApiServerSide;
import jakarta.xml.bind.JAXBException;

import java.io.*;
import java.net.ServerSocket;
import java.util.Arrays;

public class ServerConnector {

    private ServerSocket server; // серверсокет


    public ServerConnector(){

    }

    public boolean startServer(){
        try {
            server = new ServerSocket(4004);
            return true;
        }catch (Exception ignored){};
        return false;
    }

    public void acceptNextClient(){
        try {
            var clientSocket = server.accept();
            new ApiServerSide(new SendRecive(clientSocket)).start();
        } catch (IOException | JAXBException e) {
            //e.printStackTrace();
        }
    }


}
