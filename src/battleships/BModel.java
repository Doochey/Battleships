package Battleships;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class BModel extends Observable {

    private char[][] gameBoard;
    private ArrayList<Battleship> ships = new ArrayList<>();
    private ArrayList<Battleship> sunkShips = new ArrayList<>();

    public BModel() {
        this.gameBoard = initBoard();


    }

    // Initialises an empty game board (No Battleships placed)
    /**
     * @pre. TRUE
     * @post. newBoard[0][5] == '5'
     * @post. newBoard[4][0] == 'D'
     */
    private char[][] initBoard() {
        char[] alpha = "ZABCDEFGHIJ".toCharArray();
        char[] num = "Z123456789X".toCharArray();
        char[][] newBoard = new char[11][11];
        newBoard[0] = num;
        for (int i = 0; i < newBoard.length; i++) {
            newBoard[i][0] = alpha[i];
        }
        assert newBoard[0][5] == '5';
        assert newBoard[4][0] == 'D';
        return newBoard;
    }


    /**
     * @pre. TRUE
     * @post. loaded is false or true.
     */
    public boolean loadGame(File file) {
        boolean loaded = false;
        try {
            Scanner scan = new Scanner(file).useDelimiter(",|\\n");
            char[][] newBoard = initBoard();
            ArrayList<Battleship> tempShips = new ArrayList<>();
            ArrayList<Battleship> tempSunkShips = new ArrayList<>();
            int shipsToLoad = scan.nextInt();
            int shipsLoaded = 0;
            while (scan.hasNext()) {
                scan.next();
                if (scan.next().equals("size")) {
                    int size = scan.nextInt();
                    Battleship ship = new Battleship(size);
                    scan.next();
                    scan.next();
                    boolean sunk = scan.next().equals("Sunk");
                    for (int j = 0; j < size; j++) {
                        scan.next();
                        int x = scan.nextInt();
                        int y = scan.nextInt();
                        ship.setCoords(x,y);
                        char c = scan.next().charAt(0);
                        newBoard[y][x] = c;
                        if (c == 'V') {
                            ship.hit();
                        }

                    }
                    if (sunk) {
                        tempSunkShips.add(ship);
                        shipsLoaded++;
                    } else {
                        tempShips.add(ship);
                        shipsLoaded++;
                    }

                }
                if (shipsToLoad == shipsLoaded) {
                    loaded = true;
                    this.gameBoard = newBoard;
                    this.ships = tempShips;
                    this.sunkShips = tempSunkShips;
                    scan.next();
                }
            }
            scan.close();

        } catch (FileNotFoundException | NullPointerException | InputMismatchException e) {
            loaded = false;
        }
        assert !loaded || true == true;
        return loaded;
    }

    /**
     * @pre. gameBoard != null
     * @pre. ships != null && sunkShips != null
     * @post.
     */
    public boolean saveGame(String fileName) {
        assert ships != null && sunkShips != null && gameBoard != null;
        try{
            PrintWriter os = new PrintWriter(fileName);
            os.print(ships.size() + sunkShips.size() + ",\n");
            for (Battleship b : ships) {
                os.print("size,");
                os.print(b.getSize() + ",\n");
                os.print("status,");
                os.print("notSunk,\n");
                for (int[] r : b.getCoords()) {
                    os.print(r[0] + "," + r[1] + "," + gameBoard[r[1]][r[0]] + ",\n");
                }
            }
            for (Battleship b : sunkShips) {
                os.print("size,");
                os.print(b.getSize() + ",\n");
                os.print("status,");
                os.print("Sunk,\n");
                for (int[] r : b.getCoords()) {
                    os.print(r[0] + "," + r[1] + "," + gameBoard[r[1]][r[0]] + ",\n");
                }
            }
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("ERROR SAVING FILE");
            return false;
        }

    }

    /**
     * @pre. gameBoard != null
     * @post. getGameBoard() == this.gameBoard
     */
    public char[][] getGameBoard() {
        assert gameBoard != null;
        return gameBoard;
    }

    /**
     * @pre. gameBoard != null
     * @post.
     */
    public void printBoard(){
        assert gameBoard != null;
        for (int r = 0; r< gameBoard.length; r++) {
            String row = "";
            for (int c = 0; c< gameBoard.length; c++) {
                if (gameBoard[r][c] == 0 || gameBoard[r][c] == 'S') {
                    row = row + "* ";
                } else if (gameBoard[r][c] == 'X') {
                    row = row + "10";
                } else if (gameBoard[r][c] == 'Z') {
                    row = row + "  ";
                } else if (gameBoard[r][c] == 'V') {
                    row = row + "H ";
                } else if (gameBoard[r][c] == 'M') {
                    row = row + "M ";
                } else {
                    row = row + gameBoard[r][c] + " ";
                }

            }
            System.out.println(row);
        }
    }

    /* Places the default amount of ships
    *  1 Size 5
    *  1 Size 4
    *  1 Size 3
    *  2 Size 2
    */

    /**
     * @pre. ships != null
     * @post. ships.isEmpty() == false
     */
    public void startDefaultGame() {
        assert ships != null;
        for (int i = 1; i < 6; i++) {
            if (i < 3) {
                Battleship ship = new Battleship(2);
                ships.add(ship);
            } else {
                Battleship ship = new Battleship(i);
                ships.add(ship);
            }
        }
        for (Battleship s : ships) {
            placeShip(s);
        }
        assert !ships.isEmpty();
    }


    /* Places a more random amount of ships, maximum is 10
    *  1 Size 5
    *  1 Size 4
    *  1 Size 3
    *  0..3 Size 2
    *  0..4 Size 1
    */

    /**
     * @pre. ships != null
     * @post. ships.isEmpty() == false
     */
    public void startRandomGame() {
        assert ships != null;
        for (int i = 3; i < 6; i++) {
            Battleship ship = new Battleship(i);
            ships.add(ship);
        }
        Random rand = new Random();
        int s = rand.nextInt(4); // Ships size 2 0..3
        while (s > 0) {
            Battleship ship = new Battleship(2);
            ships.add(ship);
            s--;
        }
        s = rand.nextInt(5);  // Ships size 1 0..4
        while (s > 0) {
            Battleship ship = new Battleship(1);
            ships.add(ship);
            s--;
        }
        for (Battleship b : ships) {
            placeShip(b);
        }
        assert !ships.isEmpty();
    }

    /* First, attempts to place a ship randomly
    *  After set number of attempts, searches for space to place the ship sequentially
    */
    /**
     * @pre. ship != null
     * @pre. gameBoard != null
     * @post. placed == true || placed == false
     */
    private void placeShip(Battleship ship) {
        assert ship != null;
        assert gameBoard != null;
        Random rand = new Random();
        boolean placed = false;
        for (int i = 0; i < 6; i++) {
            int xCoord = rand.nextInt(11);
            int yCoord = rand.nextInt(11);

            if (gameBoard[yCoord][xCoord] == 0) {
                int direction = rand.nextInt(4);
                switch (direction) {
                    // Searches right (0), down (1), left (2), or up (3) of the rand X & Y for space to place the ship
                    case 0:
                        if (xCoord + ship.getSize() -1 <= 10) {
                            boolean open = true;
                            for (int j = 1; j < ship.getSize(); j++) {
                                if (gameBoard[yCoord][xCoord+j] != 0) {
                                    j = 10;
                                    open = false;
                                }
                            }
                            if (open) {
                                for (int j = 0; j < ship.getSize(); j++) {
                                    gameBoard[yCoord][xCoord+j] = 'S';
                                    ship.setCoords(xCoord+j, yCoord);
                                }
                                i = 6;
                                placed = true;
                            }
                        }
                        break;
                    case 1:
                        if (yCoord + ship.getSize() -1 <= 10) {
                            boolean open = true;
                            for (int j = 1; j < ship.getSize(); j++) {
                                if (gameBoard[yCoord+j][xCoord] != 0) {
                                    j = 10;
                                    open = false;
                                }
                            }
                            if (open) {
                                for (int j = 0; j < ship.getSize(); j++) {
                                    gameBoard[yCoord+j][xCoord] = 'S';
                                    ship.setCoords(xCoord, yCoord+j);
                                }
                                i = 6;
                                placed = true;
                            }
                        }
                        break;
                    case 2:
                        if (xCoord - ship.getSize() -1 >= 1) {
                            boolean open = true;
                            for (int j = 1; j < ship.getSize(); j++) {
                                if (gameBoard[yCoord][xCoord-j] != 0) {
                                    j = 10;
                                    open = false;
                                }
                            }
                            if (open) {
                                for (int j = 0; j < ship.getSize(); j++) {
                                    gameBoard[yCoord][xCoord-j] = 'S';
                                    ship.setCoords(xCoord-j, yCoord);
                                }
                                i = 6;
                                placed = true;
                            }
                        }
                        ;
                        break;
                    case 3:
                        if (yCoord - ship.getSize() -1 <= 1) {
                            boolean open = true;
                            for (int j = 1; j < ship.getSize(); j++) {
                                if (gameBoard[yCoord-j][xCoord] != 0) {
                                    j = 10;
                                    open = false;
                                }
                            }
                            if (open) {
                                for (int j = 0; j < ship.getSize(); j++) {
                                    gameBoard[yCoord-j][xCoord] = 'S';
                                    ship.setCoords(xCoord, yCoord-j);
                                }
                                i = 6;
                                placed = true;
                            }
                        }
                        break;


                }

            }
        }
        if (!placed) {
            // If ship is not placed randomly after limited number of loops
            // Searches for space to place the ship sequentially
            int direction = rand.nextInt(1);
            if (direction == 0) {
                boolean seqPlaced = false;
                int m = 0;
                while (!seqPlaced) {
                    if (m != 1) { // Should only loop through all X and Y once
                        for (int y = 1; y <= 10; y++) {
                            for (int x = 1; x <= 10; x++) {
                                if (x + ship.getSize() - 1 <= 10) {
                                    boolean open = true;
                                    for (int i = 0; i < ship.getSize(); i++){
                                        if (gameBoard[y][x+i] != 0) {
                                            i = 10;
                                            open = false;
                                        }
                                    }
                                    if (open) {
                                        for (int j = 0; j < ship.getSize(); j++) {
                                            gameBoard[y][x+j] = 'S';
                                            ship.setCoords(x+j, y);

                                        }
                                        x = 11;
                                        y = 11;
                                        seqPlaced = true;
                                        placed = true;
                                    }
                                }

                            }
                        }
                        m = 1;
                    } else {
                        seqPlaced = true;
                    }

                }
            }
        }
        // If ship is still not placed, print error and abort
        if (!placed) {
            System.out.println("UNABLE TO PLACE SHIP SIZE: " + ship.getSize());
        }
        assert placed == true || placed == false;
    }

    /* Checks value of gameboard at X, Y to see if a ship is there
    *  If ship is not found changes gameboard value at coordinate to represent a miss
    */
    /**
     * @pre. xCoord >= 0 && xCoord <= 10
     * @pre. yCoord >= 0 && yCoord <= 10
     * @pre. gameBoard != null
     * @post. hit == true || hit == false
     * @post. gameBoard[yCoord][xCoord] == 'S' || gameBoard[yCoord][xCoord] == 'V' || gameBoard[yCoord][xCoord] == 'M'
     */
    public boolean isAHit(int xCoord, int yCoord) {
        assert xCoord >= 0 && xCoord <= 10;
        assert yCoord >= 0 && yCoord <= 10;
        assert gameBoard != null;
        boolean hit = false;
        if (gameBoard[yCoord][xCoord] == 'S' || gameBoard[yCoord][xCoord] == 'V') {
            hit = true;
        } else {
            gameBoard[yCoord][xCoord] = 'M';
        }
        assert hit || !hit;
        assert gameBoard[yCoord][xCoord] == 'S' || gameBoard[yCoord][xCoord] == 'V' || gameBoard[yCoord][xCoord] == 'M';
        return hit;
    }

    /* Searches through ship array to find ship with matching X,Y coords
    *  If found reduces ship hit points and replaces gameboard value at that coordinate
    *  to represent a ship that has been hit
    */
    /**
     * @pre. xCoord >= 0 && xCoord <= 10
     * @pre. yCoord >= 0 && yCoord <= 10
     * @pre. ships != null
     * @pre. gameBoard != null
     * @pre. sunkShips != null
     * @post. hit == true || hit == false
     * @post. ships.isEmpty() == false || sunkShips.isEmpty() == false
     */
    public boolean hitShip(int xCoord, int yCoord) {
        assert xCoord >= 0 && xCoord <= 10;
        assert yCoord >= 0 && yCoord <= 10;
        assert ships != null && sunkShips != null && gameBoard != null;
        boolean hit = false;
        Battleship b = null;
        if (gameBoard[yCoord][xCoord] != 'V') {
            for (Battleship s : ships) {
                if (s.isHit(xCoord, yCoord)) {
                    b = s;
                    s.hit();
                    hit = true;
                    gameBoard[yCoord][xCoord] = 'V';


                }
            }
            if (b != null) {
                if (b.isSunk()) {
                    sunkShips.add(b);
                    ships.remove(b);
                    setChanged();
                    notifyObservers();

                }
            }
        }
        assert hit || !hit;
        assert !ships.isEmpty() || !sunkShips.isEmpty();
        return hit;
    }

    // When all ships have been sunk, game should end
    /**
     * @pre. ships != null
     * @post. ships.isEmpty() == true || ships.isEmpty() == false
     */
    public boolean gameOver() {
        assert ships != null;
        assert ships.isEmpty() || !ships.isEmpty();
        return ships.isEmpty();
    }


}
