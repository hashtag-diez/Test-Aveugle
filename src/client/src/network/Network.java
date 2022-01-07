package src.network;

import java.io.File;
import java.io.FileInputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

import src.ihm.model.*;

public class Network {
    private static SystemTestAveugle system = SystemTestAveugle.getSystem();
    private static ArrayList<Theme> themes = system.getThemes();

    public static void setConnexion() {
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
        // /!\ le serveur doit notifier TOUS LES UTILISATEURS

        //à supprimer: simulation du retour du back
        hasJoinedGame(pseudo, game);
    }

    public static void hasJoinedGame(String player, Game game) {
        //TODO méthode de réception d'une nouvelle connexion à une partie

        system.hasJoinedGame(player, game);
    }

    public static void startGame() {
        //TODO méthode de notification de jeu lancé par l'administrateur

        //à supprimer: simulation du retour back
        String base64Image = "";
		File file = new File("src/client/img/logo.png");
		try (FileInputStream imageInFile = new FileInputStream(file)) {
			// Reading a Image file from file system
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);
			base64Image = Base64.getEncoder().encodeToString(imageData);
		} catch (Exception e) {
			System.out.println(e);
		}
        String serverInstant = Instant.now().plus(12, ChronoUnit.SECONDS).toString();
        Question q = new Question(base64Image, serverInstant, "test");
        gameStarted(q);
    }

    public static void gameStarted(Question question) {
        system.gameStarted(question);
    }

    public static void sendAnswer(String text, Game game, String player) {
        //TODO send answer to server in game, then send back to all players

        //à supprimer: simulation du retour back
        String base64Image = "";
		File file = new File("src/client/img/logo_accueil.png");
		try (FileInputStream imageInFile = new FileInputStream(file)) {
			// Reading a Image file from file system
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);
			base64Image = Base64.getEncoder().encodeToString(imageData);
		} catch (Exception e) {
			System.out.println(e);
		}
        String serverInstant = Instant.now().plus(12, ChronoUnit.SECONDS).toString();
        Question nextQuestion = new Question(base64Image, serverInstant, "test");
        if(game.getCurrentQuestion().isGoodAnswer(text)) scoreRefresh(text, game, player, nextQuestion, false);
        else receiveAnswer(text, game, player);
    }

    public static void receiveAnswer(String text, Game game, String player) {
        //TODO receive answer

        if(game.getName().equals(system.getCurrentGame().getName())){
            system.receiveAnswer(text, player);
        }
    }

    public static void scoreRefresh(String text, Game game, String player, Question nextQuestion, boolean isClockEnd) {
        //TODO réception d'un score refresh (fin du tour, incrémentation du score)

        if(game.getName().equals(system.getCurrentGame().getName())){
            system.receiveCorrectAnswer(text, player, isClockEnd);
            system.setNextQuestion(nextQuestion);
        }
    }

    public static void endGame(String turnWinnerName) {
        //TODO end of game
    }
}
