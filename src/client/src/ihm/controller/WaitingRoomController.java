package src.ihm.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import src.ihm.model.*;

public class WaitingRoomController implements Initializable {

    private SystemTestAveugle system = SystemTestAveugle.getSystem();
    @FXML
    private Label messageLabel;

    @FXML
    private Label nbPlayerLabel;

    @FXML
    private Label nbTourLabel;

    @FXML
    private ListView<Player> playerList;

    @FXML
    private Button waitingActionButton;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Game game = system.getCurrentGame();
        Player player = system.getCurrentPlayer();

        ArrayList<Player> players = game.getPlayers();
        if(player.isAdmin()) {
            waitingActionButton.setText("Lancer la partie");
            waitingActionButton.setOnAction( e -> {
                system.startGame();
            });
            if(players.size() <= 1) {
                messageLabel.setText("En attente d'autres joueurs !");
                waitingActionButton.setDisable(true);
            } else {
                messageLabel.setText("Prêt à démarrer !");
                waitingActionButton.setDisable(false);
            }
        } else {
            waitingActionButton.setText("Quitter");
            waitingActionButton.setOnAction( e -> {
                system.deconnection();
            });
            messageLabel.setText("En attente de lancement par l'administrateur ...");
        }

        nbPlayerLabel.setText(players.size() + "/10");
        nbTourLabel.setText(game.getNbTours() + "");
        playerList.getItems().addAll(players);
    }

    public void updatePlayerList() {
        Game game = system.getCurrentGame();
        ArrayList<Player> players = game.getPlayers();
        Player player = system.getCurrentPlayer();
        playerList.getItems().clear();
        nbPlayerLabel.setText(players.size() + "/10");
        if(player.isAdmin()) {
            if(players.size() <= 1) {
                messageLabel.setText("En attente d'autres joueurs !");
                waitingActionButton.setDisable(true);
            } else {
                messageLabel.setText("Prêt à démarrer !");
                waitingActionButton.setDisable(false);
            }
        }
    }
}