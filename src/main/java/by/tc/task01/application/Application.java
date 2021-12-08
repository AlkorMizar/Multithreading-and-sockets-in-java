package by.tc.task01.application;

import by.tc.task01.application.dto.ArchiveElementDTO;
import by.tc.task01.application.dto.UserDTO;
import by.tc.task01.dao.ArchiveDAO;
import by.tc.task01.dao.DAOFactory;
import by.tc.task01.entity.impl.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Application {
    private final ArchiveDAO dao;
    private final ConcurrentHashMap<Long,User> activeUsers;


    public Application(){
        activeUsers=new ConcurrentHashMap<Long,User>();
        dao= DAOFactory.getInstance().getApplianceDAO();

    }

    public boolean register(UserDTO userDTO){
        var us=userDTO.convert();
        if(us.isCorrect()){
            return dao.add(us, ArchiveDAO.TablesEn.USERS);
        }
        return false;
    }

    public Long authorize(UserDTO userDTO,Long currentToken){
        removeInactiveUser(currentToken);
        var user=userDTO.convert();
        if (user.hasCriteria("login")) {
            var result = dao.find(new String[]{"login"}, user, ArchiveDAO.TablesEn.USERS);
            if (result.isPresent()) {
                User us = (User) result.get();
                if (Arrays.compare(us.getHashPassword(), user.getHashPassword()) == 0) {
                    Random rn = new Random();
                    Long token;
                    do {
                        token= rn.nextLong();
                    }while (token==0L);
                    activeUsers.put(token, us);
                    return token;
                }
            }

        }
        return -1L;
    }

    private  void removeInactiveUser(Long token){
        if(token!=-1){
            activeUsers.remove(token);
        }
    }

    public List<ArchiveElementDTO> viewArchive(Long token){
        if (activeUsers.containsKey(token)) {
            var us = activeUsers.get(token);
            if (us.hasCriteria("role") && us.getRole().CanDo(User.Uses.VIEW, ArchiveDAO.TablesEn.ARCHIVE)) {
                var archive = dao.getAll(ArchiveDAO.TablesEn.ARCHIVE);
                var result = new LinkedList<ArchiveElementDTO>();
                int i=0;
                for (var elem: archive) {
                    result.add(new ArchiveElementDTO(elem));
                }
                return result;
            }
        }
        return null;
    }

    public ArchiveElementDTO getElemOfArch(Long token,ArchiveElementDTO archiveElementDTO){
        if (activeUsers.containsKey(token) && activeUsers.get(token).getRole().CanDo(User.Uses.VIEW, ArchiveDAO.TablesEn.ARCHIVE)){
            var elem=archiveElementDTO.convert();
            String[] searchWith={"name","country","city","street","house"};
            var r=dao.find(searchWith,elem, ArchiveDAO.TablesEn.ARCHIVE);
            if (r.isPresent()){
                return new ArchiveElementDTO(r.get());
            }
        }
        return null;
    }

    public boolean deleteElemOfArch(Long token,ArchiveElementDTO archiveElementDTO){
        if (activeUsers.containsKey(token) && activeUsers.get(token).getRole().CanDo(User.Uses.REMOVE, ArchiveDAO.TablesEn.ARCHIVE)){
            var elem=archiveElementDTO.convert();
            String[] searchWith={"name","country","city","street","house"};
            var r=dao.find(searchWith,elem, ArchiveDAO.TablesEn.ARCHIVE);
            if (r.isPresent()){
                return dao.delete(r.get(), ArchiveDAO.TablesEn.ARCHIVE);
            }
        }
        return false;
    }

    public boolean addElemOfArch(Long token,ArchiveElementDTO archiveElementDTO){
        if (activeUsers.containsKey(token) && activeUsers.get(token).getRole().CanDo(User.Uses.CREATE, ArchiveDAO.TablesEn.ARCHIVE)){
            var elem=archiveElementDTO.convert();
            if (elem.isCorrect()){
                return dao.add(elem, ArchiveDAO.TablesEn.ARCHIVE);
            }
        }
        return false;
    }

    public boolean updateElemOfArch(Long token,ArchiveElementDTO oldDTO,ArchiveElementDTO newElemDTO){
        if (activeUsers.containsKey(token) && activeUsers.get(token).getRole().CanDo(User.Uses.EDIT, ArchiveDAO.TablesEn.ARCHIVE)){
            var old=oldDTO.convert();
            var newElem=newElemDTO.convert();
            String[] searchWith={"name","country","city","street","house"};
            var r=dao.find(searchWith,old, ArchiveDAO.TablesEn.ARCHIVE);
            if (r.isPresent() && newElem.isCorrect()){
                return dao.update(r.get(),newElem, ArchiveDAO.TablesEn.ARCHIVE);
            }
        }
        return false;
    }

    public  boolean updateUser(Long token,UserDTO oldDTO,UserDTO newElemDTO){
        if (activeUsers.containsKey(token) && activeUsers.get(token).getRole().CanDo(User.Uses.EDIT, ArchiveDAO.TablesEn.USERS)){
            var old=oldDTO.convert();
            var newElem=newElemDTO.convert();
            String[] searchWith={"login"};
            var r=dao.find(searchWith,old, ArchiveDAO.TablesEn.USERS);
            if (r.isPresent() && newElem.isCorrect()){
                return dao.update(r.get(),newElem, ArchiveDAO.TablesEn.USERS);
            }
        }
        return false;
    }

    public List<UserDTO> viewUsers(Long token){
        if (activeUsers.containsKey(token)) {
            var us = activeUsers.get(token);
            if (us.hasCriteria("role") && us.getRole().CanDo(User.Uses.VIEW, ArchiveDAO.TablesEn.USERS)) {
                var archive = dao.getAll(ArchiveDAO.TablesEn.USERS);
                var result = new LinkedList<UserDTO>();
                int i=0;
                for (var elem: archive) {
                    result.add(new UserDTO(elem));
                }
                return result;
            }
        }
        return null;
    }

}
