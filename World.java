package rogue;
/**
 * The world of game, reponsible for rendering map, moving Avatars on map and triggering encounter.
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 *
 */
public class World {
    /**
     *The width of world map.
     */
    public static final int WORLD_WIDE = 6;
    /**
     *The height of world map.
     */
    public static final int WORLD_HIGH = 4;

    private static final int PLAYER_X0 = 1;
    private static final int PLAYER_Y0 = 1;
    private static final int MONSTER_Y0 = 2;
    private static final int MONSTER_X0 = 4;

    private AvatarOnMap playerOnMap;
    private AvatarOnMap monsterOnMap;
    /**
     * Constructs World with two Strings, which are used to represent player and monster on map.
     * @param playerName    name String of player
     * @param monsterName   name String of monster
     */
    public World(String playerName, String monsterName){
        playerOnMap = new AvatarOnMap(PLAYER_X0, PLAYER_Y0, playerName);
        monsterOnMap = new AvatarOnMap(MONSTER_X0, MONSTER_Y0, monsterName);
    }
    /**
     * Resets World with two Strings, which are used to represent player and monster on map.
     * Player and monster are put back at initial coordinates.
     * Executes when game restarts.
     * @param playerName    name String of player
     * @param monsterName   name String of monster
     */
    public void reset(String playerName, String monsterName){
        playerOnMap = new AvatarOnMap(PLAYER_X0, PLAYER_Y0, playerName);
        monsterOnMap = new AvatarOnMap(MONSTER_X0, MONSTER_Y0, monsterName);
    }
    /**
     * Visually renders the game world map.
     */
    public void render(){
        for(int y = 0; y < WORLD_HIGH; y++){
            for(int x = 0; x < WORLD_WIDE; x++){
                if(x == playerOnMap.x && y == playerOnMap.y){
                    System.out.print(playerOnMap.callSign.toUpperCase().charAt(0));
                }
                else if(x == monsterOnMap.x && y == monsterOnMap.y){
                    System.out.print(monsterOnMap.callSign.toLowerCase().charAt(0));
                }
                else{
                    System.out.print('.');
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    /**
     * Moves the Player by one step.
     * @param command   A String as command to move, can be "w" "a" "s" "d"
     */
    public void movePlayer(String command){
        switch(command){
            case GameEngine.COMMAND_MAP_UP:
                playerOnMap.y = playerOnMap.y <= 0 ? playerOnMap.y : playerOnMap.y - 1;
                break;
            case GameEngine.COMMAND_MAP_DOWN:
                playerOnMap.y = playerOnMap.y >= WORLD_HIGH - 1 ? playerOnMap.y : playerOnMap.y + 1;
                break;
            case GameEngine.COMMAND_MAP_LEFT:
                playerOnMap.x = playerOnMap.x <= 0 ? playerOnMap.x : playerOnMap.x - 1;
                break;
            case GameEngine.COMMAND_MAP_RIGHT:
                playerOnMap.x = playerOnMap.x >= WORLD_WIDE - 1 ? playerOnMap.x : playerOnMap.x + 1;
                break;
            default:
                //System.out.println("World.move(): command not recognized");
        }
    }

    /**
     * Returns true if player encounters monster.
     * Should be called after every move
     * @return boolean value indicating possible encounter
     */
    public boolean encounter(){
        return playerOnMap.equals(monsterOnMap);
    }
}
/**
 * Helper class
 * Managed by World to represent Avatars on map.
 */
class AvatarOnMap{
    int x;
    int y;
    String callSign;
    public AvatarOnMap(int x, int y, String callSign){
        this.x = x;
        this.y = y;
        this.callSign = callSign;
    }

    /**
     * callSign is not matched.
     * @return boolean for coordinates match.
     */
    public boolean equals(Object o){
        if(!(o instanceof AvatarOnMap)){
            return false;
        }
        AvatarOnMap another = (AvatarOnMap) o;
        if(x != another.x || y != another.y){
            return false;
        }
        return true;
    }
}
