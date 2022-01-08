//Note: dans certaines méthodes, la présence du Game sert uniquement à la vérification du jeu courrant dans le front
//en théorie, les messages destinés à une partie en particulier seront receptionné uniquement par les joueurs concernés
//le Game peut donc, le cas échéant, être supprimé (faire les vérifications nécessaires)

package src.network;

import java.io.File;
import java.io.FileInputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
//import java.util.HashMap;
//import java.util.List;
import java.util.Map;


import src.ihm.model.*;

public class Network {
    private static SystemTestAveugle system = SystemTestAveugle.getSystem();
    private static ArrayList<Theme> themes = system.getThemes();

    public static void setConnexion() {
        //TODO méthode de connexion au server (lance la connexion dans un thread pour permetter l'exécution parallèle des affichages)
    }

    public static ArrayList<Game> getGames() {
        //TODO résupère la liste des parties disponibles: /!\ méthode synchrone: doit envoyer la requête et se mettre en attente jusqu'à recevoir la bonne réponse

        ArrayList<Game> games = new ArrayList<>();
        //à supprimer plus tard:
        games.add(new Game("Partie1", themes.get(0), "Michel", 20, false));
        games.add(new Game("Partie2", themes.get(1), "Richard", 10, false));
        games.add(new Game("Partie3", themes.get(2), "Agathe", 15, false));
        games.add(new Game("Partie4", themes.get(3), "Agathe", 15, false));
        games.add(new Game("Partie5", themes.get(4), "Agathe", 15, false));
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

    //public static void receiveGame(String title, Theme theme, String adminName, int nbTours) {
    public static void receiveGame(Map<String, Map<String, String>> data) {
        String title = data.get("result").get("title");

        String themeName = data.get("result").get("categorieName"); // ??
        Theme theme = system.getThemeByName(themeName);

        String adminName = data.get("result").get("adminName");
        int nbTours = Integer.parseInt(data.get("result").get("nbTours"));

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
        String serverInstant = Instant.now().plus(4, ChronoUnit.SECONDS).toString();
        Question q = new Question(base64Image, serverInstant, "test");
        gameStarted(q);
    }

    public static void gameStarted(Question question) {
        system.gameStarted(question);
    }

    public static void sendAnswer(String text, Game game, String player, boolean isLastTurn) {
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
        String serverInstant = Instant.now().plus(5, ChronoUnit.SECONDS).toString();
        Question nextQuestion = new Question(base64Image, serverInstant, "test");
        if(game.getCurrentQuestion().isGoodAnswer(text)) {
            scoreRefresh(text, game, player, nextQuestion, false, isLastTurn);
        } else {
            receiveAnswer(text, game, player);
        }
    }

    public static void sendEndOfClock(Game game, String player, boolean isLastTurn) {
        //TODO notifier le serveur de la fin de l'horloge (USER_ANSWER vide avec isClockEnd à true)

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
        String serverInstant = Instant.now().plus(2, ChronoUnit.SECONDS).toString();
        Question nextQuestion = new Question(base64Image, serverInstant, "test");
        scoreRefresh("", game, player, nextQuestion, true, isLastTurn);
    }

    public static void receiveAnswer(String text, Game game, String player) {
        //TODO receive answer

        if(game.getName().equals(system.getCurrentGame().getName())){
            system.receiveAnswer(text, player);
        }
    }

    public static void scoreRefresh(String text, Game game, String player, Question nextQuestion, boolean isClockEnd, boolean isLastTurn) {
        //TODO réception d'un score refresh (fin du tour, incrémentation du score)

        if(game.getName().equals(system.getCurrentGame().getName())){
            system.receiveCorrectAnswer(text, player, isClockEnd);
            if(!isLastTurn) {
                system.setNextQuestion(nextQuestion);
            } else {
                system.endGame();
            }
        }
    }

    public static void deconnection(Game game, String player, boolean isAdmin) {
        //TODO notifier le server de la déconnexion du joueur de son jeu ou bien de la fermeture de la page(précise s'il s'agit d'un administrateur)

        //à supprimer: simulation de la réponse serveur
        receiveDeconnection(game, player, isAdmin);
    }

    public static void receiveDeconnection(Game game, String player, boolean isAdmin) {
        //TODO réception d'un message de déconnexion, si isAdmin: la partie est supprimée, affichage page erreur
        system.receiveDeconnection(game, player, isAdmin);
    } 
}
