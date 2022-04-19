package rogue;
/**
 * Player class in game, inherits Avatar.
 * A Player's other attributes( MaxHealth, damage) are determined by its level, which is a positive integer.
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 */
public class Player extends Avatar{
    private static final int PLAYER_BASE_LEVEL = 1;
    private int level;

    /**
     * Constructs a player of level 1 with name.
     * @param name  name for the Player
     */
    public Player(String name){
        setName(name);
        setLevel(PLAYER_BASE_LEVEL);
        setCurrentHealth(getMaxHealth());
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
        return getLevel() + 1;
    }

}
