package rogue;
/**
 * Monster class in game, inherits Avatar.
 * A Player's other attributes( MaxHealth, damage) are determined by its level, which is a positive integer.
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 */
public class Monster extends Unit{

    /**
     * Constructs a Monster of name, maximum health, damage specified.
     * @param name  name for the Monster
     * @param health  maximum health of the Monster
     * @param damage  damage of the Monster
     */
     //for backward compatibility
    public Monster(String name, int health, int damage){
        super(name, World.MONSTER_DEFAULT_X, World.MONSTER_DEFAULT_Y);
        setMaxHealth(health);
        heal();
        setDamage(damage);
    }

    public Monster(String name, int x, int y, int health, int damage){
        super(name, x, y);
        setMaxHealth(health);
        heal();
        setDamage(damage);
    }

    public String render(){
        return getName().toLowerCase().substring(0, 1);
    }

    public void reposition(){
        setX(World.MONSTER_DEFAULT_X);
        setY(World.MONSTER_DEFAULT_Y);
    }

}
