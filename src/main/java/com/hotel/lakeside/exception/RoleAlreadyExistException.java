package com.hotel.lakeside.exception;

public class RoleAlreadyExistException extends RuntimeException {
    public RoleAlreadyExistException(String message){
        super(message);
    }
}
