package by.tc.task01.presentation;

import by.tc.task01.application.dto.ArchiveElementDTO;
import by.tc.task01.application.dto.UserDTO;
import by.tc.task01.application.dto.Wrapper;
import by.tc.task01.connection.Message;
import by.tc.task01.connection.Pair;
import by.tc.task01.connection.SendRecive;
import by.tc.task01.entity.impl.User;
import jakarta.xml.bind.JAXBException;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;

public class APIClientSide {

    SendRecive sendRecive;
    Long token;
    List<ArchiveElementDTO> currentArchive;
    Scanner sc;


    public APIClientSide(SendRecive sendRecive) throws JAXBException {
        token=-1L;
        sc=new Scanner(System.in);
        if(sendRecive==null){
            throw new NullPointerException("Connection doesn't exist");
        }
        this.sendRecive=sendRecive;
    }

    public void processCommand(Message.CommandEn commandEn) throws NoSuchAlgorithmException {
        switch (commandEn) {
            case REGISTER_USER:
                register();
                break;
            case AUTHORIZE_USER:
                enter();
                break;
            case GET_ARCHIVE:
                getArchive();
                break;
            case ADD_TO_ARCHIVE:
                if (getArchive()){
                    addElemInArchive();
                }
                break;
            case VIEW_ELEM:
                if (getArchive()){
                    viewElemInArchive();
                }
                break;
            case DELETE_IN_ARCHIVE:
                if (getArchive()){
                    deleteElemInArchive();
                }
                break;
            case UPDATE_IN_ARCHIVE:
                if (getArchive()){
                    updateElemInArchive();
                }
                break;
            case UPDATE_USER:
                if (getUsers()) {
                    updateUser();
                }
                break;
        }
    }

    public static byte[] getPasswordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    public static UserDTO createUser(String login, String password, User.Roles role) throws NoSuchAlgorithmException {
        return new UserDTO(login,getPasswordHash(password),role);
    }

    public static UserDTO createUser(String login, String password, User.Roles role,Long token) throws NoSuchAlgorithmException {
        return new UserDTO(login,getPasswordHash(password),role,token);
    }

    private String getInput(){
        String input;
        do{
            input= sc.nextLine();
        }
        while (input.trim().matches("[\\s\\n\\t\\r]*"));
        return  input;
    }

    private void register() throws NoSuchAlgorithmException {
        var message=new Message(Message.CommandEn.REGISTER_USER,token);

        System.out.println("Enter login");
        var l=getInput();

        System.out.println("Enter password");
        var p=getInput();

        message.setValue(createUser(l,p, User.Roles.SIMPLE_USER,token));

        if (sendRecive.send(message)){
            Message result= sendRecive.receive();
            if (result!=null && result.getCommand()== Message.CommandEn.SUCCESS){
                System.out.println("Registration successful");
                return;
            }

        }
        System.out.println("Registration failed");
    }

    private void enter() throws NoSuchAlgorithmException {
        var message=new Message(Message.CommandEn.AUTHORIZE_USER,token);


        System.out.println("Enter login");
        var l=getInput();

        System.out.println("Enter password");
        var p=getInput();

        message.setValue(createUser(l,p, User.Roles.SIMPLE_USER));

        if (sendRecive.send(message)){
            Message result= sendRecive.receive();
            if (result!=null && result.getCommand()== Message.CommandEn.SUCCESS){
                System.out.println("Authorization successful");
                token=(Long) result.getValue();
                return;
            }
        }
        System.out.println("Authorization failed");
    }

    private boolean getArchive() {
        var message=new Message(Message.CommandEn.GET_ARCHIVE,token);
        if (sendRecive.send(message)){
            Message result= sendRecive.receive();
            if (result!=null && result.getCommand()== Message.CommandEn.SUCCESS){
                System.out.println("Operation successful");
                currentArchive = ((Wrapper<ArchiveElementDTO>)result.getValue()).getTable();
                int i=1;
                var context=new String[]{"name","age","city","grade"};
                for (var inf:currentArchive ) {
                    System.out.println(i+": "+inf.toString(context));
                    i++;
                }
                return true;
            }
        }
        System.out.println("Operation failed");
        return false;
    }

    private void viewElemInArchive() {
        var message=new Message(Message.CommandEn.VIEW_ELEM,token);

        System.out.println("Enter index of search element");
        int ind=sc.nextInt()-1;
        try {
            message.setValue(currentArchive.get(ind));
            if (sendRecive.send(message)) {
                Message result = sendRecive.receive();
                if (result != null && result.getCommand() == Message.CommandEn.SUCCESS) {
                    System.out.println("Operation successful");
                    System.out.println(result.getValue());
                    return;
                }
            }
        }catch (Exception ignore){
            //ignore.printStackTrace();
        }
        System.out.println("Operation failed");
    }

    private void addElemInArchive()  {
        var message=new Message(Message.CommandEn.ADD_TO_ARCHIVE,token);
        ArchiveElementDTO newElem=new ArchiveElementDTO();

        System.out.println("\n\t(don't forget that link starts with https:// and name of web site with dot\n\t\t(example https://archive.com/pathToUaerDescription))\n"+
                "Don't forget that 1<=grade<=11, 6<=age<=120, 1<=rating<=10\n"+
                "onScholarship -- yes or no");

        String[] criteria={"name","country","city","street","house","grade","descriptionLink","age","rating","onScholarship","scholarship"};

        for (String criterion : criteria) {
            System.out.println("Enter " + criterion);
            newElem.addField(criterion, getInput());
        }

        message.setValue(newElem);

        if (sendRecive.send(message)){
            Message result= sendRecive.receive();
            if (result!=null && result.getCommand()== Message.CommandEn.SUCCESS){
                System.out.println("Operation successful");
                return;
            }
        }
        System.out.println("Operation failed");
    }

    private void updateElemInArchive() {
        var message=new Message(Message.CommandEn.UPDATE_IN_ARCHIVE,token);

        System.out.println("Enter index of update element");
        int ind=sc.nextInt()-1;


        System.out.println("\n\t(don't forget that link starts with https:// and name of web site with dot\n\t\t(example https://archive.com/pathToUaerDescription))\n"+
                "Don't forget that 1<=grade<=11, 6<=age<=120, 1<=rating<=10\n"+
                "onScholarship -- yes or no");
        ArchiveElementDTO newElem=new ArchiveElementDTO();
        String[] criteria={"name","country","city","street","house","grade","descriptionLink","age","rating","onScholarship","scholarship"};

        for (String criterion : criteria) {
            System.out.println("Enter " + criterion);
            newElem.addField(criterion, getInput());
        }

        message.setValue(new Pair<ArchiveElementDTO>(currentArchive.get(ind),newElem));

        if (sendRecive.send(message)){
            Message result= sendRecive.receive();
            if (result!=null && result.getCommand()== Message.CommandEn.SUCCESS){
                System.out.println("Operation successful");
                return;
            }
        }
        System.out.println("Operation failed");
    }

    private boolean getUsers() {
        var message=new Message(Message.CommandEn.GET_USERS,token);
        if (sendRecive.send(message)){
            Message result= sendRecive.receive();
            if (result!=null && result.getCommand()== Message.CommandEn.SUCCESS){
                System.out.println("Operation successful");
                var users = ((Wrapper<UserDTO>)result.getValue()).getTable();

                for (var inf:users ) {
                    System.out.println(inf.toString());
                }
                return true;
            }
        }
        System.out.println("Operation failed");
        return false;
    }

    private void updateUser() throws NoSuchAlgorithmException {
        var message=new Message(Message.CommandEn.UPDATE_USER,token);

        System.out.println("Enter login of update user");
        var oldElem=new UserDTO();
        oldElem.setLogin(getInput());

        System.out.println("Enter new login");
        var l=getInput();

        System.out.println("Enter new password");
        var p=getInput();


        System.out.println("Enter new role");
        EnumSet<User.Roles> roles=EnumSet.allOf(User.Roles.class);
        for (var c:roles ) {
            System.out.println("\t"+c);
        }
        User.Roles r=User.Roles.SIMPLE_USER;
        try {
            r=User.Roles.valueOf(getInput());
        }catch (Exception ignore){
            //ignore.printStackTrace();}
        }

        message.setValue(new Pair<UserDTO>(oldElem,createUser(l,p,r)));

        if (sendRecive.send(message)){
            Message result= sendRecive.receive();
            if (result!=null && result.getCommand()== Message.CommandEn.SUCCESS){
                System.out.println("Operation successful");
                return;
            }
        }
        System.out.println("Operation failed");
    }

    private void deleteElemInArchive() {
        var message=new Message(Message.CommandEn.DELETE_IN_ARCHIVE,token);

        System.out.println("Enter index of delete element");
        int ind=sc.nextInt()-1;

        message.setValue(currentArchive.get(ind));

        if (sendRecive.send(message)){
            Message result= sendRecive.receive();
            if (result!=null && result.getCommand()== Message.CommandEn.SUCCESS){
                System.out.println("Operation successful");
                return;
            }
        }
        System.out.println("Operation failed");
    }

}

