package src.network;

import java.util.ArrayList;

import src.ihm.model.*;

public class Network {
    private static SystemTestAveugle system = SystemTestAveugle.getSystem();
    private static ArrayList<Theme> themes = system.getThemes();

    public void setConnexion() {
        //TODO méthode de connexion au server (lance la connexion dans un thread pour permetter l'exécution parallèle des affichages)
    }

    public static ArrayList<Game> getGames() {
        //TODO résupère la liste des parties disponibles

        ArrayList<Game> games = new ArrayList<>();
        //à supprimer plus tard:
        games.add(new Game("Partie1", themes.get(0), "Michel", 25, false));
        games.add(new Game("Partie2", themes.get(1), "Richard", 10, false));
        games.add(new Game("Partie3", themes.get(2), "Agathe", 15, false));
        games.get(0).addPlayer("Roro", false);
        games.get(0).addPlayer("Roro", false);
        games.get(0).addPlayer("Roro", false);
        games.get(0).addPlayer("Roro", false);
        games.get(0).addPlayer("Roro", false);
        games.get(0).addPlayer("Roro", false);
        games.get(0).addPlayer("Roro", false);
        games.get(0).addPlayer("Roro", false);
        games.get(0).addPlayer("Roro", false);
        games.get(1).addPlayer("Riri", false);
        games.get(1).addPlayer("Fifi", false);
        games.get(1).addPlayer("Loulou", false);
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


        system.receiveGame(title, theme, adminName, nbTours);
    }

    public static void joinGame(String pseudo, Game game) {
        //TODO notifier le server que l'utilisateur à rejoint la partie Game
        // /!\ le serveur doit notifier TOUT LES UTILISATEURS

        //à supprimer: simulation du retour du back
        hasJoinedGame(pseudo, game);
    }

    public static void hasJoinedGame(String player, Game game) {
        //TODO méthode de réception d'une nouvelle connexion à une partie

        system.hasJoinedGame(player, game);
    }

}
