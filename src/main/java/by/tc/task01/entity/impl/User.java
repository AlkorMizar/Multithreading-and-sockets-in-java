package by.tc.task01.entity.impl;

import by.tc.task01.dao.ArchiveDAO;
import by.tc.task01.entity.TableElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.EnumSet;
import java.util.HashMap;

@XmlRootElement
public class User extends TableElement {
    public static enum Uses{VIEW,EDIT,CREATE,REMOVE,WORK_WITH_USER_INFO}
    public static enum Roles{
        SIMPLE_USER(EnumSet.of(Uses.VIEW),EnumSet.noneOf(Uses.class)),
        ADMIN(EnumSet.allOf(Uses.class),EnumSet.allOf(Uses.class)),
        EDITOR(EnumSet.of(Uses.VIEW,Uses.CREATE,Uses.EDIT),EnumSet.noneOf(Uses.class));

        private HashMap<ArchiveDAO.TablesEn,EnumSet<Uses>> canDo;

        Roles(EnumSet<Uses> canDoArchive,EnumSet<Uses> canDoUsers){
            canDo=new HashMap<>();
            canDo.put(ArchiveDAO.TablesEn.ARCHIVE,canDoArchive);
            canDo.put(ArchiveDAO.TablesEn.USERS,canDoUsers);
        }

        public boolean CanDo(Uses use, ArchiveDAO.TablesEn tablesEn){return canDo.get(tablesEn).contains(use);}
    }
    private String login;
    private byte[] hashPassword;
    private Roles role;

    public User(int primaryKey, String login, byte[] hashPassword, Roles role) {
        values=new HashMap<>();
        setPrimaryKey(primaryKey);
        setLogin(login);
        setHashPassword(hashPassword);
        setRole(role);
    }

    public User() {
        values=new HashMap<>();
    }
    @XmlElement
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if(login.matches("\\w+")){
            values.put("login",login);
            this.login = login;
        }
    }
    @XmlElement
    public byte[] getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(byte[] hashPassword) {
        if (hashPassword!= null && hashPassword.length==32){
            values.put("hashPassword",hashPassword);
            this.hashPassword = hashPassword;
        }
    }
    @XmlElement
    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        if (role!=null){
            values.put("role",role);
            this.role = role;
        }
    }

    @Override
    public void update(TableElement element) {
        if(element instanceof User){
            var el=(User) element;
            setPrimaryKey(el.primaryKey);
            setLogin(el.login);
            setHashPassword(el.hashPassword);
            setRole(el.role);
        }
    }

    @Override
    public HashMap<String, Object> getValues() {
        return (HashMap<String, Object>) values.clone();
    }

    @Override
    public Object getValue(String criteria) {
        return values.getOrDefault(criteria,null);
    }

    @Override
    public boolean hasCriteria(String criteria) {
        return values.containsKey(criteria);
    }

    @Override
    public boolean isCorrect() {
        return containAllCriteria();
    }

    @Override
    protected boolean containAllCriteria() {
        String[] criteria={"login","hashPassword","role"};
        for (var cr: criteria ) {
            if (!values.containsKey(cr)){
                return false;
            }
        }
        return true;
    }
}
