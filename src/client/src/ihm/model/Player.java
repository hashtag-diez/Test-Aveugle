package client.src.ihm.model;

public class Player {
    private String name;
    private int points;
    private boolean isAdmin;
    private String game;

    public Player(String name, boolean admin) {
        this.name = name;
        this.isAdmin = admin;
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints() {
        this.points ++;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String title) {
        this.game = title;
    }
}
