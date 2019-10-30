package edu.raf.sofkom;

public class PrivilegeException extends Exception {
    String error;
    public PrivilegeException(String errorMsg){
        super(errorMsg);
    }

}
