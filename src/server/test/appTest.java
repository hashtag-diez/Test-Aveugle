package test;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import src.utils.Serialization;

public class appTest {
  public static String username;
  private volatile String line = ""; 
  public void waitForLoad(AsynchronousSocketChannel socket, String line) throws 
    InterruptedException, ExecutionException, ClassNotFoundException, IOException{
    while(true){
      ByteBuffer buffer = ByteBuffer.allocate(4024000);
      System.out.println("Nouvelle réponse !");
      socket.read(buffer).get();
      handleResponse(buffer);
    }
  }
  public Map<String, Map<String, String>>  outputParser(String line){
    Map<String, Map<String, String>> request = new HashMap<String, Map<String, String>>();
    Map<String, String> type = new HashMap<String, String>();
    Map<String, String> params = new HashMap<String, String>();
    String[] command = line.split(" ");
    switch(command[0]){
      case "CHANNEL_CREATE":
        System.out.println("On veut créer une room !");
        type.put("type", command[0]);
        params.put("channelName", command[1]);
        params.put("adminName", command[2]);
        params.put("categorieName", command[3]);
        request.put("header", type);
        request.put("params", params);
        break;
      case "CHANNEL_DELETE":
        System.out.println("On veut quitter une room !");
        type.put("type", command[0]);
        params.put("channelName", command[1]);
        params.put("adminName", command[2]);
        request.put("header", type);
        request.put("params", params);
        break;
      case "CHANNEL_START":
        System.out.println("On veut quitter une room !");
        type.put("type", command[0]);
        params.put("channelName", command[1]);
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
        System.out.println("On veut se connecter à une room !");
        type.put("type", command[0]);
        params.put("questionResponse", command[1]);
        params.put("userAnswer",  String.join(" ",Arrays.copyOfRange(command, 3, command.length)));
        params.put("pseudo", command[2]);
        request.put("header", type);
        request.put("params", params);
        break;
      case "CHANNEL_QUESTIONS":
        System.out.println("On veut la liste des images pour la catégorie " + command[1]);
        type.put("type", command[0]);
        params.put("categorieName", command[1]);
        params.put("channelName", command[2]);
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
  public void handleRequest(AsynchronousSocketChannel socket, String line, Scanner scanner) throws 
    InterruptedException, ExecutionException, IOException, ClassNotFoundException{
      while(!line.equals("Bye !")){
        line = scanner.nextLine();
        Map<String,Map<String,String>> request = outputParser(line);
        socket.write(ByteBuffer.wrap(Serialization.serializeMap(request))).get();
      }
    sendDisconnectLoad(socket);
    scanner.close();
  }
  public void handleResponse(ByteBuffer buffer) throws 
    ClassNotFoundException, IOException{
      
      Map<String,Map<String,String>> response = Serialization.deserializeMap(buffer.flip().array());
      System.out.println("{");
      for(Map.Entry<String, Map<String,String>> fields : response.entrySet()){
        System.out.println("    "+ fields.getKey() + ": {");
        Map<String,String> values = fields.getValue();
        for(Map.Entry<String, String> props : values.entrySet()){
          System.out.println("       " + props.getKey() + ": " + props.getValue());
        }
        System.out.println("    }");
      }
      System.out.println("}");
  }
  public void sendDisconnectLoad(AsynchronousSocketChannel socket) throws 
    InterruptedException, ExecutionException, IOException{
      Map<String,Map<String,String>> exit = outputParser("EXIT");
      socket.write(ByteBuffer.wrap(Serialization.serializeMap(exit))).get();
  } 
  public void setShutdownHook(AsynchronousSocketChannel socket){
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
  public void run() throws 
    InterruptedException, ExecutionException, ClassNotFoundException, IOException {
      try(AsynchronousSocketChannel socket = AsynchronousSocketChannel.open()) {
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
  public static void main(String[] args) throws ClassNotFoundException, InterruptedException, ExecutionException, IOException {
    new appTest().run();
  }
}
