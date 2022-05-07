package rogue;

import java.util.Scanner;

public class TestHarness {

	public static void main(String[] args) {

		// TODO: Use this as a testing playground to test the different parts of your code.
		// The code in this class will not be assessed.
        testWorld();

	}

    public static void testWorld(){
        try{
            Player myPlayer = new Player("zxc");
            Scanner inputStream = World.load("rogue/simple");
            World testWorld = new World(inputStream, myPlayer);
            testWorld.render();
            System.out.println("testWorld end.");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

}
