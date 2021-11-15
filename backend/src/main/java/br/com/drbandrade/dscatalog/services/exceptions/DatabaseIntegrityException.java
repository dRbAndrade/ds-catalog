package br.com.drbandrade.dscatalog.services.exceptions;

public class DatabaseIntegrityException extends RuntimeException{

    public DatabaseIntegrityException(String message) {
        super(message);
    }
}
