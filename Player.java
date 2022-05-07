package rogue;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/**
 * Player class in game, inherits Avatar.
 * A Player's other attributes( MaxHealth, damage) are determined by its level, which is a positive integer.
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 */
public class Player extends Unit{
    public static final String PLAYER_FILE_NAME = "player.dat";
    private static final int PLAYER_BASE_LEVEL = 1;
    private int level;
    private int perk;

    /**
     * Constructs a player of level 1 with name.
     * @param name  name for the Player
     */
    public Player(String name){
        super(name, World.PLAYER_DEFAULT_X, World.PLAYER_DEFAULT_Y);
        setLevel(PLAYER_BASE_LEVEL);
        heal();
        perk = 0;
    }
    /**
     * Constructs a player of certain level with name.
     * Used for loading saved player.
     * @param name  name for the Player
     * @param level level for the Player
     */
    public Player(String name, int level){
        super(name, World.PLAYER_DEFAULT_X, World.PLAYER_DEFAULT_Y);
        setLevel(level);
        heal();
        perk = 0;
    }

    public void setPerk(int perk){
        this.perk = perk;
    }
    /**
     * @return the integer of level
     */
    public int getLevel(){
        return level;
    }
    /**
     * Sets level to a positive int.
     * @param level  a positive integer for level
     */
    public void setLevel(int level){
        this.level = level;
    }
    /**
     * @return maximum Health based on level
     */
    public int getMaxHealth(){
        return 17 + 3 * getLevel();
    }
    /**
     * @return damage based on level.
     */
    public int getDamage(){
        return getLevel() + perk + 1;
    }

    public void pickUp(Item item){
        if(item == null){
            return;
        }
        switch(item.itemType){
            case HEALING:
                heal();
                System.out.println("Healed!");
                break;
            case DAMAGE:
                perk++;
                System.out.println("Attack up!");
                break;
            case WARP_STONE:
                level++;
                System.out.println("World complete! (You leveled up!)");
                break;
        }


    }

    public void save(){
        try{
            PrintWriter outputStream = new PrintWriter(new FileOutputStream(PLAYER_FILE_NAME));
            outputStream.print(getName() + " " + getLevel());
            outputStream.close();
            System.out.println("Player data saved.\n");
        }
        catch(FileNotFoundException e){
            System.err.println("Save err.");
        }

    }

    public String render(){
        return getName().toUpperCase().substring(0, 1);
    }

    public static Player load(){
        try {
            Scanner inputStream = new Scanner(new FileInputStream(PLAYER_FILE_NAME));
            String name = inputStream.next();
            int level = inputStream.nextInt();
            System.out.println("Player data loaded.\n");
            inputStream.close();
            return new Player(name, level);
        }
        catch (FileNotFoundException e) {
            System.err.println("No player data found.\n");
            return null;
        }
        catch(Exception e){
            System.err.println("Load err.");
            return null;
        }
    }

    public void reposition(){
        setX(World.PLAYER_DEFAULT_X);
        setY(World.PLAYER_DEFAULT_Y);
    }

}
