package rogue;
/**
 * Item that Player can pick up.
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 * @see Entity
 */
public class Item extends Entity{
    /**
     * Defines types of Item
     */
    public enum Type{
        /**
         * heals the Player
         */
        HEALING,
        /**
         * increases Player damage
         */
        DAMAGE,
        /**
         * acquire to level up and pass
         */
        WARP_STONE};
    /**
     * type field for Item
     */
    public Type itemType;
    /**
     * Constructs Item with coordinates and enum Type value
     * @param x         the x coordinate
     * @param y         the y coordinate
     * @param itemType  enum Type value for Item
     */
    public Item(int x, int y, Type itemType){
        super(x, y);
        this.itemType = itemType;
    }
    /**
     * Constructs Item with coordinates and Type symbol
     * @param x                 the x coordinate
     * @param y                 the y coordinate
     * @param itemTypeString    String symbol indicating enum Type
     */
    public Item(int x, int y, String itemTypeString){
        super(x, y);
        switch(itemTypeString){
            case "+":
                itemType = Type.HEALING;
                break;
            case "^":
                itemType = Type.DAMAGE;
                break;
            case "@":
                itemType = Type.WARP_STONE;
                break;
            default:
                itemType = null;
        }
    }
    /**
     * Renders Item in map.
     * @return String representing Item
     */
    @Override
    public String render(){
        switch(itemType){
            case HEALING:
                return "+";
            case DAMAGE:
                return "^";
            case WARP_STONE:
                return "@";
        }
        return "";
    }

}
