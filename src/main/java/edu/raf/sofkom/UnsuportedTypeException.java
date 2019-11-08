package edu.raf.sofkom;

public class UnsuportedTypeException extends Exception {
    String error;
    public UnsuportedTypeException(String errorMsg){
        super(errorMsg);
        error = errorMsg;
    }
}
