package src.ihm.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import src.ihm.model.*;

public class MainSceneController implements Initializable {

    @FXML
    private Button createGameButton;

    @FXML
    private ListView<String> gameList;

    @FXML
    private Pane mainPane;

    @FXML
    private BorderPane mainViewGamePanel;

    private ArrayList<Game> games;
    private String selectedGame;

    private static JoinGameController joinGameController;

    private static SystemTestAveugle system;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        system = SystemTestAveugle.getSystem();
        this.games = system.getGames();
        for(Game g : games) {
            gameList.getItems().add(g.getName());
        }
        gameList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                selectedGame = gameList.getSelectionModel().getSelectedItem();
                Game selected = system.getGameByName(selectedGame);
                if(selected != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/JoinGame.fxml"));
                        Pane createPane = (Pane) loader.load();
                        mainViewGamePanel.setCenter(createPane);
                        JoinGameController joinController = loader.getController();
                        joinController.setGame(selected);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @FXML
    public void createGameButtonPushed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/CreationPartie.fxml"));
            Pane createPane = (Pane) loader.load();
            mainViewGamePanel.setCenter(createPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateGameList() {
        Platform.runLater(() -> {
            gameList.getItems().clear();
            this.games = system.getGames();
            for(Game g : games) {
                gameList.getItems().add(g.getName());
            }
            if(joinGameController!= null) joinGameController.updateGame();
        });
        
    }
}
