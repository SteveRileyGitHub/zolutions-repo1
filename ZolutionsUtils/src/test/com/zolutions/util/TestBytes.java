package test.com.zolutions.util;

import junit.framework.*;
import com.zolutions.util.Bytes;
import java.io.*;

public class TestBytes extends TestCase {
  String testString;

  public TestBytes(String name) {
      super(name);
  }
  public void setUp() throws IOException {
    testString = new String("hello");
  }
  public void tearDown() throws IOException {
  }

  private byte[] call_StringToByteArray(String s, boolean b) {
    return Bytes.stringToByteArray(s, b);
  }
  private byte[] call_StringToByteArray(String s, int l, boolean b) {
    return Bytes.stringToByteArray(s, l, b);
  }

  /**
   * Tests that the method stringToByteArray(string, TRUE) returns a byte array
   * with a length 1 greater than the passed String.
   */
  public void testStringToByteArrayTRUE_1() {
    byte[] byteArray = call_StringToByteArray(testString, true);
    assertTrue(byteArray.length == testString.getBytes().length+1);
  }
  /**
   * Tests that the method stringToByteArray(string, TRUE) returns a terminated
   * byte array.
   */
  public void testStringToByteArrayTRUE_2() {
    byte[] byteArray = call_StringToByteArray(testString, true);
    assertTrue(byteArray[byteArray.length-1] == 0);
  }
  /**
   * Tests that the method stringToByteArray(string, TRUE) returns a byte array
   * containing the bytes of the given String in its head.
   */
  public void testStringToByteArrayTRUE_3() {
    byte[] byteArray = call_StringToByteArray(testString, true);
    boolean passed = true;
    for(int i=0;i<testString.length();i++) {
      if(!(testString.charAt(i) == (char)byteArray[i])) {
        passed = false;
        break;
      }
    }
    assertTrue(passed);
  }

  /**
   * Tests that the method stringToByteArray(string, FALSE) returns a byte array
   * of the passed String.
   */
  public void testStringToByteArrayFALSE_1() {
    byte[] byteArray = call_StringToByteArray(testString, false);
    String testStr = new String(byteArray);
    assertTrue(testStr.equals(testString));
  }

  /**
   * Tests that the method stringToByteArray(string, 10, TRUE) returns a byte array
   * with a length of 10.
   */
  public void testStringToByteArrayTRUE_ReqdLength_1() {
    byte[] byteArray = call_StringToByteArray(testString, 10, true);
    assertTrue(byteArray.length == 10);
  }
  /**
   * Tests that the method stringToByteArray(string, 10, TRUE) returns a terminated
   * byte array.
   */
  public void testStringToByteArrayTRUE_ReqdLength_2() {
    byte[] byteArray = call_StringToByteArray(testString, 10, true);
    assertTrue(byteArray[byteArray.length-1] == 0);
  }
  /**
   * Tests that the method stringToByteArray(string, 10, TRUE) returns a byte array
   * containing the bytes of the given String in its head.
   */
  public void testStringToByteArrayTRUE_ReqdLength_3() {
    byte[] byteArray = call_StringToByteArray(testString, 10, true);
    int charToTest = Math.min(byteArray.length-1, testString.length());
    boolean passed = true;
    for(int i=0;i<charToTest;i++) {
      if(!(testString.charAt(i) == (char)byteArray[i])) {
        passed = false;
        break;
      }
    }
    assertTrue(passed);
  }
  /**
   * Tests that the method stringToByteArray(string, 10, FALSE) returns a byte array
   * with a length of 10.
   */
  public void testStringToByteArrayFALSE_ReqdLength_1() {
    byte[] byteArray = call_StringToByteArray(testString, 10, false);
    assertTrue(byteArray.length == 10);
  }
  /**
   * Tests that the method stringToByteArray(string, 10, FALSE) returns a byte array
   * containing the bytes of the given String, and padded with terminators if necessary.
   */
  public void testStringToByteArrayFALSE_ReqdLength_3() {
    byte[] byteArray = call_StringToByteArray(testString, 10, false);
    String expectedString = testString;
    if(testString.length() > 10) {
      expectedString = testString.substring(0, 10);
    }
    String retStr = new String(byteArray);
    retStr = retStr.trim();
    assertTrue(retStr.equals(expectedString));
  }



  public static void main(String[] args) {
  	junit.swingui.TestRunner.run(TestBytes.class);
  //junit.textui.TestRunner.run(BytesTest.class);
  //junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {
      TestSuite suite = new TestSuite(TestBytes.class);
      return suite;
  }

}
