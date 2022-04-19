package rogue;
/**
 * Monster class in game, inherits Avatar.
 * A Player's other attributes( MaxHealth, damage) are determined by its level, which is a positive integer.
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 */
public class Monster extends Avatar{

    /**
     * Constructs a Monster of name, maximum health, damage specified.
     * @param name  name for the Monster
     * @param health  maximum health of the Monster
     * @param damage  damage of the Monster
     */
    public Monster(String name, int health, int damage){
        setName(name);
        setMaxHealth(health);
        setCurrentHealth(getMaxHealth());
        setDamage(damage);
    }

}
