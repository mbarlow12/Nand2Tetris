/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VMTranslator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael Barlow
 */
public class ParserTest
{
    
    public static Parser testParser;
    
    public ParserTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        try {
            File file = new File("test.vm");
            testParser = new Parser(file);
            testParser.mark();
            System.out.println("parser successfully set up");
        } catch (FileNotFoundException fnf) {
            System.out.println("file error");
            System.out.println(fnf.getMessage());
            System.exit(101);
        } catch (IOException ioe) {
            System.out.println("input/output error");
            System.out.println(ioe.getMessage());
            System.exit(102);
        }
    }
    
    @AfterClass
    public static void tearDownClass()
    {
        try {
            testParser.close();
        } catch (IOException ex) {
            Logger.getLogger(ParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
        try {
            testParser.reset();
        } catch (IOException ioe) {
            System.out.println("Couldn't reset the reader.");
            System.exit(103);
        }
    }

    /**
     * Test of hasMoreCommands method, of class Parser.
     * @throws java.io.IOException
     */
    @Test
    public void testHasMoreCommandsFalse() throws IOException
    {
        System.out.println("hasMoreCommandsFalse");
        
        while (testParser.hasMoreCommands()) {
            testParser.advance();
        }
        
        assertFalse(testParser.hasMoreCommands());
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of hasMoreCommands method, of class Parser.
     * @throws java.io.IOException
     */
    @Test
    public void testHasMoreCommandsTrue() throws IOException
    {
        System.out.println("hasMoreCommands");
        testParser.advance();
        assertTrue(testParser.hasMoreCommands());
    }

    /**
     * Test of advance method, of class Parser.
     */
    @Test
    public void testAdvance() throws IOException
    {
        System.out.println("advance");
        testParser.advance();
        String command = testParser.getCurrentCommand();
        assertEquals(command, "push constant 7");
    }

    /**
     * Test of commandType method, of class Parser.
     * @throws java.io.IOException
     */
    @Test
    public void testCommandTypePush() throws IOException
    {
        System.out.println("commandType");
        testParser.advance();
        assertEquals("C_PUSH", testParser.commandType());
        
    }

    /**
     * Test of commandType method, of class Parser.
     * @throws java.io.IOException
     */
    @Test
    public void testCommandTypePop() throws IOException
    {
        System.out.println("commandType");
        testParser.advance();
        testParser.advance();
        assertEquals("C_POP", testParser.commandType());
        
    }
    

    /**
     * Test of commandType method, of class Parser.
     * @throws java.io.IOException
     */
    @Test
    public void testCommandTypeArithmetic() throws IOException
    {
        System.out.println("commandType");
        testParser.advance();
        testParser.advance();
        testParser.advance();
        testParser.advance();
        assertEquals("C_ARITHMETIC", testParser.commandType());
        
    }

    /**
     * Test of arg1 method, of class Parser.
     * @throws java.io.IOException
     */
    @Test
    public void testArg1() throws IOException
    {
        System.out.println("arg1");
        testParser.advance();
        assertEquals("constant", testParser.arg1());
    }

    /**
     * Test of arg2 method, of class Parser.
     * @throws java.io.IOException
     */
    @Test
    public void testArg2() throws IOException
    {
        System.out.println("arg2");
        testParser.advance();
        assertEquals(7, testParser.arg2());
    }

    /**
     * Test of arg2 method, of class Parser.
     * @throws java.io.IOException
     */
    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void testArg2Empty() throws IOException
    {
        System.out.println("arg2");
        testParser.advance();
        testParser.advance();
        testParser.advance();
        testParser.advance();
        assertEquals(7, testParser.arg2());
    }
}
