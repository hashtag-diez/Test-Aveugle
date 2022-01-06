package ihm.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.naming.InitialContext;

import ihm.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
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

    private static SystemTestAveugle system = SystemTestAveugle.getSystem();
    private static ArrayList<Theme> themes = system.getThemes();
    private static Integer[] nbTourVal = {10, 15, 20, 25};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        themeMenu.getItems().addAll(themes);
        nbToursMenu.getItems().addAll(nbTourVal);
        nbToursMenu.setValue(10);
    }

    @FXML
    void createGame(ActionEvent event) {
        //TODO ajouter les vérifications
        system.createGame(titrePartieFiels.getText(), themeMenu.getValue(), pseudoField.getText(), nbToursMenu.getValue());
    }
}
