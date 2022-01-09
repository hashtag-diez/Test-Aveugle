package src;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import src.model.*;
import src.router.Router;
import src.utils.Serialization;

public class App implements Callable<Boolean> {
	private AsynchronousServerSocketChannel server;

	public static Catalogue catalogue = new Catalogue();
	// TODO: correct related bugs
	public static List<AsynchronousSocketChannel> clients = new ArrayList<AsynchronousSocketChannel>();
	public static Map<AsynchronousSocketChannel, Channel> client_Channel = new HashMap<AsynchronousSocketChannel, Channel>();
	public static List<Channel> rooms = new ArrayList<Channel>();
	
	private Router router;
	public App() throws Exception{
			router = new Router();
	} 
	public void handleRequest(Load request, Load response, AsynchronousSocketChannel client) throws 
		InterruptedException, ExecutionException, IOException{
			router.run(request, response, client);
			if(!response.getType().equals(Type.EXIT)){
				if(response.getRange().equals(Range.EVERYONE)){
					sendToAll(response);
				}
				if(response.getRange().equals(Range.ONLY_PLAYERS)){
					String channelName = request.getData().get("params").get("channelName");
					sendToPlayers(response, channelName);
				}
				else{
					sendToOneClient(response, client);
				}
			}
	}
	public void waitForRequest(AsynchronousSocketChannel client) throws 
		InterruptedException, ExecutionException, IOException, ClassNotFoundException{
			while(true&&clients.contains(client)){
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				client.read(buffer).get();
				Load request = Serialization.deserializeLoad(buffer.flip().array());
				System.out.println("Type de requête : "+ request.getType());
				Load response = new Load();
				response.setType(request.getType());
				handleRequest(request, response, client);
			}
	}
	public void sendToAll(Load res) throws InterruptedException, ExecutionException, IOException{
		// Faire sur la liste de users à la place
    for (AsynchronousSocketChannel client : clients) {
			client.write(ByteBuffer.wrap(Serialization.serializeLoad(res))).get();
		}
	}
	public void sendToPlayers(Load res, String channelName) throws InterruptedException, ExecutionException, IOException{
    for (Channel c : App.rooms) {
			if(c.getChannelName().equals(channelName)){
				for(User user : c.getChannelParticipants()){
					user.getClientSocket().write(ByteBuffer.wrap(Serialization.serializeLoad(res))).get();
				}
			}
		}
	} 
	public void sendToOneClient(Load res, AsynchronousSocketChannel client) throws IOException, InterruptedException, ExecutionException{
		client.write(ByteBuffer.wrap(Serialization.serializeLoad(res))).get();
	}
	@Override
	public Boolean call() throws Exception {
		server = AsynchronousServerSocketChannel.open();
		server.bind(new InetSocketAddress("localhost", 1237));
		server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
			@Override
			public void completed(AsynchronousSocketChannel client, Object attachment) {
				server.accept(null, this);
				try{
					System.out.println("Un client s'est connecté depuis " + client.getRemoteAddress());
					App.clients.add(client);
					Load request = new Load();
					request.setType(Type.GET_CHANNELS);
					Load response = new Load();
					router.run(request, response, client);
					sendToOneClient(response, client);
				} catch(Exception e) {
					failed(e, null);
				}
				try {
					waitForRequest(client);
				} catch (Exception e) {
					failed(e, null);
				}
			}
			@Override
			public void failed(Throwable exc, Object attachment) {
				exc.printStackTrace();
			}
		});
		System.in.read();
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		new App().call();
	}
}
