package edu.raf.sofkom.connect;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.raf.sofkom.FileStorage;
import edu.raf.sofkom.model.User;
import edu.raf.sofkom.users.StorageUsers;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;


public class ConnectionUtils {

    public static  boolean connect(String path, String userName, String password, FileStorage storageClass) throws IOException {

        if(!Files.exists(Paths.get(path))){
            return false;
        }

        HashSet<String> filetypeRestrictions;

        Path usersMetaPath = Paths.get(path,"users_state.json");
        Path ftrMetaPath = Paths.get(path,"ftr_state.json");
        StorageUsers usersT;
        HashMap<String, User> usersMapT;
        User userT;


        ObjectMapper om;

  /*      System.out.println(usersMetaPath.toString()+":pre");
        System.out.println(userName+":pre");
        System.out.println(password+":pre");*/
        if(Files.exists(usersMetaPath)){

            System.out.println(usersMetaPath.toString()+":suc");
            om = new ObjectMapper();
            usersT = om.readValue(usersMetaPath.toFile(),StorageUsers.class);
            usersMapT = usersT.getUsers();
/*
            System.out.println(usersMapT.toString());
*/

            if(usersMapT.containsKey(userName)){
/*
                System.out.println(userName+":suc");
*/
                userT = usersMapT.get(userName);

                if(userT.getPassword().equals(password)){
                    System.out.println(password+":suc");

                    storageClass.getStorageUsers().setSuperUser(usersT.getSuperUser());
/*
                    System.out.println(usersT.getSuperUser().toString());
*/
                    storageClass.getStorageUsers().setCurrentUser(userT);
                    storageClass.getStorageUsers().setUsers(usersMapT);


                    if(Files.exists(ftrMetaPath)){
                        filetypeRestrictions = om.readValue(ftrMetaPath.toFile(), HashSet.class);
                        storageClass.setFiletypeRestrictions(filetypeRestrictions);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public static  void disconnect(FileStorage fileStorage) throws IOException {



        String pathToStorage;
        StorageUsers storageUsers;

        pathToStorage = fileStorage.getPathToStorage();
        storageUsers = fileStorage.getStorageUsers();

        System.out.println(pathToStorage);
        System.out.println(storageUsers.toString());

        if(!Files.exists(Paths.get(pathToStorage))){
           System.exit(0);
        }

        fileStorage.getStorageUsers().setCurrentUser(null);

        ObjectMapper om = new ObjectMapper();
        Path usersMeta = Paths.get(pathToStorage,"users_state.json");
        Path ftrMeta = Paths.get(pathToStorage,"ftr_state.json");

        System.out.println(usersMeta.toString());
        System.out.println(ftrMeta.toString());


        if(Files.exists(usersMeta))
            Files.delete(usersMeta);
        if(Files.exists(ftrMeta))
            Files.delete(usersMeta);

        om.writeValue(usersMeta.toFile(),storageUsers);
        om.writeValue(ftrMeta.toFile(),fileStorage.getFiletypeRestrictions());


         System.exit(0);
    }
}
