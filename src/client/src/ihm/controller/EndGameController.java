package src.ihm.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import src.ihm.model.*;

public class EndGameController implements Initializable {

    @FXML
    private ListView<String> playerList;

    @FXML
    private Button returnButton;

    @FXML
    private ListView<String> scoreList;

    @FXML
    private Label winnerLabel;

    private SystemTestAveugle system = SystemTestAveugle.getSystem();

    @FXML
    void goBackToMenu(ActionEvent event) {
        system.goToMenu();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Player> players = system.getCurrentGame().getPlayers();
        setResults(players);
    }

    public void setResults(ArrayList<Player> players) {
        playerList.getItems().clear();
        scoreList.getItems().clear();
        int i = 1;
        //trie la liste des joueurs en fonction de leur score
        ArrayList<Player> tempList = new ArrayList<>(players);
        while(tempList.size() > 0) {
            Player highScorePlayer = tempList.get(0);
            for(Player p : tempList) {
                if(p.getPoints() > highScorePlayer.getPoints()) highScorePlayer = p;
            }
            tempList.remove(highScorePlayer);
            if(i == 1) {
                winnerLabel.setText(highScorePlayer.getName());
            }
            playerList.getItems().add(i + " : " + highScorePlayer.toString());
            scoreList.getItems().add(highScorePlayer.getPoints() + " pts");
            i++;
        }
    }


}
