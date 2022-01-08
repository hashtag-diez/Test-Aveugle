package src.ihm.model;

import java.util.ArrayList;

import src.ihm.App;
import src.network.Network;

public class SystemTestAveugle {
    private ArrayList<Game> games;
    private ArrayList<Theme> themes;

    private App app;
    public Player currentPlayer;
    public Game currentGame;

    private static SystemTestAveugle system = new SystemTestAveugle();

    private SystemTestAveugle() {
        games = null;
        themes = setThemeList();
        currentPlayer = null;
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

    public boolean checkGameExistence(String title) {
        for(Game g : games) {
            if(g.getName().equals(title)) return true;
        }
        return false;
    }

    public void createGame(String title, Theme theme, String adminName, int nbTours){
        currentPlayer = new Player(adminName, true);
        currentPlayer.setGame(title);
        Network.pushGame(title, theme, adminName, nbTours);
    }

    public void receiveGame(String title, Theme theme, String adminName, int nbTours) {
        Game newGame = new Game(title, theme, adminName, nbTours, false);
        if(currentPlayer != null && currentPlayer.getGame().equals(title)){
            newGame = new Game(title, theme, adminName, nbTours, true);
            //à supprimer: pour test du lancement
            newGame.addPlayer("Annie", false);
            //
            games.add(newGame);
            app.updateGameList();
            currentGame = newGame;
            app.goToGame();
        } else {
            games.add(newGame);
            app.updateGameList();
        }
    }

    public Theme getThemeByName(String name) {
        for(Theme t : themes) {
            if(t.getName().equals(name)) return t;
        }
        return null;
    }

    public ArrayList<Theme> getThemes() {
        return themes;
    }

    public ArrayList<Theme> setThemeList() {
        ArrayList<Theme> res = new ArrayList<>();
        res.add(new Theme("Acteurs", "#6df0ea"));
        res.add(new Theme("Films", "#f34646"));
        res.add(new Theme("Drapeaux", "#6df073"));
        res.add(new Theme("Chanteurs", "#ffc300"));
        res.add(new Theme("Voitures", "#504848"));
        return res;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void startGame() {
        Network.startGame();
    }

    public void gameStarted(Question question) {
        currentGame.setStarted(true);
        currentGame.setCurrentQuestion(question);
        app.startGame();
    }

    public void setNextQuestion(Question question) {
        currentGame.setCurrentQuestion(question);
        currentGame.decrementTours();
        if(currentGame.isStarted()) app.updateGameInSession();
    }

    public void sendAnswer(String text) {
        Network.sendAnswer(text, currentGame, currentPlayer.getName(), currentGame.isLastTurn());
    }

    public void sendEndOfClock() {
        Network.sendEndOfClock(currentGame, currentPlayer.getName(), currentGame.isLastTurn());
    }

    public void receiveAnswer(String text, String player) {
        String name = currentPlayer.getName().equals(player) ? "moi" : player;
        currentGame.addAnswer(name + " : " + text);
        app.updateAnswers();
    }

    public void goToMenu() {
        app.goToMenu();
    }

    public void receiveCorrectAnswer(String text, String player, boolean isClockEnd) {
        if(isClockEnd) {
            currentGame.addAnswer("Personne n'a trouvé ! ");
        } else {
            app.killTime();
            for(int i = 0; i < currentGame.getPlayers().size() ; i++) {
                if(player.equals(currentGame.getPlayers().get(i).getName())) {
                    currentGame.getPlayers().get(i).addPoints();
                }
            }
            String name = currentPlayer.getName().equals(player) ? "moi" : player;
            currentGame.addAnswer(name + " : " + text);
            currentGame.addAnswer(player + " a trouvé ! ");
        }
        app.updateAnswers();
    }


    public boolean checkPseudoExistenceInGame(String pseudo, Game game) {
        ArrayList<Player> players = game.getPlayers(); 
        for(Player p : players) {
            if(p.getName().equals(pseudo)) return true;
        }
        return false;
    }

    public void removePlayerFromGame(String game, String player) {
        if(currentGame != null && currentGame.getName().equals(game)) {
            currentGame.remove(player);
        } else {
            for(Game g : games) {
                if(g.getName().equals(game)) {
                    g.remove(player);
                }
            }
        }
    }


    public void addPlayerInGameList(String player, Game game, boolean isLocalPlayer) {
        for(Game g : games) {
            if(g.equals(game)) {
                g.addPlayer(player, isLocalPlayer);
                return;
            } 
        }
        //cas où la partie n'existe pas dans le système
        game.addPlayer(player, isLocalPlayer);
        games.add(game);
    }

    public void deconnection() {
        Network.deconnection(currentGame, currentPlayer.getName(), currentPlayer.isAdmin());
    }

    public void killTime() {
        app.killTime();
    }

    public void receiveDeconnection(Game game, String player, boolean isAdmin) {
        if(isAdmin) {
            games.remove(currentGame);
            currentPlayer = null;
            currentGame = null;
            app.goToError();
        } else {
            removePlayerFromGame(game.getName(), player);
            if(currentPlayer != null) {
                if(player.equals(currentPlayer.getName())) {
                    currentGame = null;
                    currentPlayer = null;
                    app.goToMenu();
                } 
            }
            app.updateGameList();
        }
    }

    public void endGame() {
        games.remove(currentGame);
        app.endGame();
    }

    public void joinGame(String pseudo, Game game) {
        currentPlayer = new Player(pseudo, false);
        currentPlayer.setGame(game.getName());
        Network.joinGame(pseudo, game);
    }

    public void hasJoinedGame(String player, Game game) {
        if(currentPlayer != null && currentPlayer.getGame().equals(game.getName())){
            addPlayerInGameList(player, game, true);
            app.updateGameList();
            currentGame = game;
            app.goToGame();
            return;
        }
        addPlayerInGameList(player, game, false);
        app.updateGameList();
    }

    public void connexion() {
        Network.setConnexion();
    }

    public void deleteCurrentGame() {
        app.updateGameList();
        currentGame = null;
        currentPlayer = null;
    }
}
