package client.src.ihm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import client.src.ihm.model.*;

public class WaitingRoomController {

    private SystemTestAveugle system = SystemTestAveugle.getSystem();

    @FXML
    private Button waitingActionButton;

    @FXML
    void waitingAction(ActionEvent event) {
        system.startGame();
    }

}