package com.zolutions.util.log4j;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import org.apache.log4j.*;

public class Debug {

  public Debug() {
  }

  public static void initialiseLog4j() {
    //////////////////////////////////////////////
    // setup log4j properties.
    java.io.File f = new java.io.File("log4j_config.properties");
    if(!f.exists()) {
      java.net.URL url = ClassLoader.getSystemResource("log4j_config.properties");
      if(url != null) {
        f = new java.io.File(url.getPath());
      }
    }
    if(f.exists()) PropertyConfigurator.configure(f.getAbsolutePath());
    else BasicConfigurator.configure();
    //////////////////////////////////////////////
  }
}