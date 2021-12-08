package by.tc.task01.application.dto;

import by.tc.task01.entity.TableElement;
import by.tc.task01.entity.impl.User;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDTO {
    private String login;
    private Long token;
    User.Roles roles;
    private byte[] hashPassword;

    public UserDTO() {
    }

    public UserDTO(TableElement element){
        if(element instanceof User){
            var user=(User) element;
            login = user.getLogin();
            token = -1L;
            roles = user.getRole();
            hashPassword=null;
        }
    }

    public UserDTO(String login, byte[] hashPassword, User.Roles roles) {
        this(login,hashPassword,roles,-1L);
    }

    public UserDTO(String login, byte[] hashPassword, User.Roles roles,Long token) {
        this.login = login;
        this.hashPassword=hashPassword;
        this.roles=roles;
        this.token=token;
    }

    @XmlElement
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @XmlElement
    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    @XmlElement
    public byte[] getHashpassword() {
        return hashPassword;
    }

    public User convert(){
        return  new User(0,login,hashPassword,roles);
    }

    public void setHashpassword(byte[] hashpassword) {
        this.hashPassword = hashpassword;
    }

    @XmlElement
    public User.Roles getRoles() {
        return roles;
    }

    public String toString(){
        return  "Login: "+login+", "+roles;
    }

    public void setRoles(User.Roles roles) {
        this.roles = roles;
    }

    public void addField(String cr,Object val){
        switch (cr){
            case "login":
                login=(String) val;
                break;
            case "roles":
                roles= User.Roles.SIMPLE_USER;
                try {
                    roles=User.Roles.valueOf((String)val);
                }catch (Exception ignore){}
                break;
            case "hashPassword":
                hashPassword=(byte[]) val;
                break;
        }
    }
}
