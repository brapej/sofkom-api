package edu.raf.sofkom.model;

import edu.raf.sofkom.privileges.Privilege;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;


@ToString(includeFieldNames=true)

@Data public class User implements Serializable,Comparable<User> {


    private String userName = null;
    private String password = null;
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

    public boolean checkPrivilege(Privilege p){
        return getPrivileges().contains(p);
    }

    public void addPrivileges(Privilege...privileges){
        addPrivileges(Arrays.asList(privileges));
    }

    public void addPrivileges(Collection<Privilege> privileges){
        this.privileges.addAll(privileges);
    }

    public void removePrivileges(Privilege...privileges){
        removePrivileges(Arrays.asList(privileges));
    }

    public void removePrivileges(Collection<Privilege> privileges){
        this.privileges.removeAll(privileges);
    }


    @Override
    public int compareTo(User o) {
        final int compare = Integer.compare(userName.compareTo(o.getUserName()), 0);
        return compare;
    }

    public String toString(){
        return "Username: " +userName + "\n" + privileges;
    }

}
