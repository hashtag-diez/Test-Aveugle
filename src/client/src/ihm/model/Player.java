package src.ihm.model;

public class Player {
    private String name;
    private int points;
    private boolean isAdmin;
    private String game;
    private boolean isLocal;

    public Player(String name, boolean admin) {
        this.name = name;
        this.isAdmin = admin;
        this.points = 0;
        this.isLocal = false;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void setLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void addPoints() {
        this.points += 10;
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

    public String toString() {
        return isLocal ? isAdmin ? name + "(moi : admin)" : name + "(moi)" : isAdmin ? name + "(admin)" : name;
    }
}
