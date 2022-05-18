package rogue;
/**
 * Base class for Player and Monster, encapsulating the basic things like name, health and damage...
 * Contains the straight forward getters and setters.
 * Also implements the logic of battle between two Units.
 * Unit is an abstract class.
 *
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 * @see Player
 * @see Monster
 * @see Entity
 */
public abstract class Unit extends Entity{
    private String name;
    private int currentHealth;
    private int maxHealth;
    private int damage;
    /**
     * Constructs a Unit with name and coordinates.
     * @param name  name for the Unit
     * @param x     x coordinate, must be integer
     * @param y     y coordinate, must be integer
     */
    public Unit(String name, int x, int y){
        super(x, y);
        this.name = name;
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

    /**
     * Attacks another Unit instance.
     * Prints out a line to stdout show the detail.
     * Health of Units after attack can go below 0.
     * @param another   another Unit instance to attack
     */
    private void attack(Unit another){
        int anotherHealth = another.getCurrentHealth();
        int damageIncured = this.getDamage();
        //anotherHealth = damageIncured > anotherHealth ? 0 : anotherHealth - damageIncured;
        //health actually goes below 0
        anotherHealth = anotherHealth - damageIncured;
        another.setCurrentHealth(anotherHealth);
        System.out.printf("%s attacks %s for %d damage.\n", getName(), another.getName(), getDamage());//use getDamage() instead of damageIncured.
    }

    /**
	 * Logics for battle loop, player and monster take turns to attack.
     * Technically, it's always player to battle monsters.
     * @param another   another Unit instance to battle
	 * @see Unit#attack(Unit)
	 */
	public void battle(Unit another){
		System.out.printf("%s encountered a %s!\n\n", getName(), another.getName());
		while(true){
			System.out.println(getName() + " " + getCurrentHealth() + "/" + getMaxHealth()
			+ " | " + another.getName() + " " + another.getCurrentHealth() + "/" + another.getMaxHealth());

			attack(another);
			if(another.getCurrentHealth() <= 0){
				System.out.printf("%s wins!\n", getName());
				return;
			}
			another.attack(this);
			if(getCurrentHealth() <= 0){
				System.out.printf("%s wins!\n", another.getName());
				return;
			}
            System.out.println();
		}
	}
    /** todo internall err exception
     * Moves Unit according to key stroke represented by a String.
     * @param key   the String of key stroke
     *
     */
    public void move(String key){
        switch(key){
            case GameEngine.COMMAND_MAP_UP:
                setY(getY() - 1);
                break;
            case GameEngine.COMMAND_MAP_DOWN:
                setY(getY() + 1);
                break;
            case GameEngine.COMMAND_MAP_LEFT:
                setX(getX() - 1);
                break;
            case GameEngine.COMMAND_MAP_RIGHT:
                setX(getX() + 1);
                break;
            default:
        }
    }

    /**
     * Method used to restore a Unit to it's full health.
     * Facilitates the function of Item "HEALING", and World reset.
     * Also used in constructors of Player and Monster.
     * @see Player#pickUp(Item)
     * @see World#register(Entity)
     * @see Player#Player(String)
     * @see Player#Player(String, int)
     */
    public void heal(){
        setCurrentHealth(getMaxHealth());
    }

    /**
     * Repositions Unit to coordinate (x,y).
     * Simply calls setX() and setY(), but is more specifically used for initializing, resetting the World.
     * @param x x coordinate, must be integer
     * @param y y coordinate, must be integer
     */
    public void reposition(int x, int y){
        setX(x);
        setY(y);
    }
}
