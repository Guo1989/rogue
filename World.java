package rogue;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Iterator;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
/**
 * The world of game, reponsible for rendering map, moving Avatars on map and triggering encounter.

 * The overall class for each world. Holds map data and entities of the world.
 * It would make sense for this class to manages interactions between entities
 * (such as triggering battles, or detecting the pickup of items by the player).
 * Manages the overall rendering of the world, perhaps delegating to some rendering to other classes.
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 *
 */
 //todo how to give ref of player in GameEngine to World?
public class World{
    /**
     *The width of world map.
     */
    public static final int WORLD_DEFAULT_WIDTH = 6;
    /**
     *The height of world map.
     */
    public static final int WORLD_DEFAULT_HEIGHT = 4;

    public static final int WORLD_MIN_X = 0;
    public static final int WORLD_MIN_Y = 0;

    public static final int PLAYER_DEFAULT_X = 1;
    public static final int PLAYER_DEFAULT_Y = 1;
    public static final int MONSTER_DEFAULT_Y = 2;
    public static final int MONSTER_DEFAULT_X = 4;
    //player monster ... item ...
    private LinkedList<Entity> entityList;
    private int width;
    private int height;
    private String[] map;
    //only temporarily store x, y, foring decouping world creation and player creation.
    private Integer playerX;
    private Integer playerY;
    private boolean completed;

    public World(){
        width = WORLD_DEFAULT_WIDTH;
        height = WORLD_DEFAULT_HEIGHT;
        map = new String[WORLD_DEFAULT_HEIGHT];
        entityList = new LinkedList<Entity>();
        for(int l = 0; l < WORLD_DEFAULT_HEIGHT; l++){
            //possibly throws NoSuchElementException
            map[l] = ".".repeat(WORLD_DEFAULT_WIDTH);
        }
    }

    public World(Scanner inputStream) throws NoSuchElementException, ArrayIndexOutOfBoundsException, IllegalArgumentException, NullPointerException{
        width = inputStream.nextInt();
        height = inputStream.nextInt();
        inputStream.nextLine();

        map = new String[height];
        entityList = new LinkedList<Entity>();
        String line;
        //get map Strings
        for(int l = 0; l < height; l++){
            //possibly throws NoSuchElementException
            map[l] = inputStream.nextLine();
        }

        while(inputStream.hasNextLine()){
            String[] entityArgs;
            entityArgs = inputStream.nextLine().split(" ");
            //possibly throws ArrayIndexOutOfBoundsException
            //todo parseInt, parsexxx
            //todo add monster when list is empty
            switch(entityArgs[0]){
                case "player":
                    //reserve coordinates for register Player
                    playerX = Integer.parseInt(entityArgs[1]);
                    playerY = Integer.parseInt(entityArgs[2]);
                    break;
                case "monster":
                    entityList.add(new Monster(entityArgs[3], Integer.parseInt(entityArgs[1]), Integer.parseInt(entityArgs[2]), Integer.parseInt(entityArgs[4]), Integer.parseInt(entityArgs[5])));
                    break;
                case "item":
                    //possibly throws IllegalArgumentException, NullPointerException
                    entityList.add(new Item(Integer.parseInt(entityArgs[1]), Integer.parseInt(entityArgs[2]), entityArgs[3]));
                    break;
                default:
                    //todo
                    //throw new Exception("entityArg[0] not recognized: " + entityArgs[0]);
            }
        }
    }
    //register Player from GameEngine, set coordinates if loaded from level data.
    public void register(Entity entity){
        if(entity == null){
            return;
        }
        if(entity.getClass() == Player.class){
            Player player = (Player) entity;
            entityList.addFirst(player);
            if(playerX != null){
                player.setX(playerX);
                player.setY(playerY);
            }
        }
        else if(entity.getClass() == Monster.class){
            Monster monster = (Monster) entity;
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
     * Constructs World with two Strings, which are used to represent player and monster on map.
     * @param playerName    name String of player
     * @param monsterName   name String of monster
     */



    //IOExceptions
    //todo usage: create Scanner and close
    public static Scanner load(String file) throws GameLevelNotFoundException, IOException {
        try{
            Scanner inputStream;
            inputStream = new Scanner(new FileInputStream(file+".dat"));
            return inputStream;
        }
        catch(FileNotFoundException e){
            throw new GameLevelNotFoundException("Map not found.");
        }

    }

    /**
     * Visually renders the game world map.
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
            if ((mX - pX > 2) || (mX - pX < -2) ){
                continue;
            }
            if ((mY - pY > 2) || (mY - pY < -2) ){
                continue;
            }
            int mXNext = mX;
            int mYNext = mY;
            String cmdX = "";
            String cmdY = "";
            //cannot use ternary operator.
            if(mX < pX){
                mXNext = mX + 1;
                cmdX = GameEngine.COMMAND_MAP_RIGHT;
            }
            else if(pX < mX){
                mXNext = mX - 1;
                cmdX = GameEngine.COMMAND_MAP_LEFT;
            }
            if(mY < pY){
                mYNext = mY + 1;
                cmdY = GameEngine.COMMAND_MAP_DOWN;
            }
            else if(pY < mY){
                mYNext = mY - 1;
                cmdY = GameEngine.COMMAND_MAP_UP;
            }
            if(traversable(mXNext, mY) && mX != pX){
                move(monster, cmdX);
            }
            else if(traversable(mX, mYNext)){
                move(monster, cmdY);
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

    public void scanEncounter(){
        Iterator<Entity> iterator = entityList.iterator();
        Player player = (Player) iterator.next();

        while( iterator.hasNext()) {
            Entity e = iterator.next();
            if(player.encounter(e) && e.getClass() == Monster.class){
                player.battle( (Monster) e);
                if(player.getCurrentHealth() <= 0){
                    completed = true;
                    //todo stop iteration and return
                }
                //defeated a Monster
                else{
                    iterator.remove();
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

    public boolean isCompleted(){
        return completed;
    }
}
