package src.network;

import java.util.ArrayList;

import src.ihm.model.*;

public class Network {
    private static SystemTestAveugle system = SystemTestAveugle.getSystem();
    private static ArrayList<Theme> themes = system.getThemes();
    public static ArrayList<Game> getGames() {
        //TODO résupère la liste des parties disponibles

        ArrayList<Game> games = new ArrayList<>();
        //à supprimer plus tard:
        games.add(new Game("Partie1", themes.get(0), "Michel", 25));
        games.add(new Game("Partie2", themes.get(1), "Richard", 10));
        games.add(new Game("Partie3", themes.get(2), "Agathe", 15));
        games.get(0).addPlayer("Roro");
        games.get(0).addPlayer("Roro");
        games.get(0).addPlayer("Roro");
        games.get(0).addPlayer("Roro");
        games.get(0).addPlayer("Roro");
        games.get(0).addPlayer("Roro");
        games.get(0).addPlayer("Roro");
        games.get(0).addPlayer("Roro");
        games.get(0).addPlayer("Roro");
        games.get(0).addPlayer("Roro");
        games.get(0).addPlayer("Roro");
        games.get(1).addPlayer("Riri");
        games.get(1).addPlayer("Fifi");
        games.get(1).addPlayer("Loulou");
        //
        return games;
    }

    public static void pushGame(String title, Theme theme, String adminName, int nbTours) {
        //TODO push la nouvelle partie vers le serveur

        //à supprimer: l'appel suivant simule le retour du serveur
        receiveGame(title, theme, adminName, nbTours);
    }

    public static void receiveGame(String title, Theme theme, String adminName, int nbTours) {
        //TODO cette méthode reçoit un Map<String, Map<String,String> -> à adapter

        //méthode appelée par la connexion au serveur
        system.receiveGame(title, theme, adminName, nbTours);
    }

}
