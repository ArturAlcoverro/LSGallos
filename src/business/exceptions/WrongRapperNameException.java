package business.exceptions;

public class WrongRapperNameException extends Exception{

    public WrongRapperNameException(String msg){
        msgPersonalized(msg);
    }

    public void msgPersonalized(String msg){
        System.out.println(msg);
    }
}
