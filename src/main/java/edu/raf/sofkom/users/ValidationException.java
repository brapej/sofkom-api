package edu.raf.sofkom.users;

import lombok.Data;

@Data
public class ValidationException extends Exception{
    String code;
    public ValidationException(String code){
        super(code);
        this.code=code;
    }

}
