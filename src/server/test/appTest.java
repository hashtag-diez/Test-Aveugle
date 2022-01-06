package server.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.src.utils.Serialization;

public class appTest {
  public static String username;
  private volatile String line = ""; 
  public void waitForLoad(AsynchronousSocketChannel socket, String line) throws 
    InterruptedException, ExecutionException, ClassNotFoundException, IOException{
    while(true){
      ByteBuffer buffer = ByteBuffer.allocate(1024);
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
      case "USER_CONNECT":
        System.out.println("On veut se connecter au serveur !");
        type.put("type", command[0]);
        params.put("username", command[1]);
        request.put("header", type);
        request.put("params", params);
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
    //sendDisconnectLoad(socket);
    scanner.close();
  }
  public void handleResponse(ByteBuffer buffer) throws 
    ClassNotFoundException, IOException{
      Map<String,Map<String,String>> response = Serialization.deserializeMap(buffer.flip().array());
      Map<String,String> header = response.get("header");
      if(header.get("status").equals("OK")){
        switch(header.get("type")){
          case "USER_CONNECT":
            username = response.get("result").get("message");
            System.out.println("Le server a bien reçu "+  username);
            assertTrue("Erreur, mauvaise réception", username.equals(line.split(" ")[0]));
            break;
          default:
            break;
        }
      } else {
        System.out.println("Erreur");
      }
  }
/*   public void sendDisconnectLoad(AsynchronousSocketChannel socket) throws 
    InterruptedException, ExecutionException, IOException{
      Map<String, String> params = new HashMap<String,String>();
      params.put("username", username);
      Load req = new Load("EXIT",params);
      socket.write(ByteBuffer.wrap(Serialization.serializeLoad(req))).get();
  } */
 /*  public void setShutdownHook(AsynchronousSocketChannel socket){
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
  } */
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
/*       setShutdownHook(socket);
 */   handleRequest(socket, line, scanner);
    }
	}
  public static void main(String[] args) throws ClassNotFoundException, InterruptedException, ExecutionException, IOException {
    new appTest().run();
  }
}
