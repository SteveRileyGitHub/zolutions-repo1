package com.zolutions.util.log4j;

import java.awt.*;
import javax.swing.*;

import org.apache.log4j.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;
import javax.swing.border.*;

/**
 * A user friendly front end to enable log4j logger levels to be adjusted at runtime.
 *
 * @author Steve Riley
 */

public class DebugFrame extends JFrame {

  String propertiesFilename = "debugFrame.properties";

  Properties props = new Properties();

  static Logger logger = Logger.getLogger(DebugFrame.class);

  static Object lockingObject = new Object();

  static final int MAXHEIGHT = 500;
  static final int WIDTH = 500;

  private LoggerMonitor loggerMonitor;

  private JScrollPane jScrollPane1 = new JScrollPane();
  private JPanel mainPanel = new JPanel();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JPanel jPanel1 = new JPanel();
  private JPanel blankingPanel = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private Border border1;
  private JButton buttonSave = new JButton();
  private JPanel jPanel2 = new JPanel();
  private GridBagLayout gridBagLayout3 = new GridBagLayout();
  private JRadioButton rb_debugALL = new JRadioButton();
  private JRadioButton rb_infoALL = new JRadioButton();
  private JRadioButton rb_warnALL = new JRadioButton();
  private JRadioButton rb_errorALL = new JRadioButton();
  private JRadioButton rb_fatalALL = new JRadioButton();
  private ButtonGroup buttonGroupALL = new ButtonGroup();
  private JLabel jLabel1 = new JLabel();
  private GridBagLayout gridBagLayout4 = new GridBagLayout();
  private Border border2;

  public DebugFrame() {
    File propFile = new File(this.propertiesFilename);
    try {
      if(propFile.exists()) {
        props.load(new FileInputStream(propFile));
      }
      else {
        props.store(new FileOutputStream(propFile),"DebugFrame properties.");
      }
    }
    catch (IOException ioe) {
      System.err.println(ioe);
    }
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    this.setSize(WIDTH,MAXHEIGHT);
    this.setTitle("Loggers");
    startMonitoring();
  }

  /**
   * Starts the log4j monitor, i.e. checks for new Loggers.
   */
  public void startMonitoring() {
    Thread aThread = new Thread(new LoggerMonitor(this), "loggerMonitor");
    aThread.setDaemon(true);
    aThread.start();
  }

  private void jbInit() throws Exception {
    border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(165, 163, 151));
    border2 = BorderFactory.createEmptyBorder();
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        this_componentResized(e);
      }
    });
    mainPanel.setLayout(gridBagLayout2);
    jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    this.getContentPane().setLayout(gridBagLayout1);



    mainPanel.setMinimumSize(new Dimension(500, 500));
    jPanel1.setBorder(border2);
    jPanel1.setLayout(gridBagLayout3);
    buttonSave.setMargin(new Insets(2, 2, 2, 2));
    buttonSave.setText("save settings");
    buttonSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonSave_actionPerformed(e);
      }
    });

    rb_debugALL.setText("d");
    rb_debugALL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {button_actionPerformed(e, Priority.DEBUG);}});
    rb_infoALL.setText("i");
    rb_infoALL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {button_actionPerformed(e, Priority.INFO);}});
    rb_warnALL.setText("w");
    rb_warnALL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {button_actionPerformed(e, Priority.WARN);}});
    rb_errorALL.setText("e");
    rb_errorALL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {button_actionPerformed(e, Priority.ERROR);}});
    rb_fatalALL.setText("f");
    rb_fatalALL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {button_actionPerformed(e, Priority.FATAL);}});
    jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
    jLabel1.setText("set ALL to:- ");
    jPanel2.setLayout(gridBagLayout4);
    buttonGroupALL.add(rb_debugALL);
    buttonGroupALL.add(rb_infoALL);
    buttonGroupALL.add(rb_warnALL);
    buttonGroupALL.add(rb_errorALL);
    buttonGroupALL.add(rb_fatalALL);

    this.getContentPane().add(jScrollPane1,              new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.getContentPane().add(jPanel1,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(buttonSave,         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    jPanel1.add(jPanel2,          new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
    jPanel2.add(jLabel1,      new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
    jPanel1.add(rb_debugALL,      new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jScrollPane1.getViewport().add(mainPanel, null);
    mainPanel.add(blankingPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(rb_infoALL,    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(rb_warnALL,   new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(rb_errorALL,   new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(rb_fatalALL,     new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 25), 0, 0));

  }

  /**
   * Adds a Logger to the frame.
   */
  public void addLogger(Logger l) {
    synchronized(lockingObject) {
      TreeSet loggerPanels = new TreeSet();
      java.awt.Component[] components = mainPanel.getComponents();
      // check if the logger is already in the list.
      for(int i=0;i<components.length;i++) {
        if(components[i] instanceof LoggerPanel) {
          LoggerPanel lp = (LoggerPanel)components[i];
          loggerPanels.add(lp);
          String lpName = lp.getName();
          String loggersName = l.getName();
          if(lpName.equals(loggersName)) {
            return;
          }
        }
      }

      String logName = l.getName();
      String value = props.getProperty(logName);

      if(value != null) {
        //System.out.println(logName+" has value from properties file "+value);
        if(logger.isEnabledFor(Priority.INFO)) logger.info(logName+" has value from properties file "+value);
        try {
          int logLevel = Integer.parseInt(value);
          l.setLevel(Level.toLevel(logLevel));
        }
        catch (NumberFormatException nfe) {
          System.err.println("property ["+logName+"] has been set to a non integer ["+value+"].");
        }
      } else {
      }

      LoggerPanel lp = new LoggerPanel();
      lp.setLogger(l);
      loggerPanels.add(lp);
      Iterator it = loggerPanels.iterator();
      int pos = 1;
      while (it.hasNext()) {
        lp = (LoggerPanel)it.next();
        mainPanel.add(lp,new GridBagConstraints(0, pos, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pos++;
      }
    }
    pack();
  }

  public void pack() {
    super.pack();
    limitSize();
  }

  public static void main(String[] args) {
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

    DebugFrame df = new DebugFrame();
    //df.addLogger(logger);
    //df.pack();
    df.addPanels();
    df.startMonitoring();
    df.setVisible(true);
  }

  private void limitSize() {
    if(getSize().height > MAXHEIGHT) {
      setSize(getSize().width,MAXHEIGHT);
    }
  }
  /**
   * A Test method to add several LoggerPanel's to the Frame.
   */
  private void addPanels() {
    for(int pos=1;pos<11;pos++) {
      java.awt.Component[] loggerPanels = mainPanel.getComponents();
      LoggerPanel lp = new LoggerPanel();
      lp.setLogger(logger);
      mainPanel.add(lp,new GridBagConstraints(0, pos, 1, 1, 1.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    }
  }

  void this_componentResized(ComponentEvent e) {
    //System.out.println(this.getSize());
  }

  void button_actionPerformed(ActionEvent e, Priority p) {
    java.awt.Component[] loggerPanels = mainPanel.getComponents();
    // check if the logger is already in the list.
    for(int i=0;i<loggerPanels.length;i++) {
      if(loggerPanels[i] instanceof LoggerPanel) {
        LoggerPanel lp = (LoggerPanel)loggerPanels[i];
        lp.setLevel(p);
      }
    }
  }


  /**
   * A Runnable class, that monitors the current log4j Loggers, and adds loggers as appropriate.
   */
  private class LoggerMonitor implements Runnable
  {
    DebugFrame df;

    public LoggerMonitor(DebugFrame df) {
      this.df = df;
    }

    public void run() {
      // prints loggers
 //     try {Thread.sleep(5000);} catch(InterruptedException ie){}
      while(true) {
        java.util.Enumeration enum = logger.getLoggerRepository().getCurrentLoggers();
        if(logger.isEnabledFor(Priority.DEBUG))logger.debug("===== log4j Loggers ===================================");
        while (enum.hasMoreElements()) {
          Logger aLogger = (Logger)enum.nextElement();
          df.addLogger(aLogger);
          if(logger.isEnabledFor(Priority.DEBUG))logger.debug(aLogger.getName());
        }
        if(logger.isEnabledFor(Priority.DEBUG))logger.debug("=======================================================");
        try {Thread.sleep(250);} catch(InterruptedException ie){}
      }
    }
  }

  void buttonSave_actionPerformed(ActionEvent e) {
    storeProperties();
  }

  private void storeProperties() {
    java.awt.Component[] loggerPanels = mainPanel.getComponents();
    // check if the logger is already in the list.
    for(int i=0;i<loggerPanels.length;i++) {
      if(loggerPanels[i] instanceof LoggerPanel) {
        LoggerPanel lp = (LoggerPanel)loggerPanels[i];
        String logName = lp.getName();
        Logger log = Logger.getLogger(logName);
        Level logLevel = log.getLevel();
        props.setProperty(logName,logLevel.toInt()+"");
      }
    }
    try {
      props.store(new FileOutputStream(propertiesFilename),"DebugFrame properties.");
    }
    catch (IOException ioe) {
      System.err.println(ioe);
    }
  }

  void rb_debugALL_actionPerformed(ActionEvent e) {

  }

}