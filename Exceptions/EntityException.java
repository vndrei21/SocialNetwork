package com.example.lab8.Exceptions;

/**
 * Exceptie pentru entitati
 */
public class EntityException extends IllegalArgumentException {

    /**
     * arunca exceptie pentru entitati
     * @param msg string mesaj
     */
    public EntityException(String msg)
    {
        super(msg);
    }
}
