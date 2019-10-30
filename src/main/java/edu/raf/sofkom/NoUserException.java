package edu.raf.sofkom;

public class NoUserException extends Exception{
    String error;
    public NoUserException(String errorMsg){
        super(errorMsg);
    }

}
