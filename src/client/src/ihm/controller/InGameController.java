package client.src.ihm.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.stream.IIOByteBuffer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import client.src.ihm.model.*;

public class InGameController implements Initializable{

    @FXML
    private ImageView image;

    private SystemTestAveugle system = SystemTestAveugle.getSystem();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Game currentGame = system.getCurrentGame();
        //Image imageFile = new Image(getClass().getResourceAsStream("../lib/img/image.jpg"));
        //image.setImage(imageFile);
    }

}
