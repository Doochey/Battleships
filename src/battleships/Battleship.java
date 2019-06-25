package Battleships;

public class Battleship {

    private int size;
    private int hitsLeft;
    private boolean sunk;
    private int[][] coords;


    public Battleship(int size) {
        this.size = size;
        this.hitsLeft = size;
        this.coords = new int[size][2];
    }

    public boolean isSunk() {
        if (!this.sunk) {
            if (this.hitsLeft == 0) {
                this.sunk = true;
            }
        }
        return this.sunk;
    }

    public void hit() {
        this.hitsLeft--;
    }

    public boolean isHit(int xCoord, int yCoord) {
        boolean found = false;
        for (int row = 0; row < coords.length; row++) {
            if (coords[row][0] == xCoord && coords[row][1] == yCoord) {
                found = true;
            }
        }
        return found;
    }

    public int getSize() { return this.size; }

    public void setCoords(int xCoord, int yCoord) {
        for (int row = 0; row < coords.length; row++) {
            if (coords[row][0] == 0) {
                coords[row][0] = xCoord;
                coords[row][1] = yCoord;
                row=10;
            }
        }
    }

    public int[][] getCoords() {
        return this.coords;
    }




}
