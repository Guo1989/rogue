package rogue;
/**
 * Base class for Player and Monster, encapsulating the basic things like name, health...
 * Contains the straight forward getters and setters.
 * Also implements the logic of attack between two Units.
 * Unit has no constructor, will not be instaniated.
 *
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 * @see rogue.Player
 * @see rogue.Monster
 */
public abstract class Unit extends Entity{
    private String name;
    private int currentHealth;
    private int maxHealth;
    private int damage;

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
     * Attacks another Unit instance.
     * Prints out a line to stdout show the detail.
     * Health of Units after attack can go below 0.
     * @param another   another Unit instance
     */
    public void attack(Unit another){
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
	 * @see Unit#attack()
	 */
	public void battle(Unit another){
		System.out.printf("%s encountered a %s!\n\n", getName(), another.getName());
		while(true){
			System.out.println(getName() + " " + getCurrentHealth() + "/" + getMaxHealth()
			+ " | " + another.getName() + " " + another.getCurrentHealth() + "/" + another.getMaxHealth());
			attack(another);
			if(another.getCurrentHealth() <= 0){
				System.out.printf("%s wins!\n", getName());
                System.out.println();
				return;
			}
			another.attack(this);
			if(getCurrentHealth() <= 0){
				System.out.printf("%s wins!\n", another.getName());
                //todo: not very modularized
                //System.out.println();
				return;
			}
            System.out.println();
		}
	}

    public void move(String key) throws Exception{
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
                // todo: exception
                throw new Exception("");
        }
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

    public void heal(){
        setCurrentHealth(getMaxHealth());
    }


}
