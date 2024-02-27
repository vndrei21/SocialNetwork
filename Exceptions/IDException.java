package com.example.lab8.Exceptions;

public class IDException extends IllegalArgumentException {


    /**
     * arunca exceptii pentru id-uri
     * @param msg String
     */
    public IDException(String msg){
        super(msg);
    }

}
