package server.src.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import server.src.model.Load;

public class Serialization {

	@SuppressWarnings("unchecked")
	public static Load deserializeLoad(byte[] bytes, boolean isRequest) throws ClassNotFoundException{
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);    
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			Map<String,Map<String,String>> data = (Map<String,Map<String,String>>) in.readObject(); 
			Load res = new Load(data.get("header"), data , isRequest);
			return res;
		} catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  public static byte[] serializeLoad(Load load) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try{
			out = new ObjectOutputStream(bos);
      Map<String,Map<String,String>> results = load.convertToMap();
			out.writeObject(results);
			out.flush();
			byte[] res = bos.toByteArray();
			return res;
		}
		catch(IOException e){throw e;}
	}
	@SuppressWarnings("unchecked")
	public static Map<String,Map<String,String>> deserializeMap(byte[] bytes) throws ClassNotFoundException{
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);    
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			Map<String,Map<String,String>> map = (Map<String,Map<String,String>>) in.readObject(); 
			return map;
		} catch (IOException e) {
      e.printStackTrace();
    }
    return null;
	}
	public static byte[] serializeMap(Map<String,Map<String,String>> map) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try{
			out = new ObjectOutputStream(bos);
			out.writeObject(map);
			out.flush();
			byte[] res = bos.toByteArray();
			return res;
		}
		catch(IOException e){throw e;}
	}
}
