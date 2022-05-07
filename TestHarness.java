package rogue;

import java.util.Scanner;
import java.io.IOException;

public class TestHarness {

	public static void main(String[] args) {

		// TODO: Use this as a testing playground to test the different parts of your code.
		// The code in this class will not be assessed.
        testWorld();

	}

    public static void testWorld(){
        try{
            Player myPlayer = new Player("zxc");
            Scanner inputStream = World.load("simple");
            World testWorld = new World(inputStream);
            testWorld.register(myPlayer);
            testWorld.render();
            System.out.println("testWorld end.");
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("IndexOutOfBoundsException" + e.getMessage());
            e.printStackTrace();
        }
        catch(GameLevelNotFoundException e){
            System.out.println("GameLevelNotFoundException" + e.getMessage());
        }
        catch(IllegalArgumentException e){
            System.out.println("IllegalArgumentException" + e.getMessage());
        }

        catch(IOException e){
            System.out.println("IOException" + e.getMessage());
        }
        /*
        catch(IOException e){
            System.out.println("IOException" + e.getMessage());
        }

        catch(Exception e){
            System.out.println("Exception" + e.getMessage());
        }
        */

    }

}
