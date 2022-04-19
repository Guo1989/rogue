package rogue;

import java.util.Scanner;
/**
 * The engine that drives the whole game, manages the Scanner to read in user input,
 * generates and keeps references to Player and Monster,
 * manages the World object.
 * @author Xiaocong Zhang xiaocongz@student.unimelb.edu.au 1292460
 *
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
		switch(command){
			case COMMAND_MAINMENU_HELP:
				displayHelpText();
				break;
			case COMMAND_MAINMENU_LISTCOMMAND:
				dispalyCommands();
				break;
			case COMMAND_MAINMENU_START:
				startGame();
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
	private void startGame(){
		//prepare player and monster
		if(player == null){
			promptPressReturn("No player found, please create a player with 'player' first.\n");
			return;
		}
		if(monster == null){
			promptPressReturn("No monster found, please create a monster with 'monster' first.\n");
			return;
		}
		player.setCurrentHealth(player.getMaxHealth());
		monster.setCurrentHealth(monster.getMaxHealth());

		//create world obj or reset
		if(world == null){
			world = new World(player.getName(), monster.getName());
		}
		world.reset(player.getName(), monster.getName());

		// Runs one round of game in this infinite loop, till get "home" command or enter battle.
		while(true){
			world.render();
			String command = promptGet(SIGN_PROMPT);
			switch(command){
				case COMMAND_MAP_HOME:
					promptPressReturn("Returning home...\n");
					return;
				default:
					world.movePlayer(command);
					if(world.encounter()){
						battle();
						promptPressReturn("");
						return;
					}
			}


		}
	}
	/**
	 * Logics for battle loop, player and monster take turns to attack.
	 * @see Avatar#attack()
	 */
	private void battle(){
		System.out.printf("%s encounterd a %s\n\n", player.getName(), monster.getName());
		while(true){
			System.out.println(player.getName() + " " + player.getCurrentHealth() + "/" + player.getMaxHealth()
			+ " | " + monster.getName() + " " + monster.getCurrentHealth() + "/" + monster.getMaxHealth());
			player.attack(monster);
			if(monster.getCurrentHealth() <= 0){
				System.out.printf("%s wins!\n", player.getName());
				return;
			}
			monster.attack(player);
			if(player.getCurrentHealth() <= 0){
				System.out.printf("%s wins!\n", monster.getName());
				return;
			}
			System.out.println();
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
	 * then askes user feels like press to return.
	 */
	private void promptPressReturn(String message){
		System.out.print(message);
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
