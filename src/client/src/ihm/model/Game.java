package src.ihm.model;

import java.util.ArrayList;

public class Game {
    private String name;
    private Theme theme;
    private ArrayList<Player> players;
    private Player admin;
    private int nbTours;
    private boolean isStarted;
    private Question currentQuestion;
    private ArrayList<String> answers;

    public Game(String name, Theme theme, String adminName, int tours, boolean isLocalPlayer) {
        this.name = name;
        this.theme = theme;
        this.players = new ArrayList<>();
        this.admin = new Player(adminName, true);
        this.admin.setLocal(isLocalPlayer);
        this.players.add(this.admin);
        this.nbTours = tours;
        this.isStarted = false;
        this.currentQuestion = null;
        this.answers = new ArrayList<>();
    }

    public Player getAdmin() {
        return admin;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Theme getTheme() {
        return theme;
    }

    public int getNbTours() {
        return nbTours;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void addAnswer(String text) {
        answers.add(text);
    }

    public void decrementTours() {
        nbTours --;
    }
    public void addPlayer(String name, boolean isLocalPlayer) {
        Player player = new Player(name, false);
        player.setLocal(isLocalPlayer);
        players.add(player);
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void quitGame(Player player) {
        players.remove(player);
    }

    public void remove(String player) {
        Player tmp = null;
        for(Player p : players) {
            if(p.getName().equals(player)) {
                tmp = p;
            }
        }
        if(tmp != null) players.remove(tmp);
    }

    public boolean checkPlayerExistence(String name) {
        for (Player p : players) {
            if(p.getName().equals(name)) return true;
        }
        return false;
    }

    public boolean isLastTurn() {
        return nbTours <= 1;
    }
}