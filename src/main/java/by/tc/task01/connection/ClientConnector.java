package by.tc.task01.connection;

import jakarta.xml.bind.JAXBException;

import java.net.Socket;

public class ClientConnector {

    public ClientConnector() throws JAXBException {

    }

    public SendRecive connectToServer(){
        for (int i = 0; i < 1000; i++) {
            try {
                var clientSocket = new Socket("localhost", 4004);
                return new SendRecive(clientSocket);
            }catch (Exception ignored){
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    return null;
                }
            };
        }
        return null;
    }
}
