package Battleships;


import java.io.File;
import java.util.Scanner;

public class CLIView {

    private static BModel model = new BModel();

    public static void main(String args[]) {
        handleChoice(displayMenu());


    }

    private static int displayMenu() {
        boolean selected = false;
        System.out.println("Welcome to BATTLESHIPS! \n");
        System.out.println("Please select an option");
        int i = 0;
        while (!selected) {

            System.out.println(" 1. Start a game with the default number of ships");
            System.out.println(" 2. Start a game with a random number of ships");
            System.out.println(" 3. Load a game");
            System.out.println(" 4. Quit game");


            Scanner scan = new Scanner(System.in);

            if (scan.hasNextInt()) {
                i = scan.nextInt();
                if (i < 5 && i > 0) {
                    selected = true;
                } else {
                    System.out.println("INVALID INPUT!");
                    System.out.println("Please select a number between 1-4 inclusive");
                }

            } else {
                System.out.println("INVALID INPUT");
                System.out.println("Please select a number between 1-4 inclusive");
            }


        }
        return i;
    }

    private static void handleChoice(int choice) {
        switch(choice) {
            case 1:
                model.startDefaultGame();
                runGame();
                break;
            case 2:
                model.startRandomGame();
                runGame();
                break;
            case 3:
                if (loadGame()) {
                    runGame();
                } else {
                    System.out.println("Unable to load game");
                }
                break;
            case 4:
                System.exit(0);
                break;
        }

    }

    private static boolean saveGame() {
        System.out.println("Please enter the save game name...");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();
        return model.saveGame(fileName);
    }

    private static boolean loadGame() {
        System.out.println("Please enter the save game name...");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();
        File fileIn = new File(fileName);
        return model.loadGame(fileIn);
    }

    private static void runGame() {
        model.printBoard();
        while (!model.gameOver()) {
            int[] point = getCoordInput();
            int y = point[0];
            int x = point[1];
            if (y != 0 && x != 0) {
                if (hitShip(x,y)) {
                    model.printBoard();
                    System.out.println("Ship Hit!");
                } else {
                    model.printBoard();
                    System.out.println("MISS!");
                }
            }


        }
        System.out.println("GAME OVER! YOU WIN!");
    }

    private static boolean hitShip(int xCoord, int yCoord) {
        boolean hit = false;
        if (model.isAHit(xCoord, yCoord)) {
            if (model.hitShip(xCoord,yCoord)) {
                hit = true;
            } else {
                System.out.println("Ship already hit");
            }

        }
        return hit;
    }

    private static int[] getCoordInput() {
        int[] point = new int[2];
        boolean valid = false;
        char[] alpha = "ZABCDEFGHIJ".toCharArray();
        String input;
        Scanner scan = new Scanner(System.in);

        while (!valid) {
            System.out.println("Please enter a grid location e.g G3");

            if (scan.hasNext()) {
                input = scan.nextLine();
                input = input.toUpperCase();
                if (input.equals("QUIT")) {
                    System.exit(0);
                } else if (input.equals("SAVE")) {
                    if (saveGame()) {
                        System.out.println("Save Successful!");
                    } else {
                        System.out.println("Save Unsuccessful!");
                    }
                    valid = true;
                } else if (input.equals("LOAD")) {
                    if (loadGame()) {
                        System.out.println("Load Successful");
                        model.printBoard();
                    } else {
                        System.out.println("Load Unsuccessful");
                    }
                    valid = true;
                } else {
                    if (input.length() > 1 && input.length() <= 3) {
                        for (int i = 0; i < alpha.length; i++) {
                            if (input.charAt(0) == alpha[i] && input.charAt(0) != 'Z') {
                                point[0] = i;
                                i = 10;
                                try {
                                    int x = Integer.parseInt(input.substring(1));
                                    if (x > 10 || x < 1) {
                                        System.out.println("Column index out of range 1-10 inclusive");
                                    } else {
                                        if (x == 10) {
                                            point[1] = 10;
                                            valid = true;
                                        }else {
                                            point[1] = x;
                                            valid = true;
                                        }
                                    }

                                } catch (NumberFormatException e) {
                                }

                            }
                        }
                    }
                }


            }
            if (!valid) {
                System.out.println("INVALID INPUT");
            }
        }
        return point;

    }


}
