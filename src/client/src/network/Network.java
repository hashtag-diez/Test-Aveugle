//Note: dans certaines méthodes, la présence du Game sert uniquement à la vérification du jeu courrant dans le front
//en théorie, les messages destinés à une partie en particulier seront receptionné uniquement par les joueurs concernés
//le Game peut donc, le cas échéant, être supprimé (faire les vérifications nécessaires)

package src.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import src.ihm.model.*;

public class Network {
    private static SystemTestAveugle system = SystemTestAveugle.getSystem();
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
        if(data.get("header").get("status").equals("OK")){
            data.remove("header");
            System.out.println("On mappe les games");
            for(Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
                Map<String,String> values = entry.getValue();
                String name = entry.getKey();
                String theme = values.get("categorie");
                String adminName = values.get("admin");
                int tours = Integer.parseInt(values.get("nbTours"));
                Game game = new Game(name, system.getThemeByName(theme), adminName, tours, false);
                String[] players = values.get("users").split(",");
                for(String playerName : players) {
                    if(!playerName.isEmpty() && !playerName.equals(adminName)){
                        Player p = new Player(playerName, playerName.equals(adminName));
                        p.setLocal(false);
                        p.setGame(name);
                        game.addPlayer(p);
                    }
                }
                games.add(game);
            }
        }
        System.out.println("On a fini !");
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

    public static void startGame(Game game) {
        String request = "CHANNEL_START";
        request += " " + game.getName();
        request += " " + game.getTheme().getName();
        try {
            UserConnection.sendRequest(request);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void gameStarted(Map<String, Map<String, String>> data) {
        Map<String, String> results = data.get("results");
        String reponse = results.get("response");
        String image = results.get("image");
        String game = results.get("channelName");
        String startTime = results.get("startTime");
        Question question = new Question(image, startTime, reponse);
        System.out.println("GameStarted");
        system.gameStarted(question, game);
    }

    public static void sendAnswer(String text, Game game, String player, boolean isLastTurn) {
        String line = "USER_ANSWER "+ game.getCurrentQuestion().getResponse() + " " + player  + " " + text;
        try {
            UserConnection.sendRequest(line);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void receiveAnswer(Map<String, Map<String, String>> data) {
        String text = data.get("result").get("userAnswer");
        String player = data.get("result").get("pseudo");
        String gameName = data.get("result").get("gameName"); 
        String isCorrect = data.get("result").get("trueOrFalse");
        Game game = system.getGameByName(gameName);

        if (game.getName().equals(system.getCurrentGame().getName())) {
            system.receiveAnswer(text, player);
            if(isCorrect.equals("true") && system.getCurrentPlayer().isAdmin()){
                sendScoreRefresh(gameName, system.getCurrentPlayer().getName(), game.getTheme().getName());
            }
        }
    }

    public static void sendScoreRefresh(String channelName, String pseudo, String categString) {
        String request = "SCORE_REFRESH";
        request += " " + channelName;
        request += " " + pseudo;
        request += " " + categString;
        try {
            UserConnection.sendRequest(request);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void scoreRefresh(Map<String,Map<String,String>> data) {
        Map<String,String> res = data.get("result");
        String reponse = res.get("response");
        String image = res.get("image");
        String game = res.get("channelName");
        String startTime = res.get("startTime");
        String player = res.get("winnerUser");
        if(player.equals("none")) {
            system.receiveCorrectAnswer(reponse, null, true);
        } else {
            if (game.equals(system.getCurrentGame().getName())) {
                system.receiveCorrectAnswer(reponse, player, false);
            }
        }
        Question question = new Question(image, startTime, reponse);
        if (!system.isLastTurn()) {
            system.setNextQuestion(question);
        } else {
            system.endGame();
        }
    }

    public static void deconnection(Game game, String player, boolean isAdmin) {
        String line = "USER_DISCONNECT "+ game.getName() + " " + player;
        try {
            UserConnection.sendRequest(line);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void receiveDeconnection(Map<String, Map<String, String>> data) {
        // réception d'un message de déconnexion, si isAdmin: la partie est supprimée, affichage page erreur
        String gameName = data.get("result").get("channelName"); 
        Game game = system.getGameByName(gameName);

        String player = data.get("result").get("disconnectedUser");

        Boolean isAdmin = Boolean.parseBoolean(data.get("result").get("isAdmin"));

        system.receiveDeconnection(game, player, isAdmin);
    }

    public static void receiveDeletion(Map<String, Map<String, String>> data){
        system.deleteGame(data.get("result").get("channelName"));
    }
}
