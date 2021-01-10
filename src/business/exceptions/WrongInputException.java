package business.exceptions;

public class WrongInputException extends Exception {
    
    public WrongInputException(String msg){
        msgPersonalized(msg);
    }

    public void msgPersonalized(String msg){
        System.out.println(msg);
    }
}
