package server.src.model;

import java.util.HashMap;
import java.util.Map;

public class Load {
  private Range range;
  private Type type;
  private Status status;
  private Map<String,Map<String,String>> data = new HashMap<String, Map<String, String>>();

  public Load(){}
  public Load(String type, Map<String,String> params){
    this.type = Type.valueOf(type);
    params.remove("type");
    data.put("params", params);
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
  public void setType(Type type) {
    this.type = type;
  }
  public Status getStatus() {
    return status;
  }
  public void setStatus(Status status) {
    this.status = status;
  }
  public Map<String, Map<String, String>> getData() {
    return data;
  }
  public void setData(Map<String, Map<String, String>> data) {
    this.data = data;
  }
}
