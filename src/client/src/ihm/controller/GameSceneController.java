package src.ihm.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import src.ihm.model.*;

public class GameSceneController implements Initializable {

    private static SystemTestAveugle system = SystemTestAveugle.getSystem();
    private static Game game;

    private static WaitingRoomController waitingRoomController;
    private static InGameController inGameController;

    @FXML
    private Label gameNameLabel;

    @FXML
    private BorderPane gamePane;

    @FXML
    private Label themeLabel;

    @FXML
    private Rectangle backgroundRectangle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game = system.getCurrentGame();
        gameNameLabel.setText(game.getName());
        themeLabel.setText("Theme : " + game.getTheme().getName());
        String themeColor = game.getTheme().getColor();
        int r = Integer.valueOf( themeColor.substring( 1, 3 ), 16 );
        int g = Integer.valueOf( themeColor.substring( 3, 5 ), 16 );
        int b = Integer.parseInt( themeColor.substring( 5, 7 ), 16 );
        Color color = Color.rgb(r,g,b);
        backgroundRectangle.setFill(color);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/WaitingRoom.fxml"));
            Pane createPane = (Pane) loader.load();
            waitingRoomController = loader.getController();
            gamePane.setCenter(createPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/InGameView.fxml"));
                    Pane createPane = (Pane) loader.load();
                    inGameController = loader.getController();
                gamePane.setCenter(createPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    public void goToError() {
        try {
            inGameController.killTime();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ErrorScene.fxml"));
            Pane createPane = (Pane) loader.load();
            gamePane.setCenter(createPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateGame() {
        waitingRoomController.updatePlayerList();
        if(inGameController != null) {
            inGameController.updateGame();
        }
    }

    public void killTime() {
        if(inGameController != null) {
            inGameController.killTime();
        }
    }

    public void endGame() {
            Platform.runLater(() -> {
                try {
                inGameController.endGame();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/EndGameView.fxml"));
                Pane createPane = (Pane) loader.load();
                gamePane.setCenter(createPane);
                system.deleteCurrentGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        
    }
    
    public void updateAnswers() {
        if(inGameController != null) inGameController.updateAnswers();
    }

    public void updateGameInSession() {
        Platform.runLater(() -> {
            inGameController.updateScore();
            inGameController.updateGame();
        });
    }

}
