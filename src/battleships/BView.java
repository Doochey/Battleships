package Battleships;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Observable;
import java.util.Observer;


public class BView extends Application implements Observer {

    private BModel model = new BModel();
    private BController controller;
    private VBox root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.controller = new BController(model);
        controller.setView(this);

        root = new VBox();
        primaryStage.setTitle("Battleships");
        Scene myScene = new Scene(root, 700,550);
        displayMainMenu(primaryStage, myScene);

        model.addObserver(this);


    }

    private void displayGame(Stage primaryStage, Scene myScene) {
        root.getChildren().clear();

        HBox hBox = new HBox();

        HBox menu = new HBox();
        menu.setSpacing(5);
        menu.setPadding(new Insets(0,0,0,230));
        Button save = new Button("Save Game");
        save.setMaxWidth(150);
        save.setOnAction(event -> {
            FileChooser files = new FileChooser();
            files.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Documents", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            files.setTitle("Save Game");
            File saveFile = files.showSaveDialog(primaryStage);
            if (saveFile != null) {
                if (!controller.saveGame(saveFile)) {
                    if (root.getChildren().size() == 3) {
                        root.getChildren().remove(2);
                    }
                    Label error = new Label("Error Saving File");
                    error.setPadding(new Insets(5,0,80,265));
                    error.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
                    root.getChildren().add(error);
                }
            }
           });
        Button load = new Button("Load Game");
        load.setMaxWidth(150);
        load.setOnAction(event -> {
            FileChooser files = new FileChooser();
            files.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Documents", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            files.setTitle("Load Game");
            File file = files.showOpenDialog(primaryStage);
            if (controller.loadGame(file)) {
                displayGame(primaryStage, myScene);
            } else {
                if (root.getChildren().size() == 3) {
                    root.getChildren().remove(2);
                }
                Label error = new Label("Error Loading File");
                error.setPadding(new Insets(5,0,80,265));
                error.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
                root.getChildren().add(error);



            }
            });

        Button quit = new Button("Quit Game");
        quit.setOnAction(event -> primaryStage.close());
        quit.setMaxWidth(150);

        menu.getChildren().addAll(save, load, quit);
        GridPane gameDisplay = new GridPane();
        root.getChildren().addAll(hBox, menu);
        hBox.getChildren().addAll(gameDisplay);
        gameDisplay.setPadding(new Insets(50,50,15,50));
        gameDisplay.setHgap(10);
        gameDisplay.setVgap(10);
        for (int i = 0; i < 12; i++) {
            gameDisplay.getColumnConstraints().add(new ColumnConstraints(40));
        }



        for (int r = 0; r < model.getGameBoard().length; r++) {
            for (int c = 0; c < model.getGameBoard().length; c++) {
                if (controller.getGameBoardValue(c, r) == 0 || controller.getGameBoardValue(c, r) == 'S') {
                    Button button = new Button("" + controller.getGameBoardValue(0, r) + c);
                    button.setMinWidth(40);
                    button.setMinHeight(25);
                    gameDisplay.add(button, c,r);
                    button.setOnAction(event -> {
                        int columnIndex = GridPane.getColumnIndex(button);
                        int rowIndex = GridPane.getRowIndex(button);
                        if (controller.isAHit(columnIndex, rowIndex)) {
                            if (root.getChildren().size() == 3) {
                                root.getChildren().remove(2);
                            }
                            if (controller.hitShip(columnIndex, rowIndex)) {
                                Label label = new Label("  HIT");
                                label.setMinHeight(25);
                                gameDisplay.getChildren().remove(button);
                                gameDisplay.add(label, columnIndex, rowIndex);


                            }

                        } else {
                            Label label = new Label("  MISS");
                            label.setMinHeight(25);
                            gameDisplay.getChildren().remove(button);
                            gameDisplay.add(label, columnIndex, rowIndex);
                            if (root.getChildren().size() == 3) {
                                root.getChildren().remove(2);
                            }
                        }

                    });
                }else if(controller.getGameBoardValue(c, r) == 'X') {
                    Label label = new Label("    10");
                    label.setMinWidth(100);
                    label.setContentDisplay(ContentDisplay.TOP);
                    gameDisplay.add(label, c,r);
                }else if(controller.getGameBoardValue(c, r) == 'Z') {
                } else if(controller.getGameBoardValue(c, r) == 'V') {
                    Label label = new Label("  HIT");
                    label.setMinHeight(25);
                    gameDisplay.add(label, c, r);
                } else {
                    Label label = new Label("     " + controller.getGameBoardValue(c, r));
                    label.setMinWidth(40);
                    gameDisplay.add(label, c,r);
                }

            }
        }


        primaryStage.setScene(myScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void displayMainMenu(Stage primaryStage, Scene myScene) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(100,0,0,250));
        VBox mainMenu = new VBox();
        mainMenu.setSpacing(10);
        mainMenu.setPadding(new Insets(30,0,0,30));
        primaryStage.setResizable(false);

        Label battleships = new Label("BATTLESHIPS");
        battleships.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 30));

        Button startD = new Button("Start Default Game");
        startD.setMaxWidth(150);
        startD.setOnAction(event -> {
            controller.startDefaultGame();
            displayGame(primaryStage, myScene);

        });

        Button startR = new Button("Start Random Game");
        startR.setMaxWidth(150);
        startR.setOnAction(event -> {
            controller.startRandomGame();
            displayGame(primaryStage, myScene);
        });

        Button load = new Button("Load Game");
        load.setMaxWidth(150);
        load.setOnAction(event -> {
            FileChooser files = new FileChooser();
            files.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Documents", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            files.setTitle("Load Game");
            File file = files.showOpenDialog(primaryStage);
             if (controller.loadGame(file)) {
                 displayGame(primaryStage, myScene);
             } else {
                 Label error = new Label("Error Loading File");
                 error.setPadding(new Insets(5,0,80,268));
                 error.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
                 root.getChildren().add(error);
             }

        });

        Button quit = new Button("Quit Game");
        quit.setMaxWidth(150);
        quit.setOnAction(event -> primaryStage.close());

        root.getChildren().add(vBox);
        vBox.getChildren().addAll(battleships, mainMenu);
        mainMenu.getChildren().addAll(startD, startR, load, quit);





        primaryStage.setScene(myScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }



    @Override
    public void update(Observable o, Object arg) {
        if (controller.isGameOver()) {

            // Removes any status messages e.g 'Battleship sunk!'
            root.getChildren().get(0).setDisable(true);
            if (root.getChildren().size() == 3) {
                root.getChildren().remove(2);
            }

            // Disables save button
            Node menuBox = root.getChildren().get(1);
            if (menuBox instanceof HBox) {
                ((HBox) menuBox).getChildren().get(0).setDisable(true);
            }

            Label gameOver = new Label("GAME OVER, YOU WIN!");
            gameOver.setPadding(new Insets(5,0,80,235));
            gameOver.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20));

            root.getChildren().add(gameOver);


        } else {
            Label shipSunk = new Label("BATTLESHIP SUNK!");
            shipSunk.setPadding(new Insets(5,0,80,250));
            shipSunk.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
            root.getChildren().add(shipSunk);
        }

    }
}
