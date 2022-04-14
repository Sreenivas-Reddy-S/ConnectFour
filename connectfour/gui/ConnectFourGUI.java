package connectfour.gui;

import connectfour.model.ConnectFourBoard;
import connectfour.model.Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Objects;


/**
 * A simple GUI to play Connect Four. Each turn alternates with the current player dropping a disc of their color into a column of the board.
 * The winner is the first player to connect four of their discs together horizontally, vertically or in diagonal.
 * This class is both the view and controller,
 * according to the MVC architectural pattern.
 *
 */

public class ConnectFourGUI extends Application implements Observer<ConnectFourBoard> {

    /**
     * creating Model object i.e object of class ConnectFourBoard
     */
    private ConnectFourBoard model = new ConnectFourBoard();

    /**
     * creating a new gridpane
     */
    private GridPane gridPane;


    /**
     * Initializing disc images
     */
    private final Image player1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("p1black.png")));
    private final Image player2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("p2red.png")));
    private final Image empty = new Image(Objects.requireNonNull(getClass().getResourceAsStream("empty.png")));


    /**
     * Creating a new labels
     */
    private Label label1;
    private Label label2;
    private Label label3;


    /**
     * This method creates the model and add ourselves as an observer
     */
    public void init() {
        this.model = new ConnectFourBoard();
        model.addObserver(this);
    }


    /**
     * The start() method really begins a JavaFX application defining the GUI.
     * The code creates the scene to populate the application's stage with borderpane, hbox and labels
     *
     * @param stage container in which the UI will be rendered
     */
    @Override
    public void start(Stage stage) {


        // it will instantiate the border pane
        BorderPane borderPane = new BorderPane();

        // it will instantiate the hbox
        HBox hbox = new HBox();

        makeGridPane();

        borderPane.setTop(gridPane);
        label1 = new Label();
        label2 = new Label();
        label3 = new Label();
        label1.setText("Moves Made:" + model.getMovesMade());
        label1.setFont(new Font("Monotype Corsiva", 15));

        label2.setText("Player:" + model.getCurrentPlayer());
        label2.setFont(new Font("Monotype Corsiva", 15));

        label3.setText("Status:" + model.getGameStatus());
        label3.setFont(new Font("Monotype Corsiva", 15));

        // Adding labels to the hbox
        hbox.getChildren().add(label1);
        hbox.getChildren().add(label2);
        hbox.getChildren().add(label3);
        hbox.setSpacing(140);

        borderPane.setBottom(hbox);

        // It will instantiate and passes the border pane into the scene
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setTitle("ConnectFourGUI");
        stage.setWidth(650);

        // it will not allow the stage to resize
        stage.setResizable(false);

        // it will display the stage
        stage.show();

    }


    /**
     * method to create a gridpane and its children such as buttons
     */
    private void makeGridPane() {

        // gap between items in grid pane
        int gap = 10;

        gridPane = new GridPane();
        gridPane.setVgap(gap);
        gridPane.setHgap(gap);

        //Loop for the controller to execute the move and to populate the grid
        for (int row = 0; row < ConnectFourBoard.ROWS; row++) {
            for (int column = 0; column < ConnectFourBoard.COLS; column++) {
                Button button = new Button();
                ImageView view = new ImageView(empty);
                button.setGraphic(view);
                int temp_col = column;
                gridPane.add(button, column, row);

                // lambda function for the controller to perform the action
                button.setOnAction(actionEvent -> {
                    if (model.getGameStatus() == ConnectFourBoard.Status.NOT_OVER && model.isValidMove(temp_col)) {
                        model.makeMove(temp_col);
                    }
                });
            }
        }

    }


    /**
     * The GUI components, the buttons and labels, updates in this method by getting
     * the current value from the model.
     *
     * @param model the model
     */
    private void refresh(ConnectFourBoard model) {
        ImageView view;

        //Loop for the player to place the disc
        for (int row = 0; row < ConnectFourBoard.ROWS; row++) {
            for (int column = 0; column < ConnectFourBoard.COLS; column++) {
                Button button = (Button) gridPane.getChildren().get(row * ConnectFourBoard.COLS + column);

                if (model.getContents(row, column) == ConnectFourBoard.Player.P1) {
                    view = new ImageView(player1);
                    button.setGraphic(view);
                } else if (model.getContents(row, column) == ConnectFourBoard.Player.P2) {
                    view = new ImageView(player2);
                    button.setGraphic(view);
                }

            }
        }

        // The label is to update the moves of the game
        label1.setText("Moves Made:" + model.getMovesMade());

        // The label is to update the player of the game
        label2.setText("Player:" + model.getCurrentPlayer());

        // The label is to update the status of the game
        label3.setText("Status:" + model.getGameStatus());

    }

    /**
     * This method updates the content.
     *
     * @param model : the model
     */
    @Override
    public void update(ConnectFourBoard model) {
        if (Platform.isFxApplicationThread()) {
            this.refresh(model);
        } else {
            Platform.runLater(() -> this.refresh(model));
        }
    }


    /**
     * The main runs the JavaFX application.
     *
     * @param args ignored
     * @see Application#launch
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}