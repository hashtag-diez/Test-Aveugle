package client.src.ihm.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.src.ihm.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class CreationPartieController implements Initializable {

    @FXML
    private Pane createGamePane;

    @FXML
    private ChoiceBox<Integer> nbToursMenu;

    @FXML
    private TextField pseudoField;

    @FXML
    private ChoiceBox<Theme> themeMenu;

    @FXML
    private TextField titrePartieFiels;

    @FXML
    private Label errorLabel;

    private static SystemTestAveugle system = SystemTestAveugle.getSystem();
    private static ArrayList<Theme> themes = system.getThemes();
    private static Integer[] nbTourVal = {10, 15, 20, 25};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        themeMenu.getItems().addAll(themes);
        themeMenu.setValue(themes.get(0));
        nbToursMenu.getItems().addAll(nbTourVal);
        nbToursMenu.setValue(10);
    }

    @FXML
    void createGame(ActionEvent event) {
        errorLabel.setText("");
        String title = titrePartieFiels.getText();
        Theme theme = themeMenu.getValue();
        String pseudo = pseudoField.getText();
        int nbTour = nbToursMenu.getValue();
        if(title.isEmpty() || pseudo.isEmpty()) {
            errorLabel.setText("Tous les champs doivent être remplis !");
            return;
        }
        if(system.checkGameExistence(title)) {
            errorLabel.setText("Titre indisponible");
        } else {
            system.createGame(title, theme, pseudo, nbTour);
        }
    }
}
