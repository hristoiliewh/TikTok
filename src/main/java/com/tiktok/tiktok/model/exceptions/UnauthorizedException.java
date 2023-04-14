package com.tiktok.tiktok.model.exceptions;

public class UnauthorizedException extends RuntimeException{
    private UnauthorizedException(String msg){
        super(msg);
    }
}
