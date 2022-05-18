package rogue;
/**
 * Abstract class for things on map.
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 */
public abstract class Entity{
    private int x;
    private int y;

    /**
     * Constructs Entity with coordinates
     * @param x x coordinate, must be integer
     * @param y y coordinate, must be integer
     */
    public Entity(int x, int y){
        this.x = x;
        this.y = y;
    }
    /**
     * Renders Entity on map
     * @return the String to render on map
     */
    abstract public String render();
    /**
     * @return x coordinate
     */
    public int getX(){
        return x;
    }
    /**
     * Disregards traversability
     * @param x x coordinate to set
     */
    public void setX(int x){
        this.x = x;
    }
    /**
     * @return y coordinate
     */
    public int getY(){
        return y;
    }
    /**
     * Disregards traversability
     * @param y y coordinate to set
     */
    public void setY(int y){
        this.y = y;
    }
    /**
     * tests if two Entitys encounter
     * @param otherEntity the other Entity to test with
     * @return boolean for whether this Entity and {@code otherEntity} encounter
     * @see Unit#battle(Unit)
     * @see Player#pickUp(Item)
     */
    public boolean encounter(Entity otherEntity){
        if(otherEntity == null){
            return false;
        }
        if(x != otherEntity.getX()){
            return false;
        }
        if(y != otherEntity.getY()){
            return false;
        }
        return true;
    }

}
