package com.zolutions.io;

import java.io.*;
import java.util.*;

/**
 * <P>A generic Comma-Separated Value (CSV) reader. An associated file is used to
 * construct the Reader, abd subsequent calls to readNextLine() and
 * getCurrentLineColumns() can be used to iterate through the data, line by line.
 * Each value in the line of a CSV can be delimited with a comma, space or TAB.
 * Values can quoted, and/or backslash escape characters are ignored. For example
 * the following formats will be passed correctly.</P>
 * <P>
 * 10 some street, Hampshire, UK<BR>
 * 1015 Tenth Street,\"Laurel, MD 20707\", US<BR>
 * 1846 Another Road, "Surrey", England<BR>
 * </P>
 *
 * @author  steve riley
 */
public class CSVReader {
  private String currentColumn = "";
  private StringBuffer columnBuffer = new StringBuffer();
  private List columns = new ArrayList();
  private String currentWord = "";
  private BufferedReader reader;
  private String currentLine;
  private int state = stateDelim;
  final static int stateDelim = 0;
  final static int stateInWord = 1;
  final static int stateInQuoteWord = 2;
  final static int stateQuoteInQuoteWord = 3;
  final static int stateEscapeInQuote = 4;

  /**
   * Constructs a CSVReader given the filename of the input data.
   */
  public CSVReader(String filename) throws IOException {
    if (!new File(filename).exists())
      throw new IOException();
    reader = new BufferedReader(new FileReader(filename));
    readNextLine();
  }

  /**
   * Returns true if there is another line in the CSV file.
   */
  public boolean hasNext() {
    return currentLine != null;
  }

  /**
   * Returns a List of the values is the current line.
   */
  public List next() throws IOException {
    if (currentLine == null)
      throw new IOException("Read past end of file in next()");
    parse(currentLine);
    readNextLine();
    return getCurrentLineColumns();
  }

  /**
   * Parse the given line, iterating over each column of the data.
   */
  void parse(String line) throws IOException {
    columns.clear();
    newWord();
    for (int i = 0; i < line.length(); i++)
      charEvent(line.charAt(i));
    endOfStringEvent();
  }

  /**
   * Reads the next line from the CSV file.
   */
  void readNextLine() throws IOException {
    do {
      currentLine = reader.readLine();
    }
    while (currentLine != null && isCommentLine(currentLine));
  }

  /**
   * returns the current state of the line parser.
   */
  int getState() {
    return state;
  }

  /**
   * Returns the last word parsed from the input line.
   */
  String getCurrentWord() {
    return currentWord;
  }

  /**
   * Returns the name of the current column being read.
   */
  String getCurrentColumn() {
    return columnBuffer.toString();
  }

  /**
   * Returns a List of the column headers.
   */
  List getCurrentLineColumns() {
    return columns;
  }

  /**
   * <p>Given a character, this method modifies the current state of the line
   * parse. If current state changing characters are:-</P>
   * <LI>comma</LI>
   * <LI>"</LI>
   * <LI><space></LI>
   * <LI>tab</LI>
   * <LI>\</LI>
   */
  void charEvent(char ch) {
    switch (ch) {
      case ',':
        commaEvent();
        break;
      case '"':
        doubleQuoteEvent();
        break;
      case ' ':
      case '\t':
        whitespaceEvent(ch);
        break;
      case '\\':
        backslashEvent(ch);
        break;
      default:
        defaultCharEvent(ch);
        break;
    }
  }

  /**
   * If the line parser is in the stateInQuoteWord state, the state is changed
   * to the stateEscapeInQuote state.
   */
  void backslashEvent(char ch) {
    if (state == stateInQuoteWord)
      state = stateEscapeInQuote;
    else
      defaultCharEvent(ch);
  }

  /**
   * If the current state is not in delimted (i.e. between vales, this method
   * simple passes the character to the defaultCharEvent method.
   *
   * @see #defaultCharEvent(char ch)
   */
  void whitespaceEvent(char ch) {
    if (state != stateDelim)
      defaultCharEvent(ch);
  }

  /**
       * <p>Given a comma has been read and the current state, a test is made to check
   * if the parser is currently:-</P>
   *
   * a) reading a word, (i.e. current state == stateInWord, new state = stateDelim)<BR>
   * b) between values (i.e. current state == stateDelim, state unchanged)<BR>
   * c) reading a quoted word (i.e. current state == stateInQuoteWord, state unchanged)<BR>
   * d) last character was the end of quoted word (i.e. current state == stateQuoteInQuoteWord, new state = stateDelim)<BR>
   *
   * @see #defaultCharEvent(char ch)
   */
  void commaEvent() {
    switch (state) {
      case (stateInWord):
        writeEndTrimWord();
        newWord();
        state = stateDelim;
        break;
      case (stateDelim):
        writeWord();
        break;
      case (stateInQuoteWord):
        append(',');
        break;
      case (stateQuoteInQuoteWord):
        state = stateDelim;
        break;
    }
  }

  /**
   * <p>Given a double quote has been read and the current state, a test is made to check
   * if the parser is currently:-</P>
   *
       * a) reading a word, (i.e. current state == stateInWord, state unchanged)<BR>
   * b) between values (i.e. current state == stateDelim, new state = stateInQuoteWord)<BR>
   * c) reading a quoted word (i.e. current state == stateInQuoteWord, new state = stateQuoteInQuoteWord)<BR>
   * d) last character was an escape character (i.e. current state == stateEscapeInQuote, new state = stateInQuoteWord)<BR>
   *
   * @see #defaultCharEvent(char ch)
   */
  void doubleQuoteEvent() {
    switch (state) {
      case stateDelim:
        state = stateInQuoteWord;
        break;
      case stateInWord:
        append('"');
        break;
      case stateInQuoteWord:
        state = stateQuoteInQuoteWord;
        writeWord();
        newWord();
        break;
      case stateEscapeInQuote:
        state = stateInQuoteWord;
        append('"');
        break;
    }
  }

  /**
   * <p>Appends the read character to the word (value) being read. A test is made to check
       * if the parser needs to change state, based on the current state, i.e.:-</P>
   *
   * a) between values (i.e. current state == stateDelim, new state = stateInWord)<BR>
       * b) reading a word, (i.e. current state == stateInWord, state unchanged)<BR>
   * c) reading a quoted word (i.e. current state == stateInQuoteWord, state unchanged)<BR>
   * d) read a second escape charater, and inside a quoted word (i.e. current state == stateEscapeInQuote, new state = stateInQuoteWord)<BR>
   *
   * @see #defaultCharEvent(char ch)
   */
  void defaultCharEvent(char ch) {
    switch (state) {
      case stateDelim:
        state = stateInWord;
        append(ch);
        break;
      case stateInWord:
      case stateInQuoteWord:
        append(ch);
        break;
      case stateEscapeInQuote:
        append('\\');
        append(ch);
        state = stateInQuoteWord;
        break;
    }
  }

  /**
   * Adds the word just read to the columns list, extracting the contents of the columnBuffer.
   */
  void writeWord() {
    columns.add(getCurrentColumn());
  }

  /**
   * Start forming a new word, given the contents of the columnBuffer so far.
   */
  void newWord() {
    columnBuffer.delete(0, columnBuffer.length());
  }

  /**
   * Appends a read character to the word.
   */
  void append(char ch) {
    columnBuffer.append(ch);
  }

  /**
   * Given that the end of the line has been read, this method determines whether
   * a correctly formed line has been read.
   */
  void endOfStringEvent() throws IOException {
    switch (state) {
      case stateQuoteInQuoteWord:
        break;
      case stateInWord:
        writeEndTrimWord();
        break;
      case stateInQuoteWord:
        throw new IOException(
            "Badly formed record: quoted string not terminated");
      default:
        writeWord();
        break;
    }
  }

  void writeEndTrimWord() {
    columns.add(endTrim(getCurrentColumn()));
  }

  /**
   * Trims spaces and tabs off the end of the given String.
   */
  String endTrim(String source) {
    int i = source.length() - 1;
    while (i > -1) {
      if (isWhitespace(source.charAt(i)))
        i--;
      else
        break;
    }
    return source.substring(0, i + 1);
  }

  /**
   * Returns true if the passed character is a SPACE or TAB.
   */
  boolean isWhitespace(char ch) {
    return ch == ' ' || ch == '\t';
  }

  /**
   * Returns true if the passed String starts with a #.
   */
  boolean isCommentLine(String line) {
    if (line.length() == 0)
      return false;
    return line.charAt(0) == '#';
  }
}