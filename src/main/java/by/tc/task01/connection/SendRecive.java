package by.tc.task01.connection;

import by.tc.task01.application.dto.ArchiveElementDTO;
import by.tc.task01.application.dto.UserDTO;
import by.tc.task01.application.dto.Wrapper;
import by.tc.task01.entity.impl.ArchiveElement;
import by.tc.task01.entity.impl.User;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.*;
import java.net.Socket;

public class SendRecive {
    Socket socket;
    Marshaller jaxbMarshaller;
    Unmarshaller jaxbUnmarshaller;

    public SendRecive(Socket socket) throws JAXBException {
        this.socket = socket;

        var classes=new Class[] {Pair.class,Message.class, UserDTO.class, Wrapper.class, ArchiveElementDTO.class, User.class, ArchiveElement.class};
        var jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                .createContext(classes, null);
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    }

    public boolean send(Message message)  {
        try
        {
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(message, sw);
            var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            out.write(sw.toString());
            out.flush();
            return true;

        } catch (Exception e)
        {
            //e.printStackTrace();
        }
        return false;
    }

    public Message receive()  {
        try
        {
            var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder builder=new StringBuilder();
            boolean stop=true;
            char[] buff=new char[128];
            do {
                var intt=in.read(buff);
                stop=intt!=128;
                builder.append(buff,0,intt);
            }while (!stop);
            StringReader reader=new StringReader(builder.toString());
            return (Message)jaxbUnmarshaller.unmarshal(reader);

        } catch (Exception ignore) {
            //ignore.printStackTrace();
        }
        return null;
    }
}
