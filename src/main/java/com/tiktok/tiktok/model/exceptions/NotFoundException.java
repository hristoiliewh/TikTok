package com.tiktok.tiktok.model.exceptions;

public class NotFoundException extends RuntimeException{
    private NotFoundException(String msg){
        super(msg);
    }
}
