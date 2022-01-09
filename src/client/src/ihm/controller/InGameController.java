package src.ihm.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
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
    private Label timeLabel;

    @FXML
    private ProgressBar timer;

    @FXML
    private Label tourLabel;

    @FXML
    private Label answerLabel;
    
    @FXML
    public synchronized void enterAnswer(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            String response = responseInput.getText();
            if(!response.isEmpty()) {
                system.sendAnswer(responseInput.getText());
                responseInput.clear();
            }
        }
    }

    private Integer nbQuestions;
    private double imageX;
    private double imageY;
    private double a;
    private double b;
    private volatile boolean killTime;
    private SystemTestAveugle system = SystemTestAveugle.getSystem();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Game currentGame = system.getCurrentGame();
        Question currentQuestion = currentGame.getCurrentQuestion();
        imageX = image.getX();
        imageY = image.getY();
        a = image.getFitWidth();
        b = image.getFitHeight();

        nbQuestions = currentGame.getNbTours();
        tourLabel.setText((nbQuestions - currentGame.getNbTours() + 1) + "/" + nbQuestions);

        answerLabel.setVisible(false);
        image.setVisible(false);

        responseList.getItems().addAll(currentGame.getAnswers());        

        try {
            FileInputStream imageInFile = new FileInputStream("src/client/img/image.jpg");
            Image newImage = new Image(imageInFile);
            setImage(newImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSortedTable(currentGame.getPlayers());

        setTimer(currentQuestion.getStartingDate(), currentQuestion.getResponse());
    }

    public void setImage(Image newImage) {
        double x = newImage.getWidth();
        double y = newImage.getHeight();
        double ratioX = a / x;
        double ratioY = b / y;
        double applicableRatio = (ratioX > ratioY) ? ratioY : ratioX;
        image.setX(imageX + (a - x * applicableRatio)/2);
        image.setY(imageY + (b - y * applicableRatio)/2);
        image.setImage(newImage);
    }

    public void setSortedTable(ArrayList<Player> players) {
        Platform.runLater(new Runnable() {
            public void run() {
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
        });
    }

    public void setTimer(String startTime, String reponse) {
        Instant thisTime = Instant.now();
        Instant serverParsedInstant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(startTime));
        Duration d = Duration.between(thisTime, serverParsedInstant);
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        Runnable countdown = () -> {
            killTime = false;
            int timeLeft = 30;
            final int actualTimeLeft = timeLeft;
            Platform.runLater(new Runnable() {
                public void run() {
                    if (!killTime) {
                        timeLabel.setText(actualTimeLeft + "s"); 
                        responseInput.setDisable(false);
                        image.setVisible(true);
                        answerLabel.setText("La réponse est: \n" + reponse);
                        answerLabel.setVisible(false);
                    }
                }
            });
            while(!killTime && timeLeft > 0){
                try {
                    if(!killTime) TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    timeLeft--;
                    final int nowTimeLeft = timeLeft;
                    Platform.runLater(new Runnable() {
                        public void run() {
                            if(!killTime && nowTimeLeft >= 0){
                                showTime(nowTimeLeft);
                            }
                        }
                    });
                }
            }
            if(!killTime){ 
                system.sendEndOfClock();
            }
        };
        resetShowTime();
        ses.schedule(countdown, d.toSeconds() , TimeUnit.SECONDS);
    }

    public synchronized void killTime() {
        killTime = true;
        resetShowTime();
    }

    public synchronized void showTime(int timeLeft) {
        timeLabel.setText(timeLeft + "s");
        timer.setProgress((double)(30 - timeLeft) / 30);
    }

    public void resetShowTime() {
        Platform.runLater(new Runnable(){
            public void run() {
                timer.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                timeLabel.setText("...");
            }
        });
    }

    public void updateAnswers() {
        Platform.runLater(new Runnable(){
            public void run() {
                Game currentGame = system.getCurrentGame();
                ArrayList<String> answers = currentGame.getAnswers();
                responseList.getItems().clear();
                responseList.getItems().addAll(answers);  
                if(answers.size() > 0 ) responseList.scrollTo(answers.size() - 1);  
            }
        });
    }

    public synchronized void endGame() {
        killTime();
        image.setVisible(false);
        answerLabel.setVisible(true);
    }

    public void updateScore() {
        setSortedTable(system.getCurrentGame().getPlayers());
    }

    public void updateGame() {
        Game currentGame = system.getCurrentGame();
        Question currentQuestion = currentGame.getCurrentQuestion();
        Platform.runLater(new Runnable(){
            public void run() {
                image.setVisible(false);
                answerLabel.setVisible(true);
                tourLabel.setText((nbQuestions - currentGame.getNbTours() + 1) + "/" + nbQuestions);
                responseInput.setDisable(true);
                try {
                    FileInputStream imageInFile = new FileInputStream("src/client/img/image.jpg");
                    Image newImage = new Image(imageInFile);
                    setImage(newImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        
                setSortedTable(currentGame.getPlayers());
        
                setTimer(currentQuestion.getStartingDate(), currentQuestion.getResponse());
            }
        });
       
    }
}
