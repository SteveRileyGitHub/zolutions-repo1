package com.zolutions.util;

/**
 * This class contains various methods for manipulating bytes, and converting scalars to byte
 * arrays (useful for writing to sockets!).
 * @author S J RILEY - Zolutions Ltd.
 * @version 1.0
 */
public final class Bytes {

  // byte shifts
  private static int SHIFT_0 = 8 * 0;
  private static int SHIFT_1 = 8 * 1;
  private static int SHIFT_2 = 8 * 2;
  private static int SHIFT_3 = 8 * 3;
  private static int SHIFT_4 = 8 * 4;
  private static int SHIFT_5 = 8 * 5;
  private static int SHIFT_6 = 8 * 6;
  private static int SHIFT_7 = 8 * 7;

  // Byte masks
  private static final byte B_1 = 0x1;
  private static final byte B_2 = 0x2;
  private static final byte B_3 = 0x4;
  private static final byte B_4 = 0x8;
  private static final byte B_5 = 0x10;
  private static final byte B_6 = 0x20;
  private static final byte B_7 = 0x40;
  private static final byte B_8 = (byte) 0x80;

  private static byte STRING_TERMINATOR = '\0';
  //private static byte STRING_TERMINATOR = B_8;

  /**
   * returns a byte array (size=8) given a double (ie eight byte number)
   *
   * @param value double
   * @return byte[]
   */
  public static byte[] toByteArray(double value) {
    long eightByteWord = Double.doubleToLongBits(value);
    return toByteArray(eightByteWord);
  }

  /**
   * Reads four input bytes and returns an int value. Let a be the first byte
   * read, b be the second byte, c be the third byte, and d be the fourth byte.
   * The value returned is: <pre>
   *  (((a & 0xff) << 24) |
   *   ((b & 0xff) << 16) |
   *   ((c & 0xff) << 8) |
   *   (d & 0xff))
   *  </pre>
   *
   * @param byteArray byte[]
   * @return int
   */
  public static int toInt(byte[] byteArray) {
    return ( ( (byteArray[0] & 0xff) << SHIFT_3) |
            ( (byteArray[1] & 0xff) << SHIFT_2) |
            ( (byteArray[2] & 0xff) << SHIFT_1) |
            (byteArray[3] & 0xff)
            );
  }

  /**
   * Reads two input bytes and returns an short value. Let a be the first byte
   * read and b be the second byte. The value returned is: <pre>
   *   ((a & 0xff) << 8) |
   *   (b & 0xff))
   *  </pre>
   *
   * @param byteArray byte[]
   * @return short
   */
  public static short toShort(byte[] byteArray) {
    return (short) ( ( (byteArray[0] & 0xff) << SHIFT_1) |
                    (byteArray[1] & 0xff)
                    );
  }

  /**
   * Reads eight input bytes and returns a long value. Let a be the first byte
   * read, b be the second byte, c be the third byte, d be the fourth byte, e be
   * the fifth byte, f be the sixth byte, g be the seventh byte, and h be the
   * eighth byte. The value returned is: <pre>
   *  (((long)(a & 0xff) << 56) |
   *   ((long)(b & 0xff) << 48) |
   *   ((long)(c & 0xff) << 40) |
   *   ((long)(d & 0xff) << 32) |
   *   ((long)(e & 0xff) << 24) |
   *   ((long)(f & 0xff) << 16) |
   *   ((long)(g & 0xff) <<  8) |
   *   ((long)(h & 0xff)))
   *  </pre>
   *
   * @param eightByteArray byte[]
   * @return long
   */
  public static long toLong(byte[] eightByteArray) {
    return ( ( (long) (eightByteArray[0] & 0xff) << SHIFT_7) |
            ( (long) (eightByteArray[1] & 0xff) << SHIFT_6) |
            ( (long) (eightByteArray[2] & 0xff) << SHIFT_5) |
            ( (long) (eightByteArray[3] & 0xff) << SHIFT_4) |
            ( (long) (eightByteArray[4] & 0xff) << SHIFT_3) |
            ( (long) (eightByteArray[5] & 0xff) << SHIFT_2) |
            ( (long) (eightByteArray[6] & 0xff) << SHIFT_1) |
            ( (long) (eightByteArray[7] & 0xff)));
  }

  /**
   * Reads eight input bytes and returns a double value. It does this by first
   * constructing a long value in exactly the manner of the toLong method, then
   * converting this long value to a double in exactly the manner of the method
   * Double.longBitsToDouble.
   *
   * @param eightByteArray byte[]
   * @return double
   */
  public static double toDouble(byte[] eightByteArray) {
    return Double.longBitsToDouble(toLong(eightByteArray));
  }

  /**
   * Reads eight input bytes and returns a float value. It does this by first
   * constructing a long value in exactly the manner of the toLong method, then
   * converting this long value to a float in exactly the manner of the method
   * Float.intBitsToFloat.
   *
   * @param eightByteArray byte[]
   * @return float
   */
  public static float toFloat(byte[] eightByteArray) {
    return Float.intBitsToFloat(toInt(eightByteArray));
  }

  /**
   * returns a byte array (size=8) given a long (ie eight byte number)
   *
   * @param value long
   * @return byte[]
   */
  public static byte[] toByteArray(long value) {
    byte[] bytes = new byte[8];
    bytes[0] = (byte) ( (value & 0xFF00000000000000L) >> SHIFT_7);
    bytes[1] = (byte) ( (value & 0x00FF000000000000L) >> SHIFT_6);
    bytes[2] = (byte) ( (value & 0x0000FF0000000000L) >> SHIFT_5);
    bytes[3] = (byte) ( (value & 0x000000FF00000000L) >> SHIFT_4);
    bytes[4] = (byte) ( (value & 0x00000000FF000000L) >> SHIFT_3);
    bytes[5] = (byte) ( (value & 0x0000000000FF0000L) >> SHIFT_2);
    bytes[6] = (byte) ( (value & 0x000000000000FF00L) >> SHIFT_1);
    bytes[7] = (byte) ( (value & 0x00000000000000FFL) >> SHIFT_0);
    return bytes;
  }

  /**
   * returns a byte array (size=4) given an int (ie four byte number)
   *
   * @param intValue int
   * @return byte[]
   */
  public static byte[] toByteArray(int intValue) {
    byte[] bytes = new byte[4];
    bytes[0] = (byte) ( (intValue & 0xFF000000) >> SHIFT_3);
    bytes[1] = (byte) ( (intValue & 0x00FF0000) >> SHIFT_2);
    bytes[2] = (byte) ( (intValue & 0x0000FF00) >> SHIFT_1);
    bytes[3] = (byte) ( (intValue & 0x000000FF) >> SHIFT_0);
    return bytes;
  }

  /**
   * returns a byte array (size=2) given a short (ie two byte number)
   *
   * @param twoByteWord short
   * @return byte[]
   */
  public static byte[] toByteArray(short twoByteWord) {
    byte[] bytes = new byte[2];
    bytes[0] = (byte) ( (twoByteWord & 0xFF00) >> SHIFT_1);
    bytes[1] = (byte) ( (twoByteWord & 0x00FF) >> SHIFT_0);
    return bytes;
  }

  /**
   * returns a byte array (size=4) given a float (ie four byte number)
   *
   * @param fourByteWord float
   * @return byte[]
   */
  public static byte[] toByteArray(float fourByteWord) {
    int intBits = Float.floatToIntBits(fourByteWord);
    return toByteArray(intBits);
  }

  /**
   * returns the resulting byte array of appending <code>tailArray</code> to the
   * end of <code>headArray</code>. The returned byte array's size is therefore
   * the sum of the two.
   *
   * @param headArray byte[]
   * @param tailArray byte[]
   * @return byte[]
   */
  public static byte[] appendByteArray(byte[] headArray, byte[] tailArray) {
    int size = headArray.length + tailArray.length;
    byte[] byteArray = new byte[size];
    for (int i = 0; i < headArray.length; i++) {
      byteArray[i] = headArray[i];
    }
    for (int i = headArray.length; i < size; i++) {
      int index = i - size + tailArray.length;
      byteArray[i] = tailArray[index];
    }
    return byteArray;
  }

  /**
   * Converts a String to a byte[] with reqdSize specified, a terminator
   * character at the end if terminate is TRUE. Note: if the string is longer
   * than the reqdSize, the string will be truncated.
   *
   * @param string String
   * @param reqdSize int
   * @param terminate boolean
   * @return byte[]
   */
  public static byte[] stringToByteArray(String string, int reqdSize,
                                         boolean terminate) {
    byte[] byteArray = new byte[reqdSize];
    byte[] stringBytes = string.getBytes();

    if (terminate) {
      int indexEnd = ( (byteArray.length - 1) < stringBytes.length) ?
          byteArray.length - 1 : stringBytes.length;
      for (int i = 0; i < indexEnd; i++) {
        byteArray[i] = stringBytes[i];
      }
      byteArray[indexEnd] = STRING_TERMINATOR;
    }
    else {
      int indexEnd = Math.min(byteArray.length, stringBytes.length);
      for (int i = 0; i < indexEnd; i++) {
        byteArray[i] = stringBytes[i];
      }
    }
    return byteArray;
  }

  /**
   * Converts a String to a byte[]. If terminate is TRUE the size of the byte
   * array is one greater than the string.length() as a STRING_TERMINATOR is
   * added.
   *
   * @param string String
   * @param terminate boolean
   * @return byte[]
   */
  public static byte[] stringToByteArray(String string, boolean terminate) {
    byte[] stringBytes = string.getBytes();
    byte[] result = null;
    if (terminate) {
      // create byte[] one greater to add a terminator.
      result = new byte[string.length() + 1];
      System.arraycopy(stringBytes,0,result,0,result.length-1);
      result[stringBytes.length] = STRING_TERMINATOR;
    }
    else {
      result = stringBytes;
    }
    return result;
  }

  /**
   * Converts a byte to a String representation, for example 0x96 to ->
   * "10010110".
   *
   * @param b byte
   * @return String
   */
  public static String byteToString(byte b) {
    StringBuffer strBuf = new StringBuffer(8);
    int i = (int) b;
    int b1 = (int) ( (i & B_8) >> 7);
    int b2 = (int) ( (i & B_7) >> 6);
    int b3 = (int) ( (i & B_6) >> 5);
    int b4 = (int) ( (i & B_5) >> 4);
    int b5 = (int) ( (i & B_4) >> 3);
    int b6 = (int) ( (i & B_3) >> 2);
    int b7 = (int) ( (i & B_2) >> 1);
    int b8 = (int) ( (i & B_1) >> 0);

    strBuf.append(unsigned(b1));
    strBuf.append(unsigned(b2));
    strBuf.append(unsigned(b3));
    strBuf.append(unsigned(b4));
    strBuf.append(unsigned(b5));
    strBuf.append(unsigned(b6));
    strBuf.append(unsigned(b7));
    strBuf.append(unsigned(b8));
    return new String(strBuf);
  }

  /**
   * Returns an unsigned version of the geiven int.
   *
   * @param i int
   * @return int
   */
  private static int unsigned(int i) {
    int v = (i > 0) ? i : i * -1;
    return (i > 0) ? i : i * -1;
  }

  /**
   * Converts a byte to a String representation, each byte separated with a
   * white space; for example 0x9696 to -> "10010110 10010110".
   *
   * @param b byte[]
   * @return String
   */
  public static String byteArrayToString(byte[] b) {
    StringBuffer buf = new StringBuffer(b.length * 8);
    int i;
    for (i = 0; i < b.length - 1; i++) {
      buf.append(byteToString(b[i]) + " ");
    }
    buf.append(byteToString(b[i]));
    return new String(buf);
  }

  /**
   * Conducts a Byte swap of the given double; useful for Big to Little Endian
   * (and vica versa) conversion.
   *
   * @param val double
   * @return double
   */
  public static double swap(double val) {
    return swapEightBytes(val);
  }

  /**
   * Conducts a Byte swap of the given long; useful for Big to Little Endian
   * (and vica versa) conversion.
   *
   * @param val long
   * @return long
   */
  public static long swap(long val) {
    return swapEightBytes(val);
  }

  /**
   * Conducts a Byte swap of the given short; useful for Big to Little Endian
   * (and vica versa) conversion.
   *
   * @param val short
   * @return short
   */
  public static short swap(short val) {
    return swapTwoBytes(val);
  }

  /**
   * Conducts a Byte swap of the given float; useful for Big to Little Endian
   * (and vica versa) conversion.
   *
   * @param val float
   * @return float
   */
  public static float swap(float val) {
    return swapFourBytes(val);
  }

  /**
   * Conducts a Byte swap of the given int; useful for Big to Little Endian (and
   * vica versa) conversion.
   *
   * @param val int
   * @return int
   */
  public static int swap(int val) {
    return swapFourBytes(val);
  }

  /**
   * returns a "byte swapped" <B>short</B>; ie given 2-bytes (12) returns (21).
   *
   * @param twoByteWord short
   * @return short
   */
  private static short swapTwoBytes(short twoByteWord) {
    return (short) ( ( (twoByteWord & 0xFF00) >>> SHIFT_1) |
                    ( (twoByteWord & 0x00FF) << SHIFT_1));
  }

  /**
   * returns a "byte swapped" <B>int</B>; ie given 4-bytes (1234) returns (4321).
   *
   * @param fourByteWord int
   * @return int
   */
  private static int swapFourBytes(int fourByteWord) {
    return (int) ( ( (fourByteWord & 0xFF000000) >>> SHIFT_3) |
                  ( (fourByteWord & 0x00FF0000) >>> SHIFT_1) |
                  ( (fourByteWord & 0x0000FF00) << SHIFT_1) |
                  ( (fourByteWord & 0x000000FF) << SHIFT_3));
  }

  /**
   * swapFourBytes
   *
   * @param fourByteWord float
   * @return float
   */
  private static float swapFourBytes(float fourByteWord) {
    int floatAsInt = Float.floatToIntBits(fourByteWord);
    int swappedFloat = swapFourBytes(floatAsInt);
    return Float.intBitsToFloat(swappedFloat);
  }

  /**
   * returns a "byte swapped" <B>long</B>; ie given 8-bytes (12345678) returns
   * (87654321).
   *
   * @param eightByteWord long
   * @return long
   */
  private static long swapEightBytes(long eightByteWord) {
    return (long) ( ( (eightByteWord & 0xFF00000000000000L) >>> SHIFT_7) |
                   ( (eightByteWord & 0x00FF000000000000L) >>> SHIFT_5) |
                   ( (eightByteWord & 0x0000FF0000000000L) >>> SHIFT_3) |
                   ( (eightByteWord & 0x000000FF00000000L) >>> SHIFT_1) |
                   ( (eightByteWord & 0x00000000FF000000L) << SHIFT_1) |
                   ( (eightByteWord & 0x0000000000FF0000L) << SHIFT_3) |
                   ( (eightByteWord & 0x000000000000FF00L) << SHIFT_5) |
                   ( (eightByteWord & 0x00000000000000FFL) << SHIFT_7));
  }

  /**
   * returns a "byte swapped" <B>double</B>.
   *
   * @param eightByteWord double
   * @return double
   */
  private static double swapEightBytes(double eightByteWord) {
    long doubleAsLong = Double.doubleToLongBits(eightByteWord);
    long swappedLong = swapEightBytes(doubleAsLong);
    return Double.longBitsToDouble(swappedLong);
  }

  /**
   * main
   *
   * @param args String[]
   */
  public static void main(String[] args) {

    short s = 9934;
    System.out.println("s=" + s);
    byte[] s_asBytes = Bytes.toByteArray(s);
    System.out.println(Bytes.byteArrayToString(s_asBytes));
    short s2 = Bytes.toShort(s_asBytes);
    System.out.println("s2=" + s2);

    System.out.println(Bytes.byteArrayToString(Bytes.toByteArray(Bytes.swap(s))));

    double d_bigEndian = 3.435735;
    double d_littleEndian = Bytes.swap(d_bigEndian);
    System.out.println(d_littleEndian);

    System.out.println(Bytes.swap(d_littleEndian));

  }

  /**
   * main_1
   *
   * @param args String[]
   */
  public static void main_1(String[] args) {
    byte[] byteArray;
    String spc = "        ";

    // Test - 1
    byteArray = stringToByteArray("hello", true);
    System.out.println("h" + spc + "e" + spc + "l" + spc + "l" + spc + "o" +
                       spc + "\0");
    for (int i = 0; i < byteArray.length; i++) {
      System.out.print(byteToString(byteArray[i]) + " ");
    }
    System.out.println("");

    // Test - 2
    byteArray = stringToByteArray("hello", 10, true);
    System.out.println("h" + spc + "e" + spc + "l" + spc + "l" + spc + "o" +
                       spc + "\0" + spc + "0" + spc + "0" + spc + "0" + spc +
                       "0");
    for (int i = 0; i < byteArray.length; i++) {
      System.out.print(byteToString(byteArray[i]) + " ");
    }
    System.out.println("");

    // Test - 3
    byteArray = stringToByteArray("hello", false);
    System.out.println("h" + spc + "e" + spc + "l" + spc + "l" + spc + "o");
    for (int i = 0; i < byteArray.length; i++) {
      System.out.print(byteToString(byteArray[i]) + " ");
    }
    System.out.println("");

    // Test - 4
    byteArray = stringToByteArray("hello", 5, false);
    System.out.println("h" + spc + "e" + spc + "l" + spc + "l" + spc + "o");
    for (int i = 0; i < byteArray.length; i++) {
      System.out.print(byteToString(byteArray[i]) + " ");
    }
    System.out.println("");

    // Test - 5
    byteArray = stringToByteArray("hello", 10, false);
    System.out.println("h" + spc + "e" + spc + "l" + spc + "l" + spc + "o" +
                       spc + "0" + spc + "0" + spc + "0" + spc + "0" + spc +
                       "0");
    for (int i = 0; i < byteArray.length; i++) {
      System.out.print(byteToString(byteArray[i]) + " ");
    }
    System.out.println("");

    // Test - 6
    byteArray = stringToByteArray("hello", 3, false);
    System.out.println("h" + spc + "e" + spc + "l");
    for (int i = 0; i < byteArray.length; i++) {
      System.out.print(byteToString(byteArray[i]) + " ");
    }
    System.out.println("");

    // Test - 7
    byteArray = stringToByteArray("hel", 3, true);
    System.out.println("h" + spc + "e" + spc + "\0");
    for (int i = 0; i < byteArray.length; i++) {
      System.out.print(byteToString(byteArray[i]) + " ");
    }
    System.out.println("");

    // Test - 8
    byteArray = stringToByteArray("hello", 7, true);
    System.out.println("h" + spc + "e" + spc + "l" + spc + "l" + spc + "o" +
                       spc + "\0" + spc + "0");
    for (int i = 0; i < byteArray.length; i++) {
      System.out.print(byteToString(byteArray[i]) + " ");
    }
    System.out.println("");

    long myLong = 0xFFFF;
    byte[] myLongBytes = toByteArray(myLong);
    long myReturnedLong = toLong(myLongBytes);
    System.out.println("converted " + myLong + " to byteArray..\n" +
                       byteArrayToString(myLongBytes) + "\n" +
                       "which converted back to " + myReturnedLong + "\n" +
                       "or as byte array...\n" +
                       byteArrayToString(toByteArray(myReturnedLong)));
  }

}
