package edu.raf.sofkom;



import edu.raf.sofkom.privileges.PrivilegeException;
import edu.raf.sofkom.users.StorageUsers;
import lombok.*;

import java.io.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Data
@SuppressWarnings("unused")
 public abstract class FileStorage implements Serializable {


    private StorageUsers storageUsers = new StorageUsers();
    private  String pathToStorage = Paths.get("./").toString();
    private String pathToDownloads= Paths.get(System.getProperty("user.home"),"storage-downloads").toString();
    private String currentPath = Paths.get("./").toString();
    private HashSet<String> filetypeRestrictions = new HashSet<>();
    private HashMap<String,HashMap<String,String>> filesMeta = new HashMap<>();



    public FileStorage(){

    }
    /*public FileStorage(String userName,String password) {
        storageUsers = new StorageUsers(userName,password);
    }*/



    /**
     * Initialises storage on a given path with a given name.
     * @param pathToStorage Path on witch the storage will be created
     * @param storageName Name of the folder(storage)
     * */
    public abstract void init(String pathToStorage,String storageName) throws IOException;
    /**
     * Stores a file from one given path to the other.
     * @param from this path
     * @param to this path witch is relative to storage.
     * */
    public abstract boolean store(String to, String from) throws  IOException, PrivilegeException, UnsuportedTypeException;
    /**
     * Stores a file from multiple given paths to the other.
     * @param from this path's
     * @param to this path witch is relative to storage.
     * */
    public abstract boolean store(String to, String... from) throws  IOException,PrivilegeException, UnsuportedTypeException;
    /**
     * Stores a file from multiple given paths to the other.
     * @param from this path
     * @param to this path witch is relative to storage.
     * */
    public abstract boolean store(Path to, Path from) throws IOException, PrivilegeException, UnsuportedTypeException;
    /**
     * Stores a file from multiple given paths to the other.
     * @param from this path's
     * @param to this path witch is relative to storage.
     * */
    public abstract boolean store(Path to, Path... from) throws IOException,PrivilegeException, UnsuportedTypeException;
    /**
     * Retrieves a file
     * @param from this path's
     * */
    public abstract boolean retrieve(Path from) throws PrivilegeException,IOException;
    /**
     * Retrieves a file
     * @param from this path's
     * */
   public abstract boolean retrieve(String from) throws PrivilegeException, IOException;
    /**
     * Deletes a file from the given path
     * @param path path
     * */
   public abstract boolean delete(String path) throws PrivilegeException,IOException;
    /**
     * Deletes a file from the given path
     * @param path path
     * */
    public abstract boolean delete(Path path) throws PrivilegeException,IOException;


    /**
     * Stores a file and its metadata from the given path
     * @param from this path
     * @param to this path
     * @param meta with given metadata.
     * */
    public void store(String to,String from,HashMap<String,String> meta) throws IOException, PrivilegeException, UnsuportedTypeException {
        if(store(to,from))
            filesMeta.put(Paths.get(to, String.valueOf(Paths.get(from).getFileName())).toString(),meta);
    }

    public String readFileMeta(String filePath){
       return filesMeta.get(filePath).toString();
    }

    /**
     * Restricts filetype to a storage
     * @param extension filetype extension (.txt ,.xml , .bat...)
     * */
   public boolean addFiletypeRestriction(String extension) throws PrivilegeException{

      if(storageUsers.ifSuperUser())
         throw new PrivilegeException("Permission denied,current user not superuser.");

      if(extension.charAt(0) != '.') {
         extension = ".".concat(extension);
      }
      extension = getFileExtension(new File(extension));
      if(!extension.equals("") && !filetypeRestrictions.contains(extension)){
      filetypeRestrictions.add(extension);
      return true;
      }
         return false;
   }

    /**
     * Unrestricts filetype to a storage
     * @param extension filetype extension (.txt ,.xml , .bat...)
     * */
    public boolean removeFiletypeRestriction(String extension) throws PrivilegeException{

        if(storageUsers.ifSuperUser())
            throw new PrivilegeException("Permission denied,current user not superuser.");

        if(extension.charAt(0) != '.') {
            extension = ".".concat(extension);
        }
        extension = getFileExtension(new File(extension));
        if(!extension.equals("") && !filetypeRestrictions.contains(extension)){
            filetypeRestrictions.remove(extension);
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


    /**
     * Prints user as a string
     * @param userName Username
     * */
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


