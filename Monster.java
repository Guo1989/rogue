package rogue;
/**
 * Monster class in game, inherits Unit.
 *
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 * @see Unit
 */
public class Monster extends Unit{
    /**
     * The inclusive range within which Monster can track Player.
     * Works both vertically and horizontally.
     */
    public static final int TRACK_RANGE = 2;

    /**
     * Constructs a Monster of name, maximum health, damage specified.
     * Used in command "monster" to generate monster manually.
     * @param name      name for the Monster
     * @param health    maximum health of the Monster
     * @param damage    damage of the Monster
     */
    public Monster(String name, int health, int damage){
        super(name, World.MONSTER_DEFAULT_X, World.MONSTER_DEFAULT_Y);
        setMaxHealth(health);
        heal();
        setDamage(damage);
    }
    /**
     * Constructs a Monster of name, coordinates, maximum health, damage specified in .dat file.
     * @param name      name for the Monster
     * @param x         x coordinate, must be integer
     * @param y         y coordinate, must be integer
     * @param health    maximum health of the Monster
     * @param damage    damage of the Monster
     */
    public Monster(String name, int x, int y, int health, int damage){
        super(name, x, y);
        setMaxHealth(health);
        heal();
        setDamage(damage);
    }
    /**
     * Renders Monster in map.
     * @return String representing Monster
     */
    @Override
    public String render(){
        return getName().toLowerCase().substring(0, 1);
    }

}
