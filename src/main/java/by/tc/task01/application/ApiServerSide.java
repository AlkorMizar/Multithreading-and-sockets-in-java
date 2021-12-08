package by.tc.task01.application;

import by.tc.task01.application.dto.ArchiveElementDTO;
import by.tc.task01.application.dto.UserDTO;
import by.tc.task01.application.dto.Wrapper;
import by.tc.task01.connection.Message;
import by.tc.task01.connection.Pair;
import by.tc.task01.connection.SendRecive;

public class ApiServerSide extends Thread {
    SendRecive sendRecive;
    Application app;
    Long token;

    public ApiServerSide(SendRecive sendRecive) {
        token=-1L;
        this.sendRecive=sendRecive;
        app= AplicationFactory.getInstance().getApplication();
    }

    @Override
    public void run(){
        try{
            do {
                var received=sendRecive.receive();
                if(received.getCommand()== Message.CommandEn.CLOSE_CONNECTION){
                    return;
                }
                received=process(received);
                sendRecive.send(received);
            }while (true);
        }catch (Exception e){
           // e.printStackTrace();
            System.out.println("User disconnected");
        }

    }

    private Message process(Message ms){
        var result=new Message();
        switch (ms.getCommand()){
            case REGISTER_USER:{
                if (app.register((UserDTO) ms.getValue())){
                    result.setCommand(Message.CommandEn.SUCCESS);
                }else {
                    result.setCommand(Message.CommandEn.FAILED);
                }
                break;
            }
            case AUTHORIZE_USER:{
                token=app.authorize((UserDTO) ms.getValue(),ms.getToken());
                result.setValue(token);
                if (token!=-1L){
                    result.setCommand(Message.CommandEn.SUCCESS);
                }else {
                    result.setCommand(Message.CommandEn.FAILED);
                }
                break;
            }
            case GET_ARCHIVE:{
                var archive=new Wrapper<ArchiveElementDTO>(app.viewArchive(ms.getToken()));
                result.setValue(archive);
                if (archive.table!=null){
                    result.setCommand(Message.CommandEn.SUCCESS);
                }else {
                    result.setCommand(Message.CommandEn.FAILED);
                }
                break;
            }
            case VIEW_ELEM:{
                var elem=app.getElemOfArch(ms.getToken(),(ArchiveElementDTO) ms.getValue());
                result.setValue(elem);
                if (elem!=null){
                    result.setCommand(Message.CommandEn.SUCCESS);
                }else {
                    result.setCommand(Message.CommandEn.FAILED);
                }
                break;
            }
            case ADD_TO_ARCHIVE:{
                if (app.addElemOfArch(ms.getToken(),(ArchiveElementDTO)  ms.getValue())){
                    result.setCommand(Message.CommandEn.SUCCESS);
                }else {
                    result.setCommand(Message.CommandEn.FAILED);
                }
                break;
            }
            case DELETE_IN_ARCHIVE:{
                if (app.deleteElemOfArch(ms.getToken(),(ArchiveElementDTO)  ms.getValue())){
                    result.setCommand(Message.CommandEn.SUCCESS);
                }else {
                    result.setCommand(Message.CommandEn.FAILED);
                }
                break;
            }
            case UPDATE_IN_ARCHIVE:{
                var recivedVal=(Pair<ArchiveElementDTO>)ms.getValue();
                if (app.updateElemOfArch(ms.getToken(),recivedVal.getFirst(),recivedVal.getSecond())){
                    result.setCommand(Message.CommandEn.SUCCESS);
                }else {
                    result.setCommand(Message.CommandEn.FAILED);
                }
                break;
            }
            case UPDATE_USER:{
                var recivedVal=(Pair<UserDTO>)ms.getValue();
                if (app.updateUser(ms.getToken(),recivedVal.getFirst(),recivedVal.getSecond())){
                    result.setCommand(Message.CommandEn.SUCCESS);
                }else {
                    result.setCommand(Message.CommandEn.FAILED);
                }
                break;
            }

            case GET_USERS:{
                var users=new Wrapper<UserDTO>(app.viewUsers(ms.getToken()));
                result.setValue(users);
                if (users.table!=null){
                    result.setCommand(Message.CommandEn.SUCCESS);
                }else {
                    result.setCommand(Message.CommandEn.FAILED);
                }
                break;
            }
        }
        return result;
    }
}
