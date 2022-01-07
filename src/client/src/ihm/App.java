package src.ihm;

import java.io.IOException;

import src.ihm.controller.GameSceneController;
import src.ihm.controller.MainSceneController;
import src.ihm.model.SystemTestAveugle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
//import javafx.scene.image.Image;
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
            //stage.getIcons().add(new Image("src/client/lib/img/logo.png"));
            stage.setScene(mainScene);
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateGameList() {
        mainSceneController.updateGameList();
        if(gameSceneController != null) gameSceneController.updateGame();
    }

    public void goToGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/GameScene.fxml"));
            gameScene = new Scene(loader.load());
            gameSceneController = loader.getController();

            stage.setScene(gameScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        gameSceneController.startGame();
    }

    public static void main(String[] args) {
        system = SystemTestAveugle.getSystem();
        Application.launch(args);
    }
}