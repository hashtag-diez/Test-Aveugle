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
    private TextField responseInput;

    @FXML
    private ListView<String> responseList;

    @FXML 
    private ListView<Player> playerList;

    @FXML
    private ListView<String> scoreList;

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
            Image newImage = new Image(imageInFile);
            setImage(newImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSortedTable(currentGame.getPlayers());

        //TODO gestion du timer:
        //new Timer(c)

    }

    public void setImage(Image newImage) {
        double x = newImage.getWidth();
        double y = newImage.getHeight();
        double a = image.getFitWidth();
        double b = image.getFitHeight();
        double ratioX = a / x;
        double ratioY = b / y;
        double applicableRatio = (ratioX > ratioY) ? ratioY : ratioX;
        image.setX(image.getX() + (a - x * applicableRatio)/2);
        image.setY(image.getY() + (b - y * applicableRatio)/2);
        image.setImage(newImage);
    }

    public void setSortedTable(ArrayList<Player> players) {
        playerList.getItems().clear();
        scoreList.getItems().clear();
        //trie la liste des joueurs en fonction de leur score
        ArrayList<Player> tempList = new ArrayList<>(players);
        while(tempList.size() > 0) {
            Player highScorePlayer = tempList.get(0);
            for(Player p : tempList) {
                if(p.getPoints() > highScorePlayer.getPoints()) highScorePlayer = p;
            }
            tempList.remove(highScorePlayer);
            playerList.getItems().add(highScorePlayer);
            scoreList.getItems().add(highScorePlayer.getPoints() + " pts");
        }
    }

    public void updateAnswers() {
        Game currentGame = system.getCurrentGame();
        ArrayList<String> answers = currentGame.getAnswers();
        responseList.getItems().clear();
        responseList.getItems().addAll(answers);  
        if(answers.size() > 0 ) responseList.scrollTo(answers.size() - 1);     
    }

    public void updateScore() {
        setSortedTable(system.getCurrentGame().getPlayers());
    }

    public void updateGame() {
        //TODO
    }

    public void endGame() {
        //TODO
    }

}
