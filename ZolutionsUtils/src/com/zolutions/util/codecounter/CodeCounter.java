package com.zolutions.util.codecounter;

import java.io.*;

import org.apache.log4j.*;

public class CodeCounter {

  static Logger logger = Logger.getLogger("CodeCounter");

  private static CodeCounter cc;

  private CodeCounter() {
  }

  public static CodeCounter getInstance() {
    if(cc == null) {
      cc = new CodeCounter();
    }
    return cc;
  }

  private java.util.List getAllFilesInDirectory(File directory, String extension) {
    java.util.ArrayList files = new java.util.ArrayList();
    File[] dirContents = directory.listFiles();
    for(int i=dirContents.length-1; i>-1; i--) { // reverse is quicker in Java
      if((dirContents[i].isFile()) && (dirContents[i]).getName().endsWith(extension)) {
        files.add(dirContents[i]);
      }
    }
    return files;
  }

  private java.util.List getAllDirectories(File directory) {
    java.util.ArrayList dirs = new java.util.ArrayList();
    File[] dirContents = directory.listFiles();
    for(int i=dirContents.length-1; i>-1; i--) { // reverse is quicker in Java
      if(dirContents[i].isDirectory()) dirs.add(dirContents[i]);
    }
    return dirs;
  }

  private java.util.List getAllFilesInAndBelowDirectory(File directory, String extension) {
    java.util.List files = new java.util.ArrayList();
    java.util.List dirs = getAllDirectories(directory);
    // if no sub-directories, add the files to the list
    if (dirs.size() == 0) {
      files.addAll(getAllFilesInDirectory(directory, extension));
    }
    else {
      // otherwise, descend to each sub-directory, and recursively call this method.
      java.util.Iterator it = dirs.iterator();
      while (it.hasNext()) {
        File subDir = (File)it.next();
        if(logger.isDebugEnabled()) logger.debug("descending into sub-dir "+subDir);
        files.addAll(getAllFilesInAndBelowDirectory(subDir, extension) );
      }
    }

    return files;
  }

  public int getChars(File directory, String extension, char c) {
    java.util.List files = getAllFilesInAndBelowDirectory(directory, extension);

    int count=0;
    java.util.Iterator it = files.iterator();
    while(it.hasNext()) {
      File f = (File)it.next();
      try {
        int n = getCharsInFile(f, c);
        if(logger.isDebugEnabled()) logger.debug("There are "+n+" '"+c+"' in file ["+f.getAbsolutePath()+"].");
        count += n;
      }
      catch (FileNotFoundException fnfe) {
        // strange, the files List should only contain valid Files, but perhaps directory changed since first read.
      }
    }
    return count;
  }

  private int getCharsInFile(File aFile, char c) throws FileNotFoundException {
    int count = 0;

    FileInputStream fis = new FileInputStream(aFile);

    try {
      int b;
      while ( (b = fis.read()) != -1) {
        if(b == c) count++;
      }
    }
    catch (IOException ioe) {
      System.err.println(ioe);
    }

    return count;
  }

  private static void initLog4j(boolean guiOn) {
    com.zolutions.util.log4j.Debug.initialiseLog4j();

    // start debug frame...
    if(guiOn) {
      com.zolutions.util.log4j.DebugFrame df = new com.zolutions.util.log4j.DebugFrame();
      df.startMonitoring();
      df.setVisible(true);
    }
  }

  public static void main(String[] args) throws Exception {
//    initLog4j(true);
    if(args.length != 3) {
      System.out.println("Usage::  java com.zolutions.util.codecounter.CodeCounter <directory> <file extension> <searchCharacter>");
      return;
    }
    String dir = args[0];
    String extension = args[1];
    String searchChar = args[2];

    CodeCounter codeCounter1 = CodeCounter.getInstance();

    File f = new File(dir);
    char c = searchChar.toCharArray()[0];

    codeCounter1.getChars(f, extension, c);
    System.out.println("there are "+codeCounter1.getChars(f,"java",c)+
                       " '"+c+"' characters in the java source files in and below "+
                       f.getAbsolutePath());

  }

  public static void mainTest(String[] args) throws Exception {
    initLog4j(true);
    CodeCounter codeCounter1 = CodeCounter.getInstance();
    File f = new File("C:/Projects/ZolutionUtils/src/com/zolutions/util/codecounter/CodeCounter.java");
    if(f.exists()) {
      System.out.println("semi colons in "+f.getAbsolutePath()+" = " +
          codeCounter1.getCharsInFile(f, ';'));

      f = new File("c:/projects/icc/src");
      char c = ';';
      System.out.println("there are "+codeCounter1.getChars(f,"java",c)+" '"+c+"' characters in the java source files in and below "+f.getAbsolutePath());
    }
    else {
      System.err.println("File ["+f.getAbsolutePath()+"] not found.");
    }
  }

}