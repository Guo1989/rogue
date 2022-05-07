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

    public static final int PLAYER_DEFAULT_X = 1;
    public static final int PLAYER_DEFAULT_Y = 1;
    public static final int MONSTER_DEFAULT_Y = 2;
    public static final int MONSTER_DEFAULT_X = 4;
    //player monster ... item ...
    private LinkedList<Entity> entityList;
    private int width;
    private int height;
    private String[] map;
    //also in entityList[0]
    private Player player;


    public World(Scanner inputStream, Player player) throws NoSuchElementException, ArrayIndexOutOfBoundsException, IllegalArgumentException, NullPointerException, Exception{
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
            switch(entityArgs[0]){
                case "player":
                    //register Player and set coordinates
                    registerPlayer(player);
                    player.setX(Integer.parseInt(entityArgs[1]));
                    player.setY(Integer.parseInt(entityArgs[2]));
                    break;
                case "monster":
                    entityList.add(1, new Monster(entityArgs[3], Integer.parseInt(entityArgs[1]), Integer.parseInt(entityArgs[2]), Integer.parseInt(entityArgs[4]), Integer.parseInt(entityArgs[5])));
                    break;
                case "item":
                    //possibly throws IllegalArgumentException, NullPointerException
                    entityList.add(new Item(Integer.parseInt(entityArgs[1]), Integer.parseInt(entityArgs[2]), entityArgs[3]));
                    break;
                default:
                    //todo
                    throw new Exception("entityArg[0] not recognized: " + entityArgs[0]);
            }
        }
    }
    //register Player from GameEngine
    private void registerPlayer(Player player){
        this.player = player;
        entityList.addFirst(player);
    }

    /**
     * Constructs World with two Strings, which are used to represent player and monster on map.
     * @param playerName    name String of player
     * @param monsterName   name String of monster
     */
     //todo: backward compatibility
    //public World(String playerName, String monsterName){
    //}



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
        for(int y = 0; y < height; y++){

            System.out.println(map[y]);
        }
    }
    /**
     * A wrapper to call player's move, taking account of the size of map.
     * @param command   A String as command to move, can be "w" "a" "s" "d"
     */
    public void movePlayer(String command){
        switch(command){
            case GameEngine.COMMAND_MAP_UP:

                break;
            case GameEngine.COMMAND_MAP_DOWN:

                break;
            case GameEngine.COMMAND_MAP_LEFT:

                break;
            case GameEngine.COMMAND_MAP_RIGHT:

                break;
            default:
                //System.out.println("World.move(): command not recognized");
        }
    }
}
