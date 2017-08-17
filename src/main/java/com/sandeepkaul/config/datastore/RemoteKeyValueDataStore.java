/**
 * 
 */
package com.sandeepkaul.config.datastore;

import com.sandeepkaul.config.datastore.config.RemoteDataStoreConfig;

/**
 * @author Sandeep Kaul
 * 
 * TODO: this needs to be completed based on choice of data store.
 *
 */
public class RemoteKeyValueDataStore  implements IDataStore{

  private RemoteDataStoreConfig config;
  
  public RemoteKeyValueDataStore(RemoteDataStoreConfig config) {
    this.config = config;
  }
  
  @Override
  public String get(String key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean containsKey(String key) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void put(String key, String value) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void print() {
    // Print wont do anything in case of remote datastore.
    return;
  }

}
