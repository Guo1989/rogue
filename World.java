package rogue;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
/**
 * The world of a level, reponsible for rendering map, moving Units on map and triggering encounters.
 * The overall class for each world. Holds map data and entities of the world.
 * It would make sense for this class to manages interactions between entities
 * (such as triggering battles, or detecting the pickup of items by the player).
 * Currently using a LinkedList to manage all Entitys in World.
 * Manages the overall rendering of the world, perhaps delegating to some rendering to other classes.
 * Note when Unit goes up, its y coordinate decreases 1. So uppermost y is min y.
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 *
 */
public class World{
    /**
     * The default width of world map.
     */
    public static final int WORLD_DEFAULT_WIDTH = 6;
    /**
     * The default height of world map.
     */
    public static final int WORLD_DEFAULT_HEIGHT = 4;
    /**
     * Minimum of x coordinate.
     */
    public static final int WORLD_MIN_X = 0;
    /**
     * Minimum of y coordinate.
     */
    public static final int WORLD_MIN_Y = 0;
    /**
     * Default x coordinate for Player.
     */
    public static final int PLAYER_DEFAULT_X = 1;
    /**
     * Default y coordinate for Player.
     */
    public static final int PLAYER_DEFAULT_Y = 1;
    /**
     * Default x coordinate for Player.
     */
    public static final int MONSTER_DEFAULT_X = 4;
    /**
     * Default y coordinate for Monster.
     */
    public static final int MONSTER_DEFAULT_Y = 2;

    //player monster ... item ...
    private LinkedList<Entity> entityList;
    private int width;
    private int height;
    private String[] map;
    //only temporarily store x, y, foring decouping world creation and player creation/loading,
    //since we get player coordinates and rest specs from separate sources.
    private Integer playerX;
    private Integer playerY;
    private boolean completed;

    /**
     * Constructs default world.
     */
    public World(){
        width = WORLD_DEFAULT_WIDTH;
        height = WORLD_DEFAULT_HEIGHT;
        map = new String[WORLD_DEFAULT_HEIGHT];
        entityList = new LinkedList<Entity>();
        for(int l = 0; l < WORLD_DEFAULT_HEIGHT; l++){
            map[l] = ".".repeat(WORLD_DEFAULT_WIDTH);
        }
    }

    /**
     * Constructs world from .dat file.
     * Assuming Monsters come before Items in .dat file
     * Not Assuming Player data comes before other Entitys (decoupled
     *
     * @param inputStream Scanner to read in World layout
     * @throws IllegalArgumentException indicates .dat file incompatibility
     */
    public World(Scanner inputStream) throws IOException{
        width = inputStream.nextInt();
        height = inputStream.nextInt();
        inputStream.nextLine();

        map = new String[height];
        entityList = new LinkedList<Entity>();
        String line;
        //get map rows
        for(int l = 0; l < height; l++){
            try{
                map[l] = inputStream.nextLine();
            }
            catch(NoSuchElementException e){
                throw new IOException("Inconsistent map data height.");
            }
            if (map[l].length() != width){
                throw new IOException("Inconsistent map data width.");
            }
        }
        //get Entitys
        while(inputStream.hasNextLine()){
            String[] entityArgs;
            //https://stackoverflow.com/questions/4964484/why-does-split-on-an-empty-string-return-a-non-empty-array
            String entityData = inputStream.nextLine();
            entityArgs = entityData.split(" ");
            try{
                int x = Integer.parseInt(entityArgs[1]);
                int y = Integer.parseInt(entityArgs[2]);
                switch(entityArgs[0]){
                    case "player":
                        //reserve coordinates for register Player
                        playerX = x;
                        playerY = y;
                        if(y > height - 1 || y < WORLD_MIN_Y || x > width - 1 || x < WORLD_MIN_X){
                            throw new IOException("Player out of map");
                        }
                        break;
                    case "monster":
                        String name = entityArgs[3];
                        int health = Integer.parseInt(entityArgs[4]);
                        int damage = Integer.parseInt(entityArgs[5]);
                        entityList.add(new Monster(name, x, y, health, damage));
                        if(y > height - 1 || y < WORLD_MIN_Y || x > width - 1 || x < WORLD_MIN_X
                            || health < 1 || damage < 1){
                            throw new IOException("Wrong Monster config");
                        }
                        break;
                    case "item":
                        String symbol = entityArgs[3];
                        entityList.add(new Item(x, y, symbol));
                        if(y > height - 1 || y < WORLD_MIN_Y || x > width - 1 || x < WORLD_MIN_X
                            || !symbol.equals("^") && !symbol.equals("+") && !symbol.equals("@")){
                            throw new IOException("Wrong Item config");
                        }
                        break;
                    default:
                        throw new IOException("entityArg[0] not recognized: " + entityArgs[0]);
                }
            }
            catch(NumberFormatException e){
                inputStream.close();
                throw new IOException("Entity not recognized: " + entityData);
            }
        }
        inputStream.close();
    }

    /**
     * A static method to return a Scanner, facilitating World generation.
     * @param file .dat file's name in String(without .dat suffix)
     * @return Scanner to read .dat file
     * @throws GameLevelNotFoundException cannot read .dat file
     */
    public static Scanner load(String file) throws GameLevelNotFoundException{
        try{
            Scanner inputStream;
            inputStream = new Scanner(new FileInputStream(file+".dat"));
            return inputStream;
        }
        catch(FileNotFoundException e){
            throw new GameLevelNotFoundException("Map not found: " + file);
        }

    }

    /**
     * Registers Entitys to World.
     * Heals Player, places Player.
     * Heals Monster; leaves Monster coordinates be since it cannot tell if it should reposition Monster.
     * Adds Player, Monster..., Item... in a LinkedList with ordering.
     * @param entity    Entity to register
     */
    public void register(Entity entity){
        if(entity == null){
            return;
        }
        if(entity.getClass() == Player.class){
            Player player = (Player) entity;
            player.heal();
            if(playerX != null && playerY != null){
                player.reposition(playerX, playerY);
            }
            else{
                player.reposition(World.PLAYER_DEFAULT_X, World.PLAYER_DEFAULT_Y);
            }
            entityList.addFirst(player);
        }
        else if(entity.getClass() == Monster.class){
            Monster monster = (Monster) entity;
            monster.heal();
            entityList.add(1, monster);
        }
        else if(entity.getClass() == Item.class){
            Item item = (Item) entity;
            entityList.add(entity);
            if(item.itemType == Item.Type.WARP_STONE){
                completed = false;
            }
        }
    }

    /**
     * Visually renders the game world map.
     * Renders Entity loaded first, covering the rest in same cell.
     */
    public void render(){
        StringBuilder[] entityMap = new StringBuilder[height];
        for(int y = 0; y < height; y++){
            entityMap[y] = new StringBuilder(map[y]);
        }
        for(Entity e: entityList){
            //render entity added first, also only in traversable cells.
            //so render if it's a '.' cell.
            if(entityMap[e.getY()].charAt(e.getX()) == '.'){
                entityMap[e.getY()].setCharAt(e.getX(), e.render().charAt(0));
            }

        }
        for(int y = 0; y < height; y++){
            System.out.println(entityMap[y]);
        }
        System.out.println();
    }
    /**
     * Possibly moves Monsters towards Player, iterating over entityList.
     * Doesn't consider edge of map since Monster moves towards Player, not out of map.
     */
    public void moveMonsters(){
        Iterator<Entity> iterator = entityList.iterator();
        Entity player = iterator.next();//first is player

        while( iterator.hasNext()){
            Entity monster = iterator.next();
            if(monster.getClass() != Monster.class){
                return;
            }
            int mX = monster.getX();
            int mY = monster.getY();
            int pX = player.getX();
            int pY = player.getY();
            if ((mX - pX > Monster.TRACK_RANGE) || (mX - pX < -Monster.TRACK_RANGE) ){
                continue;
            }
            if ((mY - pY > Monster.TRACK_RANGE) || (mY - pY < -Monster.TRACK_RANGE) ){
                continue;
            }
            int mXNext = mX;
            int mYNext = mY;

            //note when mX == pX or mY == pY
            //can use ternary operator.
            //since later we require mX != pX, mY != pY
            mXNext = mX <= pX ? mX + 1 : mX - 1;
            mYNext = mY <= pY ? mY + 1 : mY - 1;

            if(traversable(mXNext, mY) && mX != pX){
                monster.setX(mXNext);
            }
            else if(traversable(mX, mYNext) && mY != pY){
                monster.setY(mYNext);
            }
        }
    }

    /**
     * A wrapper to call player's move, taking account of the size of map.
     * @param command   A String as command to move, can be "w" "a" "s" "d"
     */
    public void movePlayer(String command){
        Player player = (Player) entityList.getFirst();
        move(player, command);
    }

    /**
     * Resolves all encounters between Player and Item, Monster.
     */
    public void scanEncounter(){
        Iterator<Entity> iterator = entityList.iterator();
        Player player = (Player) iterator.next();

        while( iterator.hasNext()) {
            Entity e = iterator.next();
            if(player.encounter(e) && e.getClass() == Monster.class){
                player.battle( (Monster) e);
                if(player.getCurrentHealth() <= 0){
                    completed = true;
                    player.setPerk(0);
                    return;
                }
                //defeated a Monster
                else{
                    iterator.remove();
                    //Compatibility
                    //shoud be completed with no Entity other than Player
                    //for default World
                    //todo have 2 empty lines
                    if(entityList.size() == 1){
                        completed = true;
                    }
                    else{
                        System.out.println();
                    }
                }
            }
            else if(player.encounter(e) && e.getClass() == Item.class){
                Item item = (Item) e;
                player.pickUp( (Item) e);
                if(item.itemType == Item.Type.WARP_STONE){
                    completed = true;
                }
                iterator.remove();
            }
        }
    }
    /**
     * Tells if round/level is finished: Player dead or got @.
     * @return boolean indicating wether this round/level is finished
     */
    public boolean isCompleted(){
        return completed;
    }

    private void move(Entity e, String command){
        switch(command){
            case GameEngine.COMMAND_MAP_UP:
                if(e.getY() == WORLD_MIN_Y){
                    return;
                }
                if( traversable(e.getX(), e.getY() - 1) ){
                    e.setY(e.getY() - 1);
                }
                break;
            case GameEngine.COMMAND_MAP_DOWN:
                if(e.getY() == height - 1){
                    return;
                }
                if( traversable(e.getX(), e.getY() + 1) ){
                    e.setY(e.getY() + 1);
                }
                break;
            case GameEngine.COMMAND_MAP_LEFT:
                if(e.getX() == WORLD_MIN_X){
                    return;
                }
                if( traversable(e.getX() - 1, e.getY()) ){
                    e.setX(e.getX() - 1);
                }
                break;
            case GameEngine.COMMAND_MAP_RIGHT:
                if(e.getX() == width - 1){
                    return;
                }
                if( traversable(e.getX() + 1, e.getY()) ){
                    e.setX(e.getX() + 1);
                }
                break;
            default:
                ;
        }
    }
    private boolean traversable(int x, int y){
        if(x < WORLD_MIN_X || x > width - 1 || y < WORLD_MIN_Y || y > height - 1){
            return false;
        }
        if(map[y].charAt(x) == '.'){
            return true;
        }
        return false;
    }
}
