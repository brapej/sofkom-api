package edu.raf.sofkom.privileges;

import lombok.Data;

@Data
public class PrivilegeException extends Exception {
    String error;
    public PrivilegeException(String errorMsg){
        super(errorMsg);
        error = errorMsg;
    }

}
