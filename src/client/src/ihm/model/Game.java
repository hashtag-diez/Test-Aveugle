package ihm.model;

import java.util.ArrayList;

public class Game {
    private String name;
    private Theme theme;
    private ArrayList<Player> players;
    private Player admin;
    private int nbTours;

    public Game(String name, Theme theme, String adminName, int tours) {
        this.name = name;
        this.theme = theme;
        this.players = new ArrayList<>();
        this.admin = new Player(adminName, true);
        this.players.add(this.admin);
        this.nbTours = tours;
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

    public void decrementTours() {
        nbTours --;
    }

    public void addPlayer(String name) {
        players.add(new Player(name, false));
    }

    public void quitGame(Player player) {
        players.remove(player);
    }

    public boolean checkPlayerExistence(String name) {
        for (Player p : players) {
            if(p.getName().equals(name)) return true;
        }
        return false;
    }
}