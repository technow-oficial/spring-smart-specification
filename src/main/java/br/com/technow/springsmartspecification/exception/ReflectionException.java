package br.com.technow.springsmartspecification.exception;

public class ReflectionException extends RuntimeException {

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }

}
