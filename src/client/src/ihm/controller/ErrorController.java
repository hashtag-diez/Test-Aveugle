package src.ihm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import src.ihm.model.*;

public class ErrorController {
    private SystemTestAveugle system = SystemTestAveugle.getSystem();

    @FXML
    private Button goBackButton;

    @FXML
    private void goBackToMain(ActionEvent e) {
        system.goToMenu();
    }
}
