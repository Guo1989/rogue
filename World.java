package rogue;

import java.util.LinkedList;
import java.util.Scanner;
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
    private Integer playerX;
    private Integer playerY;

    public World(){
        width = WORLD_DEFAULT_WIDTH;
        height = WORLD_DEFAULT_HEIGHT;
        map = new String[WORLD_DEFAULT_HEIGHT];
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
            entityList.add(entity);
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
            entityMap[e.getY()].setCharAt(e.getX(), e.render().charAt(0));
        }
        for(int y = 0; y < height; y++){
            System.out.println(entityMap[y]);
        }
    }
    /**
     * A wrapper to call player's move, taking account of the size of map.
     * @param command   A String as command to move, can be "w" "a" "s" "d"
     */
    public void movePlayer(String command){
        Player player = (Player) entityList.getFirst();
        switch(command){
            case GameEngine.COMMAND_MAP_UP:
                if(player.getY() == WORLD_MIN_Y){
                    return;
                }
                if(map[player.getY() - 1].charAt(player.getX()) == '.'){
                    player.setY(player.getY() - 1);
                }
                break;
            case GameEngine.COMMAND_MAP_DOWN:
                if(player.getY() == height - 1){
                    return;
                }
                if(map[player.getY() + 1].charAt(player.getX()) == '.'){
                    player.setY(player.getY() + 1);
                }
                break;
            case GameEngine.COMMAND_MAP_LEFT:
                if(player.getX() == WORLD_MIN_X){
                    return;
                }
                if(map[player.getY()].charAt(player.getX() - 1) == '.'){
                    player.setX(player.getX() - 1);
                }
                break;
            case GameEngine.COMMAND_MAP_RIGHT:
                if(player.getX() == width - 1){
                    return;
                }
                if(map[player.getY()].charAt(player.getX() + 1) == '.'){
                    player.setX(player.getX() + 1);
                }
                break;
            default:
                //System.out.println("World.move(): command not recognized");
        }
    }

    public void encounter(){

    }
}
