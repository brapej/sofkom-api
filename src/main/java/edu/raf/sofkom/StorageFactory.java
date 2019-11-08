package edu.raf.sofkom;

import java.lang.reflect.InvocationTargetException;

public class StorageFactory {
    public static FileStorage getStorage(String a) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (FileStorage) Class.forName(a).getDeclaredConstructor().newInstance();
    }
}
