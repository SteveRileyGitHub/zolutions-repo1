package com.zolutions.util;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;

/**
 *
 */
public class PropertiesManager {
  static Logger logger = Logger.getLogger(PropertiesManager.class);

  private Properties theProperties;
  /**
   * A pool of PropertiesManager's for each Property File.
   */
  private static Map propertiesManagerPool = new HashMap();

  private PropertiesManager(File propertiesFile) throws FileNotFoundException, IOException {
    if(logger.isEnabledFor(Priority.DEBUG)) logger.debug("PropertiesManager constructed");

    if(propertiesFile == null) {
      throw new PropertyException("PropertyManager constructed with a null value");
    }
    if(!propertiesFile.exists()) {
      throw new PropertyException("Properties file ["+propertiesFile.getAbsolutePath()+"] does not exist, hence cannot construct an associated PropertiesManager");
    }

    if(logger.isEnabledFor(Priority.DEBUG)) logger.debug("setting and loading Properties file "+propertiesFile.getAbsolutePath());
    theProperties = new Properties();
    FileInputStream fis = new FileInputStream(propertiesFile);
    theProperties.load(fis);
    fis.close();

  }

  public static PropertiesManager getInstance(File propertiesFile) throws FileNotFoundException, IOException {
    String key = propertiesFile.getAbsolutePath();
    Object o = propertiesManagerPool.get(key);
    if(o != null) return (PropertiesManager)o;

    PropertiesManager pm = new PropertiesManager(propertiesFile);
    propertiesManagerPool.put(key,pm);

    return pm;
  }

  private String getFullQualifiedName(Object o, String property) {
    StringBuffer sb = new StringBuffer(o.getClass().getName());
    sb.append(".");sb.append(property);
    return sb.toString();
  }

  /**
   * This method concatenates the given Object's fully qualified class name and
   * the property creating a fully qualified property name, returning the property
   * value associated with this fully qualified property name. If no property has been
   * set for the given property, the default value is returned.
   *
   * @return the value of the property, or the default value if not set.
   */
  public String get(Object o, String property, String defaultValue) {
    return get(getFullQualifiedName(o,property), defaultValue);
  }



  /**
   * Returns the property value of the given property. If no property has been
   * set for the given property, the default value is returned.
   *
   * @return the value of the property, or the default value if not set.
   */
  public String get(String property, String defaultValue) {
    String value = (String)theProperties.get(property);
    if(value == null) {
      if(logger.isEnabledFor(Priority.DEBUG)) logger.debug("Property "+property+" has not been set, therefore returning "+defaultValue);
      return defaultValue;
    }
    else {
      if(logger.isEnabledFor(Priority.DEBUG)) logger.debug("Property "+property+" has been set, therefore returning "+value);
      return value;
    }
  }

  /**
   * Returns the property value (as a double) of the given property. If no property has been
   * set for the given property, the default value is returned. NOte: if the value returned
   * (or the default value) cannot be parsed to a double, a PropertyException will be thrown.
   * As the PropertyException is-a RuntimeException it need not be caught.
   *
   * @return the value of the property, or the default value if not set.
   */
  public double get(String property, double defaultValue) {
    String value="";
    try {
      value = get(property,defaultValue+"");
      return Double.parseDouble(value);
    }
    catch (NumberFormatException nfe) {
      throw new PropertyException("Property ["+property+"="+value+"] cannot be parsed to a double.");
    }
  }

  /**
   * This method concatenates the given Object's fully qualified class name and
   * the property creating a fully qualified property name, returning the property
   * value associated with this fully qualified property name. If no property has been
   * set for the given property, the default value is returned.
   *
   * @see #get(String property, double defaultValue)
   */
  public double get(Object o, String property, double defaultValue) {
    return get(getFullQualifiedName(o,property),defaultValue);
  }


  public int get(String property, int defaultValue) {
    String value="";
    try {
      value = get(property,defaultValue+"");
      return Integer.parseInt(value);
    }
    catch (NumberFormatException nfe) {
      throw new PropertyException("Property ["+property+"="+value+"] cannot be parsed to an integer.");
    }
  }
  public double get(Object o, String property, int defaultValue) {
  return get(getFullQualifiedName(o,property),defaultValue);
  }


  public boolean get(String property, boolean defaultValue) {
    String value = get(property,defaultValue+"");
    if(value.equalsIgnoreCase("true")) return true;
    else if (value.equalsIgnoreCase("false")) return false;
    else throw new PropertyException("Property ["+property+"="+value+"] annot be parsed to a boolean value (must be true or false).");
  }

  public boolean get(Object o, String property, boolean defaultValue) {
    return get(getFullQualifiedName(o,property),defaultValue);
  }


  public static void main(String[] args) {
    // set up log4j configuration.
    String propFilename = "log4j_PropertiesManager_config.properties";
    java.io.File f = new java.io.File(propFilename);
    if(!f.exists()) {
      f = new java.io.File(ClassLoader.getSystemResource(propFilename).getPath());
    }
    if(f.exists()) PropertyConfigurator.configure(f.getAbsolutePath());
    else BasicConfigurator.configure();
    /*
    com.zolutions.util.log4j.DebugFrame df = new com.zolutions.util.log4j.DebugFrame();
    df.startMonitoring();
    df.show();
*/
    try {

      PropertiesManager pm = PropertiesManager.getInstance(new File("example.properties"));

      System.out.println("title = "+pm.get(pm.getClass().getName()+".title", "DefaultTitle"));
      System.out.println("value1 = "+pm.get(pm.getClass().getName()+".value1", 1.0));
      System.out.println("title = "+pm.get(pm,"title", "DefaultTitle"));
      System.out.println("value1 = "+pm.get(pm,"value1", 1.0));
      System.out.println("debug = "+pm.get("debug","false"));

      try {
        System.out.println("value2 = "+pm.get(pm,"value2",1.0));
      }
      catch(PropertyException pe) {
        System.out.println(pe.getMessage());
      }
      System.out.println("value3 = "+pm.get(pm,"value3",1.0));

    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    System.out.println("Finished");
    System.exit(1);
  }
}