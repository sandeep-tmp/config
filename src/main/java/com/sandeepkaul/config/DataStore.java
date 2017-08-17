package com.sandeepkaul.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.sandeepkaul.config.datastore.IDataStore;
import com.sandeepkaul.config.datastore.InMemoryDatastore;

/**
 * @author Sandeep Kaul
 *
 */
public class DataStore {

  private static final Logger log = Logger.getLogger(DataStore.class.getName());

  // Incase config format needs to be changed, this Delimiter can be updated from one place. Also,
  // if required it can be put into config/conftructor but I don't see it worth doing now.
  private final String DELIMITER = " = ";

  
   private IDataStore dataStore;

  private String currentSection;
  private Stack<String> currentSubSectionStack;
  private String currentSubSection;
  private String currentPath;

  public DataStore() {
    dataStore = new InMemoryDatastore();
    currentSubSectionStack = new Stack<>();
  }

  /**
   * 
   * @param filePath
   * 
   *        This method parses the file and stores the data in the form in which it be queried. The
   *        idea is that since loading will be one time and reading would be multiple times, we'll
   *        construct the map based on the kind of requests it'll get.
   */
  public void load(String filePath) {

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String sCurrentLine;
      while ((sCurrentLine = br.readLine()) != null) {
        processLine(sCurrentLine);
      }

    } catch (IOException e) {
      throw new RuntimeException("Error in reading the file");
    }
    log.info("Data Loading complete: "); dataStore.print();

  }

  private void processLine(String line) {
    line = line.trim();
    if (line.length() == 0) {
      return;
    }
    if (line.contains(DELIMITER)) {
      processValueLine(line);
    } else {
      processSectionLine(line);
    }

  }

  public void print() {
    dataStore.print();
  }
  private void processSectionLine(String line) {
    if (line.startsWith("Section")) {
      currentSection = line;
      currentSubSection = null;
      currentPath = currentSection;
    } else {
      if(currentSubSectionStack.isEmpty()) {
        currentSubSectionStack.push(line);
      } else {
        while(!currentSubSectionStack.isEmpty()) {
         
          String topObj = currentSubSectionStack.peek();
          if(line.startsWith(topObj)) {
            break;
          } else  {
            currentSubSectionStack.pop();
          }
        }
        currentSubSectionStack.push(line);
      }
      if (StringUtils.isBlank(currentSubSection)) {
        currentSubSection = line;
        currentPath = currentPath + "::" + currentSubSection;
      } else if (line.startsWith(currentSubSection)) {
        // Sub section of current Sub Section
        currentPath = currentPath + "::" + line;
        currentSubSection = line;
      } else {
        // New Sub Section under current Section/Subsection
        System.out.println("Line:" + line);
        System.out.println("currentPath" + currentPath);
        System.out.println("currentSubSection" + currentSubSection);
        String path = getPath(currentSection, currentSubSectionStack);
        currentPath = currentPath.substring(0, currentPath.lastIndexOf(currentSubSection));
        currentPath = currentPath + line;
        currentPath = path;
        currentSubSection = line;
        System.out.println("currentPath" + path);
      }
    }

  }

  private String getPath(String currentSection, Stack<String> currentSubSectionStack) {
    StringBuilder pathBuilder = new StringBuilder();
    pathBuilder.append(currentSection);
    if(!currentSubSectionStack.isEmpty()) {
      for(String subSections : currentSubSectionStack)
      {
        if(StringUtils.isNotBlank(subSections)) {
          pathBuilder.append("::").append(subSections);
        }
      }
      
    }
    return pathBuilder.toString();
  }

  private void processValueLine(String line) {
    String[] keys = line.split(DELIMITER);
    String key, value;
    if (keys.length == 2) {
      key = keys[0];
      value = keys[1];
      if (StringUtils.isBlank(currentPath)) {
        dataStore.put(key, value);
      } else {
        dataStore.put(currentPath + "::" + key, value);
      }
    } else {
      // Ignore the key. Also we are ignoring lines like [key=]
    }
  }

  /**
   * 
   * @param key
   * @return value
   * 
   *         This method queries the ConcurrentHashMap and fetches the values against the key. If
   *         the keys are fetched from the parent, the parent-key=value map is now stored in the
   *         ConcurrentHashMap so that the next lookup on same key would be faster.
   */
  public String get(String key) {
    log.fine("Get called for:" + key);
    if (StringUtils.isBlank(key)) {
      return null;
    }
    if (dataStore.containsKey(key)) {
      return dataStore.get(key);
    }
    String parentKey = getParentKey(key);
    String value = get(parentKey);
    if (StringUtils.isNotBlank(value)) {
      log.fine("Putting in map: " + key + " : " + value);
      dataStore.put(key, value);
      log.fine("Putting in map: " + parentKey + " : " + value);
      dataStore.put(parentKey, value);
    }
    return value;
  }

  private String getParentKey(String key) {
    if (!key.contains("::")) {
      log.fine("Parent of " + key + " is null");
      return null;
    }
    String parent = "";
    String[] splits = key.split("::");
    if (splits.length == 2) {
      parent = splits[1];
    } else {

      int counter = 0;
      for (String string : splits) {
        if (counter == splits.length - 2) {
          // Do Nothing
        } else {
          if (parent.length() == 0) {
            parent = parent + string;
          } else {
            parent = parent + "::" + string;
          }
        }
        counter++;
      }
    }
    log.fine("Parent of " + key + " is " + parent);
    return parent;
  }


}
