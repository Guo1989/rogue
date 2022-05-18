package rogue;

import java.util.Scanner;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The engine that drives the whole game, manages the Scanner to read in user input,
 * Loads data of Player and level.
 * Manages the World object.
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 */
public class GameEngine {
	/**
     * Command avaliable in mainmenu.
	 * gets help message.
     */
	public static final String COMMAND_MAINMENU_HELP = "help";
	/**
     * Command avaliable in mainmenu.
	 * Lists available commands in main menu.
     */
	public static final String COMMAND_MAINMENU_LISTCOMMAND = "commands";
	/**
     * Command avaliable in mainmenu.
	 * Starts a new round of game.
     */
	public static final String COMMAND_MAINMENU_START = "start";
	/**
     * Command avaliable in mainmenu.
	 * Loads player data from file.
     */
	public static final String COMMAND_MAINMENU_LOAD = "load";
	/**
     * Command avaliable in mainmenu.
	 * Saves player data to file.
     */
	public static final String COMMAND_MAINMENU_SAVE = "save";
	/**
     * Command avaliable in mainmenu.
	 * Creates or shows Player in GameEngine.
     */
	public static final String COMMAND_MAINMENU_PLAYER = "player";
	/**
     * Command avaliable in mainmenu.
	 * Creates Monster in GameEngine.
     */
	public static final String COMMAND_MAINMENU_MONSTER = "monster";
	/**
     * Command avaliable in mainmenu.
	 * Exits.
     */
	public static final String COMMAND_MAINMENU_EXIT = "exit";
	/**
     * Command avaliable in World map.
	 * Returns to mainmenu.
     */
	public static final String COMMAND_MAP_HOME = "home";
	/**
     * Command avaliable in World map.
	 * Moves player up one step.
     */
	public static final String COMMAND_MAP_UP = "w";
	/**
     * Command avaliable in World map.
	 * Moves player down one step.
     */
	public static final String COMMAND_MAP_DOWN = "s";
	/**
     * Command avaliable in World map.
	 * Moves player left one step.
     */
	public static final String COMMAND_MAP_LEFT = "a";
	/**
     * Command avaliable in World map.
	 * Moves player right one step.
     */
	public static final String COMMAND_MAP_RIGHT = "d";
	/**
     * Sign used throughout game.
	 * Prompts user for input.
     */
	public static final String SIGN_PROMPT = "> ";


	// To read user input.
	private Scanner terminal = new Scanner(System.in);
	private World world;
	private Player player;
	private Monster monster;

	/**
     * Main serves as a test function.
	 * It instantiates a GameEngine instance and calls runGameLoop() to boot up the game.
	 * @param args not evaluated for now
	 * @see runGameLoop
     */
	public static void main(String[] args) {

		// Creates an instance of the game engine.
		GameEngine gameEngine = new GameEngine();

		// Runs the main game loop.
		gameEngine.runGameLoop();

	}


	/**
	 * Logic for running the main game loop.
	 */
	public void runGameLoop() {
		displayMainMenu();
		while(true){
			getCommand();
		}

	}
	/**
	 *  Get a command from terminal and execute.
	 */
	private void getCommand(){
		String command = promptGet(SIGN_PROMPT);
		String[] commandargs = command.split(" ");
		switch(commandargs[0]){
			case COMMAND_MAINMENU_HELP:
				displayHelpText();
				break;
			case COMMAND_MAINMENU_LISTCOMMAND:
				dispalyCommands();
				break;
			case COMMAND_MAINMENU_START:
				startGame(commandargs);
				break;
			case COMMAND_MAINMENU_SAVE:
				savePlayer();
				break;
			case COMMAND_MAINMENU_LOAD:
				loadPlayer();
				break;
			case COMMAND_MAINMENU_PLAYER:
				genPlayer();
				break;
			case COMMAND_MAINMENU_MONSTER:
				genMonster();
				break;
			case COMMAND_MAINMENU_EXIT:
				exit();
				break;
			default:
				//comment out since it fails a test.
				//System.out.println("command not recognized");
		}
	}

	/**
	 *  Starts a game with player and monster healed
	 */
	private void startGame(String[] commandargs) {
		//Invalid arguments
		if(commandargs.length > 2){
			promptPressReturn("Received " + commandargs.length + "args, expecting 1 or 2.", System.err);
			return;
		}
		if(player == null){
			promptPressReturn("No player found, please create a player with 'player' first.\n", System.err);
			return;
		}else if(commandargs.length == 1 && monster == null){
			promptPressReturn("No monster found, please create a monster with 'monster' first.\n", System.err);
			return;
		}

		//create world from .dat
		if(commandargs.length == 2){
			try{
				Scanner inputStream = World.load(commandargs[1]);
	            world = new World(inputStream);
	        }
			catch(GameLevelNotFoundException e){
				promptPressReturn("Map not found.\n", System.err);
				return;
			}
			catch(IOException e){
				promptPressReturn("An error occurred while loading the file.\n", System.err);
				return;
			}
	        catch(Exception e){
				promptPressReturn(e.getMessage(), System.err);
				return;
	        }

		}
		//Or,
		//create default world
		if(commandargs.length == 1){
			//possibly delete previous world
			world = new World();
		}
		//heal and reposition in registration
		world.register(player);

		if(monster != null){
			world.register(monster);
			//manully generated monster need reposition since it has no coordinates in .dat file
			monster.reposition(World.MONSTER_DEFAULT_X, World.MONSTER_DEFAULT_Y);
		}

		// infinite loop for move and battle
		while(true){
			world.render();
			String command = promptGet(SIGN_PROMPT);
			switch(command){
				case COMMAND_MAP_HOME:
					player.setPerk(0);
					promptPressReturn("Returning home...\n");
					return;
				default:
					world.moveMonsters();
					world.movePlayer(command);
					//battle
					//pickup
					world.scanEncounter();
					//mission completed or player dead
					if(world.isCompleted()){
						promptPressReturn("");
						return;
					}
			}
		}
	}

	private void savePlayer(){
		if(player == null){
			System.err.println("No player data to save.\n");
			return;
		}
		try{
			player.save();
			System.out.println("Player data saved.\n");
		}
		catch(FileNotFoundException e){
            System.err.println("GameEngine#savePlayer() " + e.getMessage());
        }
	}

	//load to override; heal
	private void loadPlayer(){
		try{
			player = Player.load();
			System.out.println("Player data loaded.\n");

		}
		catch (FileNotFoundException e) {
            System.err.println("No player data found.\n");
        }
	}

	/**
	 * Generates a Player object or show Player's name, level, damage, health if Player exists.
	 *
	 */
	private void genPlayer(){
		if(player == null){
			String name = promptGet("What is your character's name?\n");
			player = new Player(name);
			System.out.println("Player '" + name + "' created.");
		}
		else{
			System.out.println(player.getName() + " (Lv. " + player.getLevel() + ")");
			System.out.println("Damage: " + player.getDamage());
			System.out.println("Health: " + player.getCurrentHealth() + "/" + player.getMaxHealth());
		}
		promptPressReturn("");
	}

	private void genMonster(){
		String name = promptGet("Monster name: ");

		int health = promptGetInt("Monster health: ");

		int damage = promptGetInt("Monster damage: ");

		monster = new Monster(name, health, damage);

		System.out.printf("Monster '%s' created.\n", name);

		promptPressReturn("");
	}

	private void exit(){
		System.out.println("Thank you for playing Rogue!");
		System.exit(0);
	}

	/**
	 * Dispalys the created player and monster, with health details.
	 * Example
	 * "Player: Bilbo 20/20  | Monster: Slime 10/10"
	 */
	private void dispalyPlayerMonster(){
		String playerText = "[None]";
		String monsterText = "[None]";
		if(player != null){
			playerText = player.getName() + " " + player.getCurrentHealth() + "/" + player.getMaxHealth();
		}
		if(monster != null){
			monsterText = monster.getName() + " " + monster.getCurrentHealth() + "/" + monster.getMaxHealth();
		}
		System.out.printf("Player: %s  | Monster: %s\n", playerText, monsterText);
	}

	/*
	 *  Displays the title text.
	 */
	private void displayTitleText() {

		String titleText = " ____                        \n" +
				"|  _ \\ ___   __ _ _   _  ___ \n" +
				"| |_) / _ \\ / _` | | | |/ _ \\\n" +
				"|  _ < (_) | (_| | |_| |  __/\n" +
				"|_| \\_\\___/ \\__, |\\__,_|\\___|\n" +
				"COMP90041   |___/ Assignment ";

		System.out.println(titleText);
		System.out.println();

	}
	/**
	 * Displays help text.
	 */
	private void displayHelpText(){
		String helpText =
		"Type 'commands' to list all available commands\n" +
		"Type 'start' to start a new game\n" +
		"Create a character, battle monsters, and find treasure!\n";
		System.out.println(helpText);
	}
	/**
	 *  Dispalys commands.
	 */
	private void dispalyCommands(){
		String commandsText =	COMMAND_MAINMENU_HELP + "\n" +
								COMMAND_MAINMENU_PLAYER + "\n" +
								COMMAND_MAINMENU_MONSTER + "\n" +
								COMMAND_MAINMENU_START + "\n" +
								COMMAND_MAINMENU_EXIT + "\n";
		System.out.println(commandsText);
	}
	/**
	 *  Dispalys main menu including title, player&monster and a few words.
	 */
	private void displayMainMenu(){
		displayTitleText();

 		dispalyPlayerMonster();

		System.out.println("\nPlease enter a command to continue.\n" +
							"Type 'help' to learn how to get started.\n");
	}
	/**
	 * Helper function
	 * Prompts user first,
	 * then askes user, feels like press to return.
	 */
	private void promptPressReturn(String message){
		System.out.print(message);
		promptGet("\n(Press enter key to return to main menu)\n");
		displayMainMenu();
	}

	private void promptPressReturn(String message, PrintStream outputStream){
		outputStream.print(message);
		promptGet("\n(Press enter key to return to main menu)\n");
		displayMainMenu();
	}

	/**
	 * Helper function
	 * Prompts user first about what to input.
	 * Gets an int from terminal, looping while input is invalid.
	 * Deals with trailing newline after invocation.
	 */
	private int promptGetInt(String message){
		System.out.print(message);
		while (!terminal.hasNextInt()) {
			System.out.println("Termianl: Input type not int");
			terminal.nextLine();
		}
		int res = terminal.nextInt();
		//clean trailing newline
		terminal.nextLine();
		return res;
	}
	/**
	 * Helper function
	 * Prompts user and gets a line.
	 */
	private String promptGet(String message){
		System.out.print(message);
		String line = terminal.nextLine();
		return line;
	}

}
