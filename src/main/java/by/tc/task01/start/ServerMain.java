package by.tc.task01.start;

import by.tc.task01.connection.ServerConnector;


public class ServerMain {

    public static void main(String[] args){
        System.out.println("This is server.It create new thread for every connected user.It saves changes in dao when shut down");

        ServerConnector serverConnector=new ServerConnector();
        serverConnector.startServer();
        System.out.println("Server is running");
        do {
            serverConnector.acceptNextClient();
            System.out.println("User connected");
        } while (true);
    }
}
