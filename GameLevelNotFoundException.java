package rogue;
/**
 * Exception class for reading game level files
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 */
public class GameLevelNotFoundException extends Exception{
    /**
     * Default constructor with default message
     */
    public GameLevelNotFoundException(){
        super("Game Level Not Found");
    }
    /**
     * Constructor with message
     * @param message to provide
     */
    public GameLevelNotFoundException(String message){
        super("Game Level Not Found:" + message);
    }
}
