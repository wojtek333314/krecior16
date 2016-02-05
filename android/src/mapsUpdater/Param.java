package mapsUpdater;

import java.util.HashMap;

public class Param {
  private HashMap<String,String> map = new HashMap<String,String>();
  
  public Param(String ...key_value){
	  for(int i=0;i<key_value.length;i+=2){
		  map.put(key_value[i], key_value[i+1]);
	  }
  }
  
  public HashMap<String,String> getMap(){
	  return map;
  }
  
  public String getQuery(){
	  String query="?";
	  
	  int i=0;
	  for(String key : map.keySet()){
		  if(i>0)
			  query+="&";
		  query+=key;
		  query+="="+map.get(key);
		  i++;
	  }
	  
	//  System.out.println(query);
	  return query;
	 }
  
}
