//Note: dans certaines méthodes, la présence du Game sert uniquement à la vérification du jeu courrant dans le front
//en théorie, les messages destinés à une partie en particulier seront receptionné uniquement par les joueurs concernés
//le Game peut donc, le cas échéant, être supprimé (faire les vérifications nécessaires)

package src.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
//import java.util.HashMap;
//import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import src.ihm.model.*;

public class Network {
    private static SystemTestAveugle system = SystemTestAveugle.getSystem();
    private static ArrayList<Theme> themes = system.getThemes();
    private static boolean hasReceivedGames = false;
    private static ArrayList<Game> games;

    public static void setConnexion() { //OK
        Thread networkThread = new Thread() {
            public void run() {
                try {
                    new UserConnection("").run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        networkThread.start();
    }

    public static ArrayList<Game> getGames() {
        while(!hasReceivedGames) {}
        return games;
    }

    public static void receiveGameList(Map<String, Map<String, String>> data) {
        games = new ArrayList<>();
        for(Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
            Map<String,String> values = entry.getValue();
            String name = entry.getKey();
            String theme = values.get("categorie");
            String adminName = values.get("admin");
            int tours = Integer.parseInt(values.get("nbTours"));
            Game game = new Game(name, system.getThemeByName(theme), adminName, tours, false);
            String[] players = values.get("users").split(",");
            for(String playerName : players) {
                if(!playerName.isEmpty()){
                    Player p = new Player(playerName, playerName.equals(adminName));
                    p.setLocal(false);
                    p.setGame(name);
                    game.addPlayer(p);
                }
            }
            games.add(game);
        }
        hasReceivedGames = true;
    }

    public static void pushGame(String title, Theme theme, String adminName, int nbTours) {
        String request = "CHANNEL_CREATE";
        request += " " + title;
        request += " " + adminName;
        request += " " + theme.getName();
        request += " " + nbTours;
        try {
            UserConnection.sendRequest(request);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void receiveGame(Map<String, Map<String, String>> data) {
        String title = data.get("result").get("channelName");
        String themeName = data.get("result").get("categorieName"); 
        Theme theme = system.getThemeByName(themeName);
        String adminName = data.get("result").get("adminName");
        int nbTours = Integer.parseInt(data.get("result").get("nbTours"));

        system.receiveGame(title, theme, adminName, nbTours);
    }

    public static void joinGame(String pseudo, Game game) {
        String request = "USER_CONNECT";
        request += " " + game.getName();
        request += " " + pseudo;
        try {
            UserConnection.sendRequest(request);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void hasJoinedGame(Map<String, Map<String, String>> data) {
        String player = data.get("result").get("userName");
        String gameName = data.get("result").get("channelName"); 
        Game game = system.getGameByName(gameName);
        
        system.hasJoinedGame(player, game);
    }

    public static void startGame(Map<String, Map<String, String>> data) {
        //TODO méthode de notification de jeu lancé par l'administrateur
        
        /*
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

 //theme
 
*/
        String title = data.get("result").get("title");

        String themeName = data.get("result").get("categorieName"); // ??
        Theme theme = system.getThemeByName(themeName);

       
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

    public static void receiveAnswer(Map<String, Map<String, String>> data) {
        String text = data.get("result").get("userAnswer");
        String player = data.get("result").get("pseudo");

        String gameName = data.get("result").get("gameName"); //??
        Game game = system.getGameByName(gameName);

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

    public static void receiveDeconnection(Map<String, Map<String, String>> data) {
        // réception d'un message de déconnexion, si isAdmin: la partie est supprimée, affichage page erreur
        String gameName = data.get("result").get("channelName"); 
        Game game = system.getGameByName(gameName);

        String player = data.get("result").get("disconnectedUser");

        Boolean isAdmin = Boolean.parseBoolean(data.get("result").get("isAdmin"));

        system.receiveDeconnection(game, player, isAdmin);
    } 
}
