package com.hotel.lakeside.exception;

public class InvalidBookingRequestException extends RuntimeException {
    public InvalidBookingRequestException(String message){
        super(message);
    }

}
