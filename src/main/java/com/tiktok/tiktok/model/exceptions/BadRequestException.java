package com.tiktok.tiktok.model.exceptions;

public class BadRequestException extends RuntimeException{
    private BadRequestException(String msg){
        super(msg);
    }
}
