package edu.raf.sofkom;


import edu.raf.sofkom.model.Privilege;
import edu.raf.sofkom.model.User;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class FileStorage implements Serializable {



    private  User superUser;
    private  User currentUser;
    //private String currentPath;
    private  String pathToStorage = Paths.get("").toString();
    private String pathToDownloads= Paths.get(System.getProperty("user.home"),"storage-downloads").toString();



    private HashMap<String,User> users;
    private HashSet<String> extRestrictions = new HashSet<>();

    public FileStorage(){
        this.superUser= new User("admin","admin");
        this.currentUser=superUser;
        this.users = new HashMap<String, User>();
        users.put(superUser.getUserName(),superUser);
        HashSet<Privilege> suPriv = new HashSet<>();
        suPriv.add(Privilege.D);
        suPriv.add(Privilege.S);
        suPriv.add(Privilege.R);
        this.superUser.setPrivileges(suPriv);

    }

    public FileStorage(String userName,String password) {
        users = new HashMap<String, User>();
        User newSu = new User(userName,password);
        this.superUser = newSu;
        HashSet<Privilege> suPriv = new HashSet<>();
        suPriv.add(Privilege.D);
        suPriv.add(Privilege.S);
        suPriv.add(Privilege.R);
        this.currentUser = newSu;
        this.superUser.setPrivileges(suPriv);
        users.put(newSu.getUserName(),newSu);


    }

    public abstract boolean init(String pathToStorage) throws FileAlreadyExistsException;
    public abstract boolean store(String to, String from) throws  PrivilegeException,NoUserException;
    public abstract boolean store(String to, String... from) throws  PrivilegeException,NoUserException;
    public abstract boolean store(Path to, Path from) throws PrivilegeException,NoUserException;
    public abstract boolean store(Path to, Path... from) throws PrivilegeException,NoUserException;
    public abstract boolean retrieve(String from) throws PrivilegeException,NoUserException;
    public abstract boolean delete(String path) throws PrivilegeException,NoUserException;
    public abstract boolean delete(Path toDelete) throws PrivilegeException,NoUserException;



    public void disconnect(User user){
        currentUser = null;
    }

    /*********** SuperUser *********/

    protected User getSuperUser() {
        return superUser;
    }

    protected void setSuperUser(User superUser) {
        this.superUser = superUser;
    }

    protected boolean isSuperUser(User user)
    {
        return getUsers().containsKey(user.getUserName());
    }
    ////////////////////////////////



    /******** CurrentUser **************/
    protected User getCurrentUser() {
        return currentUser;
    }

    protected void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    ////////////////////////////////////

    /********StoragePaths********/

    public /*Path*/String getPathToStorage() {
        return pathToStorage;
    }

    public void setPathToStorage(/*Path*/String pathToStorage) {
        this.pathToStorage = pathToStorage/*.toString()*/;
    }

    public /*Path*/String getPathToDownloads() {
        return pathToDownloads;
    }

    public void setPathToDownloads(/*Path*/String pathToDownloads) {
        this.pathToDownloads = pathToDownloads/*.toString()*/;
    }

    ////////////////////////////////////




    /*********  Users  *************/
    protected HashMap<String, User> getUsers() {
        return users;
    }

    protected void setUsers(HashMap<String, User> users) {
        this.users = users;
    }


    /**Method to add user by
     * @param userName Username for newly created user.
     * @param password Password for newly created user.
     * @return {@code true} on success, otherwise{@code false}.
     * @throws PrivilegeException if current user isnt privileged for operation.
     *
     * */
    public boolean addUser(String userName,String password) throws PrivilegeException,NoUserException
    {
        return addUser(new User(userName,password));
    }

    /**Method to add multiple users to storage base.
     * @param user User to add.
     * @return {@code true} on success, otherwise{@code false}.
     * @throws PrivilegeException if current user isnt privileged for operation.
     *
     * */

    public boolean addUser(User user) throws PrivilegeException,NoUserException
    {

        if(!isSuperUser(user))
            throw new PrivilegeException("User not superuser.");

            if(!this.users.containsKey(user.getUserName())){
                this.users.put(user.getUserName(), user);
                return true;}

            return false;
    }

    public boolean removeUser(String userName) throws PrivilegeException,NoUserException{

        if(currentUser == null)
            throw new NoUserException("No user.");

        if(!currentUser.equals(superUser))
            throw new PrivilegeException("Not allowed.");

        if(users.containsKey(userName))
            return false;
        users.remove(userName);
        return true;
    }

    public boolean removeUser(User user) throws PrivilegeException,NoUserException {
    return removeUser(user.getUserName());
    }

    /**Method to add multiple users to storage base.
     * @param users User/s to add.
     * @return {@code ArrayList<Users>} containing users that are already registered.
     *
     * */

    public ArrayList<User> addUsers(User... users) throws PrivilegeException,NoUserException{

        ArrayList<User> returnList= new ArrayList<>();
        if(isSuperUser(currentUser)) {

            for (User u : users) {
                if(!addUser(u))
                    returnList.add(u);
            }
        }
        return returnList;
    }

    ////////////////////////////

     public   Path storageRelativePath(String path) {
        return storageRelativePath(Paths.get(path));
    }
     public   Path storageRelativePath(Path path) {
            Path resolved = Paths.get/**/(this.pathToStorage).resolve(path.getFileName().normalize());
            return resolved;
     }

    @Override
    public String toString() {
        return "FileStorage{" +
                "superUser=" + superUser +
                ", currentUser=" + currentUser +
                ", pathToStorage='" + pathToStorage + '\'' +
                ", pathToDownloads='" + pathToDownloads + '\'' +
                ", users=" + users +
                '}';
    }

}
