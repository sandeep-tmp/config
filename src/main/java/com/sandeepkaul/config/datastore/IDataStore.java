package com.sandeepkaul.config.datastore;

/**
 * @author Sandeep Kaul
 *
 */
public interface IDataStore {

  public String get(String key);
  
  public boolean containsKey(String key);
  
  public void put(String key, String value);
  
  public void print();
  
  
}
