package src.ihm.controller;

import java.net.URL;
import java.util.ResourceBundle;

//import javax.imageio.stream.IIOByteBuffer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
//import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import src.ihm.model.*;

public class InGameController implements Initializable{

    @FXML
    private ImageView image;

    //private SystemTestAveugle system = SystemTestAveugle.getSystem();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
        /*Game currentGame = system.getCurrentGame();
        Image imageFile = new Image(getClass().getResourceAsStream("src/ihm/lib/img/logo.jpg"));
        image.setImage(imageFile);*/
    }

    public void updateGame() {
        //TODO
    }

}
