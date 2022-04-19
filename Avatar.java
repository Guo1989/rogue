package rogue;
/**
 * Base class for Player and Monster, encapsulating the basic things like name, health...
 * Contains the straight forward getters and setters.
 * Also implements the logic of attack between two avatars.
 * Avatar has no constructor, will not be instaniated.
 *
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 * @see rogue.Player
 * @see rogue.Monster
 */
public class Avatar{
    private String name;
    private int currentHealth;
    private int maxHealth;
    private int damage;

    /**
     * Attacks another avatar instance.
     * Prints out a line to stdout show the detail.
     * Health of Avatars after attack can go below 0.
     * @param another   another Avatar instance
     */
    public void attack(Avatar another){
        int anotherHealth = another.getCurrentHealth();
        int damageIncured = this.getDamage();
        //anotherHealth = damageIncured > anotherHealth ? 0 : anotherHealth - damageIncured;
        //health actually goes below 0
        anotherHealth = anotherHealth - damageIncured;
        another.setCurrentHealth(anotherHealth);
        System.out.printf("%s attacks %s for %d damage.\n", getName(), another.getName(), getDamage());//use getDamage() instead of damageIncured.
    }
    /**
     * @return the name string
     */
    public String getName(){
        return name;
    }
    /**
     * Sets name.
     * @param name  the name string
     */
    public void setName(String name){
        this.name = name;
    }
    /**
     * Gets currentHealth.
     * @return int for current health
     */
    public int getCurrentHealth(){
        return currentHealth;
    }
    /**
     * Sets currentHealth. Value capped by MaxHealth.
     * @param currentHealth current health, value larger than maximum health will be handled
     */
    public void setCurrentHealth(int currentHealth){
        this.currentHealth = currentHealth > getMaxHealth() ? getMaxHealth() : currentHealth;
    }
    /**
     * Sets maxHealth.
     * @param maxHealth maximum Health
     */
    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }
    /**
     * @return maximum Health
     */
    public int getMaxHealth(){
        return maxHealth;
    }
    /**
     * Sets damage.
     * @param damage int for damage value
     */
    public void setDamage(int damage){
        this.damage = damage;
    }
    /**
     * @return damage.
     */
    public int getDamage(){
        return damage;
    }


}
