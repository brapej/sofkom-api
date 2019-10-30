package edu.raf.sofkom.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;

public class User implements Serializable {

    private String userName;
    private String password;
    private HashSet<Privilege> privileges = new HashSet<>();
    private static int userCount=0;

    public User(){
       userCount++;
       String nameBase = "user";
       this.userName = nameBase + Integer.toString(userCount);
    }

    public User(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }

    /*UserName*/

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    ////////////


    /*Password*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //////////////

    /*Privileges*/

    public HashSet<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(HashSet<Privilege> privileges) {
        this.privileges = privileges;
    }

    public boolean checkPrivilege(Privilege p){
        return getPrivileges().contains(p);
    }
    /////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", privileges=" + privileges +
                '}';
    }
}
