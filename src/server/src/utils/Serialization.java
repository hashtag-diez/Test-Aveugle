package server.src.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import server.src.model.Load;

public class Serialization {

  @SuppressWarnings("unchecked")
  public static Load deserializeLoad(byte[] bytes) throws ClassNotFoundException{
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
      ObjectInput in = null;
      try {
      	in = new ObjectInputStream(bis);
      	Map<String,String> params = (Map<String,String>) in.readObject(); 
        Load res = new Load(params.get("type"), params);
      	return res;
      }  
      finally{
      	try {
      		if (in != null) {
      			in.close();
      		}
      	} catch (IOException e) {
      		throw e;
      	}
      }
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
      Map<String,Map<String,String>> results = new HashMap<String,Map<String,String>>();
      Map<String,String> type = new HashMap<String,String>();
      type.put("name", load.getType().toString());
      results.put("type",type);
      results.put("results", load.getData().get("results"));
			out.writeObject(results);
			out.flush();
			byte[] res = bos.toByteArray();
			return res;
		}
		catch(IOException e){throw e;}
	}
}
