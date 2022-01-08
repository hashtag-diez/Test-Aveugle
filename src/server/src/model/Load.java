package src.model;

import java.util.HashMap;
import java.util.Map;

public class Load {
  private Range range;
  private Status status;
  private Type type;
  private Map<String,Map<String,String>> data = new HashMap<String, Map<String, String>>();

  public Load(){}
  public Load(Map<String,String> header, Map<String, Map<String,String>> params){
    this.type = Type.valueOf(header.get("type"));
    System.out.println("Type du load créé : " + this.type);
    params.remove("header");
    this.data = params;
  }
  public Range getRange() {
    return range;
  }
  public void setRange(Range range) {
    this.range = range;
  }
  public Type getType() {
    return type;
  }
  public Status getStatus() {
    return status;
  }
  public void setType(Type type) {
    this.type = type;
  }
  public Map<String, Map<String, String>> getData() {
    return data;
  }
  public void setData(Map<String, Map<String, String>> data) {
    this.data = data;
  }
  public void setStatus(Status status){
    this.status = status;
  }
  public Map<String,Map<String,String>> convertToMap(){
    Map<String,Map<String,String>> res = new HashMap<String, Map<String, String>>();
    Map<String, String> header = new HashMap<String, String>();
    header.put("type", type.toString());
    if(status!=null && range!=null){
      header.put("status", status.toString());
      header.put("range", range.toString());
    }
    res.put("header", header);
    res.putAll(data);
    return res;
  }
}
