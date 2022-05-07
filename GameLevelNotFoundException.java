package rogue;

public class GameLevelNotFoundException extends Exception{
    public GameLevelNotFoundException(){
        super("Game Level Not Found");
    }

    public GameLevelNotFoundException(String message){
        super("Game Level Not Found:" + message);
    }
}
