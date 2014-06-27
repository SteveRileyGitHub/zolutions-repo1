package com.zolutions.util.log4j;

import javax.swing.*;
import java.awt.*;

import org.apache.log4j.*;
import java.awt.event.*;

public class LoggerPanel extends JPanel implements Comparable {

  static Logger logger = Logger.getLogger(LoggerPanel.class);

  static final Dimension rbSize = new Dimension(32, 17);

  private Logger associatedLogger = null;

  private JLabel loggerLabel = new JLabel();
  private JRadioButton rb_debug = new JRadioButton();
  private JRadioButton rb_info = new JRadioButton();
  private JRadioButton rb_warn = new JRadioButton();
  private JRadioButton rb_error = new JRadioButton();
  private JRadioButton rb_fatal = new JRadioButton();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private ButtonGroup buttonGroup = new ButtonGroup();

  public LoggerPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the name of the associated logger.
   */
  public String getName() {
    return associatedLogger.getName();
  }

  public void setLogger(Logger l) {
    associatedLogger = l;
    loggerLabel.setText(l.getName());
    setLevel(l.getEffectiveLevel());
  }

  public void setLevel(Priority priority) {
    Level l;
    switch(priority.toInt()) {
      case Priority.DEBUG_INT:
        rb_debug.setSelected(true);
        l = Level.DEBUG;
        break;
      case Priority.INFO_INT:
        rb_info.setSelected(true);
        l = Level.INFO;
        break;
      case Priority.WARN_INT:
        rb_warn.setSelected(true);
        l = Level.WARN;
        break;
      case Priority.ERROR_INT:
        rb_error.setSelected(true);
        l = Level.ERROR;
        break;
      case Priority.FATAL_INT:
        rb_fatal.setSelected(true);
        l = Level.FATAL;
        break;
        default:
          l = Level.DEBUG;
          rb_debug.setSelected(true);
    }
    setAssociatedLoggersLevel(l);
  }

  private void jbInit() throws Exception {
    loggerLabel.setText("logger name");
    rb_debug.setText("d");
    rb_debug.addActionListener(new java.awt.event.ActionListener() {public void actionPerformed(ActionEvent e) {debug_actionPerformed(e,Level.DEBUG);}});
    rb_debug.setPreferredSize(rbSize);
    rb_debug.setFocusPainted(false);

    rb_info.setText("i");
    rb_info.addActionListener(new java.awt.event.ActionListener() {public void actionPerformed(ActionEvent e) {debug_actionPerformed(e,Level.INFO);}});
    rb_info.setPreferredSize(rbSize);
    rb_info.setFocusPainted(false);

    rb_warn.setText("w");
    rb_warn.addActionListener(new java.awt.event.ActionListener() {public void actionPerformed(ActionEvent e) {debug_actionPerformed(e,Level.WARN);}});
    rb_warn.setPreferredSize(rbSize);
    rb_warn.setFocusPainted(false);

    rb_error.setText("e");
    rb_error.addActionListener(new java.awt.event.ActionListener() {public void actionPerformed(ActionEvent e) {debug_actionPerformed(e,Level.ERROR);}});
    rb_error.setPreferredSize(rbSize);
    rb_error.setFocusPainted(false);

    rb_fatal.setText("f");
    rb_fatal.addActionListener(new java.awt.event.ActionListener() {public void actionPerformed(ActionEvent e) {debug_actionPerformed(e,Level.FATAL);}});
    rb_fatal.setPreferredSize(rbSize);
    rb_fatal.setFocusPainted(false);
    buttonGroup.add(rb_debug);
    buttonGroup.add(rb_info);
    buttonGroup.add(rb_warn);
    buttonGroup.add(rb_error);
    buttonGroup.add(rb_fatal);
    this.setLayout(gridBagLayout1);
    this.add(loggerLabel,     new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
    this.add(rb_debug,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(rb_info,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(rb_warn,   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(rb_error,   new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(rb_fatal,   new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 4), 0, 0));
  }

  public static void main(String[] args) {
    PropertyConfigurator.configure(ClassLoader.getSystemResource("log4j_config.properties").getPath());
    JFrame f = new JFrame();
    LoggerPanel lp = new LoggerPanel();
    lp.setLogger(logger);
    f.getContentPane().add(lp);
    f.pack();
    f.setVisible(true);
  }

  private void setAssociatedLoggersLevel(Level l) {
    if (logger.isEnabledFor(Priority.INFO))
      logger.info("Setting logger "+this.associatedLogger.getName()+" to level "+l);
    associatedLogger.setLevel(l);

  }

  void debug_actionPerformed(ActionEvent e, Level l) {
    setAssociatedLoggersLevel(l);
  }

  public void setBackground(Color color) {
    super.setBackground(color);
    Component[] components = this.getComponents();
    for(int i=0;i<components.length;i++) {
      components[i].setBackground(color);
    }
  }

  public int compareTo(Object o) {
    if(o instanceof LoggerPanel) {
      LoggerPanel lp = (LoggerPanel)o;
      return this.getName().compareTo(lp.getName());
    }
    else {
      throw new ClassCastException("Cannot compare "+this+" to "+o);
    }
  }
}