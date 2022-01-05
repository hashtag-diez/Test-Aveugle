package ihm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainSceneController {

    @FXML
    private TextField usernameTextField;

    @FXML
    void btnJoinClicked(ActionEvent event) {
        Stage mainWindow = (Stage) usernameTextField.getScene().getWindow();
        String title = usernameTextField.getText();
        mainWindow.setTitle("Welcome " + title + " ! ");
    }

}
