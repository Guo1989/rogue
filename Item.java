package rogue;

public class Item extends Entity{
    public enum Type{HEALING, DAMAGE, WARP_STONE};
    public Type itemType;

    public Item(int x, int y, Type itemType){
        super(x, y);
        this.itemType = itemType;
    }

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
