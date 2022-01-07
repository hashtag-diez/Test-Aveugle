package src.ihm.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import src.ihm.model.*;

public class InGameController implements Initializable{

    @FXML
    private ImageView image;

    @FXML
    private TableColumn<?, ?> playerColumn;

    @FXML
    private TextField responseInput;

    @FXML
    private ListView<String> responseList;

    @FXML
    private TableColumn<?, ?> scoreColumn;

    @FXML
    private TableView<?> scoreTable;

    @FXML
    private ProgressIndicator timer;

    @FXML
    private Label tourLabel;
    
    @FXML
    void enterAnswer(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            String response = responseInput.getText();
            if(!response.isEmpty()) {
                system.sendAnswer(responseInput.getText());
                responseInput.clear();
            }
        }
    }

    private Integer nbQuestions;
    private SystemTestAveugle system = SystemTestAveugle.getSystem();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Game currentGame = system.getCurrentGame();
        //Question currentQuestion = currentGame.getCurrentQuestion();

        nbQuestions = currentGame.getNbTours();
        tourLabel.setText((nbQuestions - currentGame.getNbTours() + 1) + "/" + nbQuestions);

        responseList.getItems().addAll(currentGame.getAnswers());        

        try {
            FileInputStream imageInFile = new FileInputStream("src/client/img/image.jpg");
            image.setImage(new Image(imageInFile));
            //image.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO gestion du timer:
        //new Timer(c)
        /*playerColumn.setCellFactory(new PropertyValueFactory<Player, String>("name"));
        scoreColumn.setCellFactory(new PropertyValueFactory<Player, Integer>("points"));
        scoreTable.setItems(currentGame.getPlayers());
        */

    }

    public void updateAnswers() {
        Game currentGame = system.getCurrentGame();
        ArrayList<String> answers = currentGame.getAnswers();
        responseList.getItems().clear();
        responseList.getItems().addAll(answers);  
        //if(answers.size() > 0 ) responseList.scrollTo(answers.size() - 1);     
    }

    public void updateGame() {
        //TODO
    }

    public void endGame() {
        //TODO
    }

}
