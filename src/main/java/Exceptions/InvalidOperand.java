package Exceptions;


public class InvalidOperand extends RuntimeException{
    public InvalidOperand(String s){
        super(s);
    }
}
