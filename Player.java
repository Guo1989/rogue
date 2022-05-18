package rogue;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/**
 * Player class in game, inherits Unit.
 * A Player's other attributes( MaxHealth, damage) are determined by its level, which is a positive integer.
 *
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 * @see Unit
 */
public class Player extends Unit{
    /**
     * constant for Player file name
     */
    public static final String PLAYER_FILE_NAME = "player.dat";
    /**
     * constant for Player level at creation
     */
    public static final int PLAYER_BASE_LEVEL = 1;
    /**
     * constant for Player health calculation
     */
    public static final int PLAYER_BASE_HEALTH = 17;
    /**
     * constant for Player health calculation
     */
    public static final int PLAYER_HEALTH_GAIN_SCALE = 3;
    /**
     * constant for Player damage calculation
     */
    public static final int PLAYER_BASE_DAMAGE = 1;
    private int level;
    //perk is persistant after level up, lost after save, home, death.
    private int perk;

    /**
     * Constructs a player of level 1 with name.
     * @param name  name for the Player
     */
    public Player(String name){
        super(name, World.PLAYER_DEFAULT_X, World.PLAYER_DEFAULT_Y);
        setLevel(PLAYER_BASE_LEVEL);
        heal();
        setPerk(0);
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
        setPerk(0);
    }
    /**
     * Sets perk to a positive int.
     * @param perk  a positive integer for perk
     */
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
    @Override
    public int getMaxHealth(){
        return PLAYER_BASE_HEALTH + PLAYER_HEALTH_GAIN_SCALE * getLevel();
    }
    /**
     * @return damage based on level.
     */
    @Override
    public int getDamage(){
        return getLevel() + perk + PLAYER_BASE_DAMAGE;
    }
    /**
     * Picks up an Item and trigger effect according to {@link Item#itemType}.
     * @param item the Item to pick up
     */
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
                //no heal with level up
                System.out.println("World complete! (You leveled up!)");
                break;
        }
    }
    /**
     * Save Player to file {@link PLAYER_FILE_NAME}.
     * @throws FileNotFoundException indicates file write failure
     */
    public void save() throws FileNotFoundException{
        PrintWriter outputStream = new PrintWriter(new FileOutputStream(PLAYER_FILE_NAME));
        outputStream.print(getName() + " " + getLevel());
        outputStream.close();
    }

    /**
     * Static method that loads Player data from file. File name is constant.
     * @return Player constructed from data in file
     * @throws FileNotFoundException indicates file read failure
     */
    public static Player load() throws FileNotFoundException{
        Scanner inputStream = new Scanner(new FileInputStream(PLAYER_FILE_NAME));
        String name = inputStream.next();
        int level = inputStream.nextInt();
        inputStream.close();
        return new Player(name, level);
    }

    /**
     * Renders Player in map.
     * @return String representing Player
     */
    @Override
    public String render(){
        return getName().toUpperCase().substring(0, 1);
    }
}
