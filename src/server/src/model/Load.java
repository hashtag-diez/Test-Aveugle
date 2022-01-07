package src.model;

import java.util.HashMap;
import java.util.Map;

public class Load {
  private Range range;
  private Type type;
  private Map<String,Map<String,String>> data = new HashMap<String, Map<String, String>>();

  public Load(){}
  public Load(Map<String,String> header, Map<String, Map<String,String>> params, boolean isRequest){
    this.type = Type.valueOf(header.get("type"));
    if(!isRequest){
      this.range = Range.valueOf(header.get("range"));
      this.status = Status.valueOf(header.get("status")); 
    }
    params.remove("header");
    this.data = params;
  }
  public Range getRange() {
    return range;
  }
  public Status getStatus() {
    return status;
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
  public Map<String, Map<String, String>> getData() {
    return data;
  }
  public void setData(Map<String, Map<String, String>> data) {
    this.data = data;
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
