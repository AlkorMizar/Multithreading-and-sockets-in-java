package by.tc.task01.connection;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {
    @XmlElement
    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    public static enum CommandEn{REGISTER_USER, AUTHORIZE_USER,ADD_TO_ARCHIVE,VIEW_ELEM,
        GET_ARCHIVE,DELETE_IN_ARCHIVE,UPDATE_IN_ARCHIVE,CLOSE_CONNECTION,UPDATE_USER,GET_USERS,
        SUCCESS,FAILED}
    CommandEn command;
    Object value;
    Long token;

    public  Message(){}
    public Message(CommandEn command) {
        this.command = command;
        token=-1L;
    }

    public Message(CommandEn command,Long token) {
        this.command = command;
        this.token=token;
    }
    @XmlElement
    public CommandEn getCommand() {
        return command;
    }

    public void setCommand(CommandEn command) {
        this.command = command;
    }
    @XmlElement
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


}
