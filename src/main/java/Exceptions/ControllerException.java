package Exceptions;

public class ControllerException extends RuntimeException {
    public ControllerException(String s){
        super(s);
    }
}