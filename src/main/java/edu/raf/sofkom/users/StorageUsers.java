package edu.raf.sofkom.users;

import edu.raf.sofkom.privileges.Privilege;
import edu.raf.sofkom.model.User;
import edu.raf.sofkom.privileges.PrivilegeException;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

@Data public class StorageUsers implements Serializable {

    private User superUser;
    private User currentUser;
    private HashMap<String,User> users = new HashMap<>();

     public StorageUsers(){
        superUser = new User("admin","admin");
        currentUser = superUser;
        users.put(superUser.getUserName(),superUser);
    }

    private StorageUsers(String username, String password){
        this.superUser = new User(username,password);
        this.superUser.addPrivileges(Privilege.S,Privilege.D,Privilege.R);
        this.currentUser = superUser;
        this.users.put(superUser.getUserName(),superUser);

    }




   public boolean ifSuperUser()
    {
        return getCurrentUser().equals(superUser);
    }


    /**Method to add multiple users to storage base.
     * @param user User to add.
     * @return {@code true} on success, otherwise{@code false}.
     * @throws PrivilegeException if current user isnt privileged for operation.
     *
     * */

    public boolean addUser(User user,Privilege...privileges) throws PrivilegeException {
        if(!ifSuperUser())
            throw new PrivilegeException("Permission denied,current user not superuser.");

        if(!this.users.containsKey(user.getUserName())){
            user.addPrivileges(privileges);
            addUser(user);
            return true;}

        return false;
    }


    public void removeUserPrivilege(String userName,Privilege p){
        users.get(userName).removePrivileges(p);
    }

    private void removeUserPrivilege(User user,Privilege p){
        removeUserPrivilege(user.getUserName(),p);
    }

    public void addUserPrivilege(String userName,Privilege p){
        users.get(userName).addPrivileges(p);
    }

    private void addUserPrivilege(User user,Privilege p){
        addUserPrivilege(user.getUserName(),p);
    }

    public boolean addUser(User user, Collection<Privilege> privileges) throws PrivilegeException {

        if(!this.users.containsKey(user.getUserName())){
            user.addPrivileges(privileges);
            addUser(user);
            return true;}

        return false;
    }

    public boolean addUser(User user) throws PrivilegeException {
        if(!ifSuperUser())
            throw new PrivilegeException("Permission denied,current user not superuser.");


        if(!this.users.containsKey(user.getUserName())){
            this.users.put(user.getUserName(), user);
            return true;}

        return false;
    }

    /**Method to add user by
     * @param userName Username for newly created user.
     * @param password Password for newly created user.
     * @return {@code true} on success, otherwise{@code false}.
     * @throws PrivilegeException if current user isnt privileged for operation.
     *
     * */
    public boolean addUser(String userName,String password) throws PrivilegeException {
        return addUser(new User(userName,password));
    }

    public boolean addUser(String userName,String password,Collection<Privilege> privileges) throws PrivilegeException {
        return addUser(new User(userName,password),privileges);
    }

    public boolean addUser(String userName,String password,Privilege...privileges) throws PrivilegeException {
        return addUser(new User(userName,password),privileges);
    }

    /**Method to add user by
     * @param userName Username for newly created user.
     * @return {@code true} on success, otherwise{@code false}.
     * @throws PrivilegeException if current user isnt privileged for operation.
     *
     * */


    public boolean removeUser(String userName) throws PrivilegeException {



        if(!ifSuperUser())
            throw new PrivilegeException("Permission denied,current user not superuser.");



        if(users.containsKey(userName)) {
            users.remove(userName);
            return true;
        }
        return false;

    }

    public boolean removeUser(User user) throws PrivilegeException{
        return removeUser(user.getUserName());
    }


    public StorageUsers init(String sUserName,String sUserPwd){
       return   new StorageUsers(sUserName,sUserPwd);
    }






}
