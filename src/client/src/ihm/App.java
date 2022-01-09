package src.ihm;

import java.io.IOException;

import src.ihm.controller.GameSceneController;
import src.ihm.controller.MainSceneController;
import src.ihm.model.SystemTestAveugle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene mainScene;
    private static MainSceneController mainSceneController;

    private static Scene gameScene;
    private static GameSceneController gameSceneController;

    private Stage stage;

    private static SystemTestAveugle system;

    @Override
    public void start(Stage stage) throws Exception {
        system.setApp(this);
        this.stage = stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainScene.fxml"));
            mainScene = new Scene(loader.load());
            mainSceneController = loader.getController();
            stage.setTitle("Test Aveugle");
            stage.getIcons().add(new Image("img/logo.png"));
            stage.setScene(mainScene);
            stage.addEventFilter(javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
                if(system.getCurrentPlayer() != null) {
                    system.deconnection();
                }
            });
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateGameList() {
        mainSceneController.updateGameList();
        if(gameSceneController != null && system.getCurrentGame() != null) gameSceneController.updateGame();
    }

    public void updateAnswers() {
        if(gameSceneController != null) {
            gameSceneController.updateAnswers();
        } 
    }

    public void updateGameInSession() {
        gameSceneController.updateGameInSession();
    }

    public void killTime() {
        if(gameSceneController != null) {
            gameSceneController.killTime();
        } 
    }

    public void goToGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/GameScene.fxml"));
            gameScene = new Scene(loader.load());
            gameSceneController = loader.getController();
            stage.setX(300);
            stage.setY(25);
            stage.setScene(gameScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToMenu() {
        stage.setScene(mainScene);
        stage.centerOnScreen();
        stage.show();
    }

    public void goToError() {
        gameSceneController.goToError();
    }

    public void startGame() {
        gameSceneController.startGame();
    }

    public void endGame() {
        gameSceneController.endGame();
    }

    public static void main(String[] args) {
        system = SystemTestAveugle.getSystem();
        system.connexion();
        Application.launch(args);
    }
}