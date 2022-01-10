package src.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.nio.channels.AsynchronousSocketChannel;
import src.utils.Serialization;

import java.util.Map;
import java.util.Scanner;
import java.util.Arrays;
import java.util.HashMap;

import java.nio.ByteBuffer;

public class UserConnection {
  private volatile String line;
  private static AsynchronousSocketChannel socket;

  public UserConnection(String line) {
    this.line = line;
  }

  public void waitForLoad(AsynchronousSocketChannel socket, String line)
      throws InterruptedException, ExecutionException, ClassNotFoundException, IOException {
    while (true) {
      ByteBuffer buffer = ByteBuffer.allocate(4024000);
      socket.read(buffer).get();
      handleResponse(buffer);
    }
  }

  public static Map<String, Map<String, String>> outputParser(String line) {
    Map<String, Map<String, String>> request = new HashMap<String, Map<String, String>>();
    Map<String, String> type = new HashMap<String, String>();
    Map<String, String> params = new HashMap<String, String>();
    String[] command = line.split(" ");
    switch (command[0]) {
      case "CHANNEL_CREATE":
        System.out.println("On veut créer une room !");
        type.put("type", command[0]);
        params.put("channelName", command[1]);
        params.put("adminName", command[2]);
        params.put("categorieName", command[3].toLowerCase());
        params.put("nbTours", command[4]);
        request.put("header", type);
        request.put("params", params);
        break;
      case "CHANNEL_DELETE":
        System.out.println("On veut supprimer une partie !");
        type.put("type", command[0]);
        params.put("channelName", command[1]);
        params.put("adminName", command[2]);
        request.put("header", type);
        request.put("params", params);
        break;
      case "CHANNEL_START":
        System.out.println("On veut commencer le jeu!");
        type.put("type", command[0]);
        params.put("channelName", command[1]);
        params.put("categorieName", command[2]);
        request.put("header", type);
        request.put("params", params);
        break;
      case "USER_CONNECT":
        System.out.println("On veut se connecter à une room !");
        type.put("type", command[0]);
        params.put("channelName", command[1]);
        params.put("pseudo", command[2]);
        request.put("header", type);
        request.put("params", params);
        break;
      case "USER_DISCONNECT":
        System.out.println("On veut quitter à une room !");
        type.put("type", command[0]);
        params.put("channelName", command[1]);
        params.put("pseudo", command[2]);
        request.put("header", type);
        request.put("params", params);
        break;
      case "USER_ANSWER":
        System.out.println("On veut envoyer sa réponse !");
        type.put("type", command[0]);
        params.put("questionResponse", command[1]);
        params.put("userAnswer", String.join(" ", Arrays.copyOfRange(command, 4, command.length)));
        params.put("pseudo", command[2]);
        params.put("channelName", command[3]);
        request.put("header", type);
        request.put("params", params);
        break;
      case "GET_CHANNELS":
        System.out.println("On veut la liste des rooms !");
        type.put("type", command[0]);
        request.put("header", type);
        break;
      case "SCORE_REFRESH":
        System.out.println("On veut rafraichir le score !");
        type.put("type", command[0]);
        params.put("channelName", command[1]);
        params.put("pseudo", command[2]);
        params.put("categorieName", command[3]);
        request.put("header", type);
        request.put("params", params);
        break;
      case "EXIT":
        type.put("type", command[0]);
        request.put("header", type);
        break;
      default:
        break;
    }
    return request;
  }

  public void handleRequest(AsynchronousSocketChannel socket, String line, Scanner scanner)
      throws InterruptedException, ExecutionException, IOException, ClassNotFoundException {
    while (!line.equals("Bye !")) {
      line = scanner.nextLine();
      Map<String, Map<String, String>> request = outputParser(line);
      socket.write(ByteBuffer.wrap(Serialization.serializeMap(request))).get();
    }
    sendDisconnectLoad(socket);
    scanner.close();
  }

  public static void sendRequest(String line) throws IOException, ExecutionException, InterruptedException {
    Map<String, Map<String, String>> request = outputParser(line);
    socket.write(ByteBuffer.wrap(Serialization.serializeMap(request))).get();
  }

  public void handleResponse(ByteBuffer buffer) throws ClassNotFoundException, IOException {
    Map<String, Map<String, String>> response = Serialization.deserializeMap(buffer.flip().array());
    if (response.get("header").get("status").equals("OK")) {
      switch (response.get("header").get("type")) {
        case "CHANNEL_CREATE":
          System.out.println("On reçoit un nouveau channel !");
          Network.receiveGame(response);
          break;
        case "CHANNEL_DELETE":
          System.out.println("On reçoit une suppression de channel !");
          Network.receiveDeletion(response);
          break;
        case "CHANNEL_START":
          System.out.println("On reçoit un début de partie !");
          Network.gameStarted(response); // TODO
          break;
        case "GET_CHANNELS":
          System.out.println("On reçoit tous les channels !");
          Network.receiveGameList(response);
          break;
        case "USER_CONNECT":
          System.out.println("On un nouveau joueur !");
          Network.hasJoinedGame(response); // ok
          break;
        case "USER_DISCONNECT":
          System.out.println("Un joueur s'est déconnecté!");
          Network.receiveDeconnection(response); // ok
          break;
        case "USER_ANSWER":
          System.out.println("On reçoit un réponse!");
          Network.receiveAnswer(response); // ok
          break;
        case "SCORE_REFRESH":
          System.out.println("On reçoit le score et une nouvelle question!");
          Network.scoreRefresh(response); //
          break;
        default:
          break;
      }
    } else {
      System.out.println("Erreur");
      // Network.setAllRequestError ??
    }
  }

  public void sendDisconnectLoad(AsynchronousSocketChannel socket)
      throws InterruptedException, ExecutionException, IOException {
    Map<String, Map<String, String>> exit = outputParser("EXIT");
    socket.write(ByteBuffer.wrap(Serialization.serializeMap(exit))).get();
  }

  public void setShutdownHook(AsynchronousSocketChannel socket) {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          Thread.sleep(200);
          sendDisconnectLoad(socket);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          e.printStackTrace();
        } catch (ExecutionException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void run() throws InterruptedException, ExecutionException, ClassNotFoundException, IOException {
    try (AsynchronousSocketChannel sock = AsynchronousSocketChannel.open()) {
      socket = sock;
      socket.connect(new InetSocketAddress("localhost", 1237)).get();
      Scanner scanner = new Scanner(System.in);
      ExecutorService pool = Executors.newCachedThreadPool();
      pool.submit(() -> {
        try {
          waitForLoad(socket, line);
        } catch (ClassNotFoundException | InterruptedException | ExecutionException | IOException e) {
          e.printStackTrace();
        }
      });
      setShutdownHook(socket);
      handleRequest(socket, line, scanner);
    }
  }

}
