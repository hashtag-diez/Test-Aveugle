package ihm.model;

import java.util.ArrayList;

import ihm.App;
import network.Network;

public class SystemTestAveugle {
    private ArrayList<Game> games;
    private ArrayList<Theme> themes;

    private App app;

    private static SystemTestAveugle system = new SystemTestAveugle();

    private SystemTestAveugle() {
        games = null;
        themes = setThemeList();
    }

    public void setApp(App app) {
        this.app = app;
    }

    public static SystemTestAveugle getSystem() {
        return system;
    }

    public ArrayList<Game> getGames() {
        if(games == null) {
            games = Network.getGames();
        }
        return games;
    }

    public Game getGameByName(String name) {
        for(Game g : games) {
            if(g.getName().equals(name)) return g;
        }
        return null;
    }

    public void createGame(String title, Theme theme, String adminName, int nbTours){
        Network.pushGame(title, theme, adminName, nbTours);
    }

    public void receiveGame(String title, Theme theme, String adminName, int nbTours) {
        games.add(new Game(title, theme, adminName, nbTours));
        app.updateGameList();
    }

    public ArrayList<Theme> getThemes() {
        return themes;
    }

    public ArrayList<Theme> setThemeList() {
        ArrayList<Theme> res = new ArrayList<>();
        res.add(new Theme("Personnages", "#6df0ea"));
        res.add(new Theme("Films", "#f34646"));
        res.add(new Theme("Autres", "#6df073"));
        return res;
    }
}
