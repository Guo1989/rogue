package rogue;

public abstract class Entity{

    private int x;
    private int y;


    public Entity(int x, int y){
        this.x = x;
        this.y = y;
    }



    abstract public String render();

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y = y;
    }


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
