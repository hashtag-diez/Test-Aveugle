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
            games.add(newGame);
            app.updateGameList();
            currentGame = newGame;
            app.goToGame();
        } else {
            games.add(newGame);
            app.updateGameList();
        }
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

    public void sendAnswer(String text) {
        Network.sendAnswer(text, currentGame, currentPlayer.getName());
    }

    public void receiveAnswer(String text, String player) {
        String isMe = player.equals(currentPlayer.getName()) ? " (moi) : " : " : ";
        currentGame.addAnswer(player + isMe + text);
        app.updateAnswers();
    }

    public boolean checkPseudoExistenceInGame(String pseudo, Game game) {
        ArrayList<Player> players = game.getPlayers(); 
        for(Player p : players) {
            if(p.getName().equals(pseudo)) return true;
        }
        return false;
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
}
