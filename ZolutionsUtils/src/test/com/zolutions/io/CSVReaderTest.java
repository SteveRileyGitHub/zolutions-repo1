package test.com.zolutions.io;
/*
 * CSVReaderTest.java
 * JUnit based test
 *
 * Created on 06 January 2003, 09:47
 */
import com.zolutions.io.CSVReader;
import junit.framework.*;

import java.io.*;
import java.util.*;

/**
 *
 * @author steve
 */
public class CSVReaderTest extends TestCase {
    private String filename;
    private BufferedWriter writer;

    public CSVReaderTest(String name) {
        super(name);
    }
    public void setUp() throws IOException {
        filename = "CSVReaderTest.tmp.csv";
        writer = new BufferedWriter(new FileWriter(filename));
    }
    public void tearDown() throws IOException {
        new File(filename).delete();
    }
    public void testCreate() throws IOException {
        CSVReader reader = getReaderAndCloseWriter();
        assertTrue(!reader.hasNext());
    }
    public void testSingleRecordSingleField() throws IOException {
        writeln("test");
        CSVReader reader = getReaderAndCloseWriter();
        assertTrue(reader.hasNext());
        List columns = reader.next();
        assertEquals(1, columns.size());
        assertEquals("test", columns.get(0));
    }
    public void testSingleFieldMultipleColumns() throws IOException {
        writeln("test2col1, test2col2");
        CSVReader reader = getReaderAndCloseWriter();;
        assertTrue(reader.hasNext());
        List columns = reader.next();
        assertEquals(2, columns.size());
        assertEquals("test2col1", columns.get(0));
        assertEquals("test2col2", columns.get(1));
    }
    public void testEOF() throws IOException {
        writeln("line1");
        CSVReader reader = getReaderAndCloseWriter();
        reader.next();
        try {
            reader.next();
            fail("expected exception here");
        }
        catch (IOException e) {
            pass();
        }
    }
    public void testMultipleLines() throws IOException {
        writeln("line1");
        writeln("line2,line2");
        writeln("line3");
        CSVReader reader = getReaderAndCloseWriter();
        assertEquals(1, reader.next().size());
        assertEquals(2, reader.next().size());
        assertEquals(1, reader.next().size());
        assertTrue(!reader.hasNext());
    }
    public void testComment() throws IOException {
        writeln("line1 data");
        writeln("# this is a comment");
        String line3data = "line3,some,data,columns";
        writeln(line3data);
        CSVReader reader = getReaderAndCloseWriter();
        reader.next();
        List line3Columns = reader.next();
        assertEquals("line3", line3Columns.get(0));
        assertTrue(!reader.hasNext());
    }
    public void testMoreComments() throws IOException {
        writeln("# .... ok");
        writeln("# .... well?");
        CSVReader reader = getReaderAndCloseWriter();
        assertTrue(!reader.hasNext());
    }
    public void testDoubleQuotedData() throws IOException {
        writeln("1015 Tenth Street,\"Laurel, MD 20707\", US");
        CSVReader reader = getReaderAndCloseWriter();
        List columns = reader.next();
        assertEquals(3, columns.size());
        assertEquals("1015 Tenth Street", columns.get(0));
        assertEquals("Laurel, MD 20707", columns.get(1));
        assertEquals("US", columns.get(2));
    }
    public void testMoreThanOneCommaInDoubleQuotedData() throws IOException {
        writeln("1015 Tenth Street,\"Laurel, MD 20707, US\"");
        CSVReader reader = getReaderAndCloseWriter();
        List columns = reader.next();
        assertEquals(2, columns.size());
        assertEquals("1015 Tenth Street", columns.get(0));
        assertEquals("Laurel, MD 20707, US", columns.get(1));
    }
    public void testEmbeddedQuotesArePartOfString() throws IOException {
        writeln("1015 Tenth Street, Jeff \"AC\" Langr, 1964");
        CSVReader reader = getReaderAndCloseWriter();
        List columns = reader.next();
        assertEquals(3, columns.size());
        assertEquals("1015 Tenth Street", columns.get(0));
        assertEquals("Jeff \"AC\" Langr", columns.get(1));
        assertEquals("1964", columns.get(2));
    }
    public void testSingleEmbeddedDoubleQuoteIsPartOfString() throws IOException {
        writeln("normally you wouldn\"t do this");
        CSVReader reader = getReaderAndCloseWriter();
        List columns = reader.next();
        assertEquals(1, columns.size());
        assertEquals("normally you wouldn\"t do this", columns.get(0));
    }
    public void testUnmatchedDoubleQuoteIsAnError() throws IOException {
        writeln("\"jkl");
        CSVReader reader = getReaderAndCloseWriter();
        try {
            reader.next();
            fail("should have thrown IO exception");
        }
        catch (IOException e) {
            pass();
        }
    }
    public void testEmptyFields() throws IOException {
        writeln("");
        writeln(",");
        writeln(",a,,,");
        CSVReader reader = getReaderAndCloseWriter();
        List columns = reader.next();
        assertEquals(1, columns.size());
        assertEquals("", columns.get(0));
        columns = reader.next();
        assertEquals(2, columns.size());
        columns = reader.next();
        assertEquals(5, columns.size());
    }
    public void testTrim() throws IOException {
        writeln(
        " this , is ,\tthe end\t, \t of , it, all ");
        CSVReader reader = getReaderAndCloseWriter();
        List columns = reader.next();
        assertEquals(6, columns.size());
        assertEquals("this", columns.get(0));
        assertEquals("is", columns.get(1));
        assertEquals("the end", columns.get(2));
        assertEquals("of", columns.get(3));
        assertEquals("it", columns.get(4));
        assertEquals("all", columns.get(5));
    }
    public void testNotTrim() throws IOException {
        writeln("\" this should be flanked by white space \"");
        CSVReader reader = getReaderAndCloseWriter();
        List columns = reader.next();
        assertEquals(1, columns.size());
        assertEquals(
        " this should be flanked by white space ", columns.get(0));
    }
    int writeln(String string) throws IOException {
        writer.write(string, 0, string.length());
        writer.write("\r\n", 0, 2);
        return string.length() + 2;
    }
    CSVReader getReaderAndCloseWriter() throws IOException {
        writer.close();
        return new CSVReader(filename);
    }
    final void pass() {}

    public static void main(String[] args) {
    junit.swingui.TestRunner.run(CSVReaderTest.class);
    //junit.textui.TestRunner.run(CSVReaderTest.class);
    //junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CSVReaderTest.class);
        return suite;
    }

}
