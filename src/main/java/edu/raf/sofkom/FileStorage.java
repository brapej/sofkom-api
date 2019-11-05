package edu.raf.sofkom;



import edu.raf.sofkom.privileges.PrivilegeException;
import edu.raf.sofkom.users.StorageUsers;
import lombok.*;

import java.io.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;


@Data
@SuppressWarnings("unused")
 public abstract class FileStorage implements Serializable {


    private StorageUsers storageUsers = new StorageUsers();
    private  String pathToStorage = Paths.get("./").toString();
    private String pathToDownloads= Paths.get(System.getProperty("user.home"),"storage-downloads").toString();
    private String currentPath = Paths.get("./").toString();
    private HashSet<String> filetypeRestrictions = new HashSet<>();





    public FileStorage(){

    }
    /*public FileStorage(String userName,String password) {
        storageUsers = new StorageUsers(userName,password);
    }*/


    public abstract void init(String pathToStorage,String storageName) throws IOException;
    public abstract boolean store(String to, String from) throws  IOException, PrivilegeException;
    public abstract boolean store(String to, String... from) throws  IOException,PrivilegeException;
    public abstract boolean store(Path to, Path from) throws IOException, PrivilegeException;
    public abstract boolean store(Path to, Path... from) throws IOException,PrivilegeException;
    public abstract boolean retrieve(Path from) throws PrivilegeException,IOException;
   public abstract boolean retrieve(String from) throws PrivilegeException, IOException;
   public abstract boolean delete(String path) throws PrivilegeException,IOException;
    public abstract boolean delete(Path toDelete) throws PrivilegeException,IOException;



   public boolean addFiletypeRestriction(String ft) throws PrivilegeException{

      if(storageUsers.ifSuperUser())
         throw new PrivilegeException("Permission denied,current user not superuser.");

      if(ft.charAt(0) != '.') {
         ft = ".".concat(ft);
      }
      ft = getFileExtension(new File(ft));
      if(!ft.equals("") && !filetypeRestrictions.contains(ft)){
      filetypeRestrictions.add(ft);
      return true;
      }
         return false;
   }

    public boolean removeFiletypeRestriction(String ft) throws PrivilegeException{

        if(storageUsers.ifSuperUser())
            throw new PrivilegeException("Permission denied,current user not superuser.");

        if(ft.charAt(0) != '.') {
            ft = ".".concat(ft);
        }
        ft = getFileExtension(new File(ft));
        if(!ft.equals("") && !filetypeRestrictions.contains(ft)){
            filetypeRestrictions.remove(ft);
            return true;
        }
        return false;
    }


      private String getFileExtension(File file) {
         String name = file.getName();
         int lastIndexOf = name.lastIndexOf(".");
         if (lastIndexOf == -1) {
            return "";
         }
         return name.substring(lastIndexOf);
      }


      public String getUserString(String userName){
      return this.storageUsers.getUsers().get(userName).toString();
      }

   protected    Path toStoragePath(String path) {
      return toStoragePath(Paths.get(path));
   }
   protected Path toStoragePath(Path path) {
      return Paths.get(this.pathToStorage).resolve(path.getFileName());
   }







}


