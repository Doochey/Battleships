package Battleships;


import java.io.File;

public class BController {

    private BModel model;
    private BView view;

    BController(BModel model) {
        this.model = model;
    }

    public void setView(BView view) {
        this.view = view;
    }

    public void startDefaultGame() {
        model.startDefaultGame();
    }

    public void startRandomGame() {
        model.startRandomGame();
    }

    public boolean isAHit(int xCoord, int yCoord) {
        if (xCoord <11 && xCoord > 0 && yCoord < 11 && yCoord >= 0) {
            return model.isAHit(xCoord, yCoord);
        } else {
            return false;
        }
    }

    public boolean hitShip(int xCoord,int yCoord) {
        if (xCoord <11 && xCoord > 0 && yCoord < 11 && yCoord >= 0) {
            return model.hitShip(xCoord, yCoord);
        } else {
            return false;
        }

    }

    public boolean isGameOver() {
        return model.gameOver();
    }

    public boolean loadGame(File file) {
        return model.loadGame(file);
    }

    public boolean saveGame(File file) {
        return model.saveGame(file.getName());
    }

    public char getGameBoardValue(int xCoord, int yCoord) {
        if (xCoord <=11 && xCoord >= 0 && yCoord <= 11 && yCoord >= 0) {
            return model.getGameBoard()[yCoord][xCoord];
        } else {
            return 'Q';
        }

    }
}
