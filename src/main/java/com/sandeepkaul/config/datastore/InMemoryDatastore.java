/**
 * 
 */
package com.sandeepkaul.config.datastore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.sandeepkaul.config.DataStore;

/**
 * @author Sandeep Kaul
 *
 */
public class InMemoryDatastore implements IDataStore {

  private static final Logger log = Logger.getLogger(InMemoryDatastore.class.getName());
  
  // The final data is stored in this ConcurrentHashMap which is then queried by getValue method.
  private Map<String, String> data;

  public InMemoryDatastore() {
    data = new ConcurrentHashMap<String, String>();

  }

  @Override
  public String get(String key) {

    return data.get(key);
  }

  @Override
  public void put(String key, String value) {
    data.put(key, value);

  }

  @Override
  public void print() {
    log.info("Data:"+data);
    
  }

  @Override
  public boolean containsKey(String key) {
    return data.containsKey(key);
  }

}
