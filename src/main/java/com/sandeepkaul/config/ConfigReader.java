/**
 * 
 */
package com.sandeepkaul.config;

import java.util.logging.Logger;

/**
 * @author Sandeep Kaul
 *
 */
public class ConfigReader {
  
  private static final Logger log = Logger.getLogger(ConfigReader.class.getName());
  
  private static DataStore dataSore;
  static {
    dataSore = new DataStore();
  }
  private static boolean isInit = false;

  // This can be called multiple times, and the data would be merged from all the files..
  public static synchronized boolean initialize(String filePath) {

    dataSore.load(filePath);
    isInit = true;
    log.info("Loading file complete for file: "+filePath);
    return isInit;
  }

  public static  void print() {
    dataSore.print();
  }
  // Key will be passed in the form of “Section-1::Sub-Section-1::Sub-Section1_1::Var1”
  public static String getValue(String key) {

    if(isInit == false) {
      throw new RuntimeException("Config is not yet initialized.");
    }
    String value = dataSore.get(key);
    log.info("Returning value: "+value +" for key "+key);
    return value;
  }

}
