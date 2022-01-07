package ihm.controller;
import java.util.ArrayList;

import ihm.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class JoinGameController {

    @FXML
    private Label nbJoueur;

    @FXML
    private Label nbTourLabel;

    @FXML
    private Rectangle themeBackground;

    @FXML
    private Label themeLabel;

    @FXML
    private Label titleLabel;
    
    @FXML
    private ListView<String> playerList;

    @FXML
    private Button joinGameButton;

    @FXML
    void joinGameClicked(ActionEvent event) {

    }

    public void setGame(Game game) {
        ArrayList<Player> players = game.getPlayers();
        Theme theme = game.getTheme();
        String themeColor = theme.getColor();

        titleLabel.setText(game.getName());
        nbJoueur.setText(players.size() + "/10");
        nbTourLabel.setText(game.getNbTours() + "");

        int r = Integer.valueOf( themeColor.substring( 1, 3 ), 16 );
        int g = Integer.valueOf( themeColor.substring( 3, 5 ), 16 );
        int b = Integer.parseInt( themeColor.substring( 5, 7 ), 16 );
        Color color = Color.rgb(r,g,b);
        themeBackground.setFill(color);

        themeLabel.setText(theme.getName());

        for(Player p : players) {
            if(p.isAdmin()) {
                playerList.getItems().add(p.getName() + "(admin)");
            } else {
                playerList.getItems().add(p.getName());
            }
        }

        if(players.size() >= 10) {
            joinGameButton.setDisable(true);
            joinGameButton.setText("Partie complète!");
        } else {
            joinGameButton.setDisable(false);
            joinGameButton.setText("Rejoindre");
        }
    }

}
