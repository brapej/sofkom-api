package edu.raf.sofkom.connect;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.raf.sofkom.FileStorage;
import edu.raf.sofkom.model.User;
import edu.raf.sofkom.users.StorageUsers;


import java.io.File;
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


        Path usersMetaPath = Paths.get(path,"users_state.json");
        Path ftrMetaPath = Paths.get(path,"ftr_state.json");
        Path fileMetaPath = Paths.get(path,"filesmeta_state.json");
        StorageUsers usersT;
        HashMap<String, User> usersMapT;

        User userT;


        ObjectMapper om;

        HashSet<String> filetypeRestrictions;
        HashMap<String,HashMap<String,String>> fileMetaMap;


        if(Files.exists(usersMetaPath)){

            System.out.println(usersMetaPath.toString()+":loaded");
            om = new ObjectMapper();
            usersT = om.readValue(usersMetaPath.toFile(),StorageUsers.class);


            usersMapT = usersT.getUsers();
/*
            System.out.println(usersMapT.toString());
*/

            if(usersMapT.containsKey(userName)){

                System.out.println(userName+":matched");

                userT = usersMapT.get(userName);

                if(userT.getPassword().equals(password)){
                    System.out.println(password+":matched");

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
                    if(Files.exists(fileMetaPath)){
                       fileMetaMap  = om.readValue(ftrMetaPath.toFile(), HashMap.class);
                        storageClass.setFilesMeta(fileMetaMap);
                    }
                    storageClass.setPathToStorage(path);
                    storageClass.setPathToDownloads(Paths.get(path).toFile().getParent()+ File.separator+Paths.get(path).getFileName()+"-downloads");

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

        /*System.out.println(pathToStorage);
        System.out.println(storageUsers.toString());*/

        if(!Files.exists(Paths.get(pathToStorage))){
           System.exit(0);
        }

        fileStorage.getStorageUsers().setCurrentUser(null);

        ObjectMapper om = new ObjectMapper();
        Path usersMeta = Paths.get(pathToStorage,"users_state.json");
        Path ftrMeta = Paths.get(pathToStorage,"ftr_state.json");
        Path filesMeta = Paths.get(pathToStorage,"filesmeta_state.json");





        if(Files.exists(usersMeta))
            Files.delete(usersMeta);
        if(Files.exists(ftrMeta))
            Files.delete(ftrMeta);
        if(Files.exists(filesMeta))
            Files.delete(filesMeta);

        om.writeValue(usersMeta.toFile(),storageUsers);
        System.out.println(usersMeta.toString()+": Saved.");
        om.writeValue(ftrMeta.toFile(),fileStorage.getFiletypeRestrictions());
        System.out.println(ftrMeta.toString()+": Saved.");
        om.writeValue(filesMeta.toFile(),fileStorage.getFilesMeta());
        System.out.println(filesMeta.toString()+": Saved.");

         System.exit(0);
    }
}
