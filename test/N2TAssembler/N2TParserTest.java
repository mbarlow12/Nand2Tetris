/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package N2TAssembler;

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
public class N2TParserTest
{
    // create dummy .asm file for testing with all types of commands (excluding labels)
    // above is already created in N2T project folder
    
    public static N2TParser testParser;
    
    public N2TParserTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        try {
            testParser = new N2TParser("Max.asm");
            testParser.mark();
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
            Logger.getLogger(N2TParserTest.class.getName()).log(Level.SEVERE, null, ex);
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
     * Test of hasMoreCommands method, of class N2TParser.
     */
    @Test
    public void testHasMoreCommands() throws IOException
    {
        System.out.println("hasMoreCommands");
        testParser.advance();
        assertEquals(true, testParser.hasMoreCommands());
    }

    /**
     * Test of advance method, of class N2TParser.
     */
    @Test
    public void testAdvance() throws IOException
    {
        System.out.println("advance");
        testParser.advance();
        String str1 = testParser.getCurrentCommand();
        testParser.advance();
        String str2 = testParser.getCurrentCommand();
        assertNotEquals(str1, str2);
    }

    /**
     * Test of commandType method, of class N2TParser.
     */
    @Test
    public void testCommandTypeA()
    {
        System.out.println("commandTypeA");
        try {
            do {
                testParser.advance();
                System.out.println(testParser.getCurrentCommand());
            } while (!(testParser.getCurrentCommand().startsWith("@")));
        } catch (IOException ex) {
            Logger.getLogger(N2TParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("A_COMMAND", testParser.commandType());
    }

    /**
     * Test of commandType method, of class N2TParser.
     */
    @Test
    public void testCommandTypeC()
    {
        System.out.println("commandTypeC");
        try {
            do {
                testParser.advance();
            } while (testParser.getCurrentCommand().startsWith("@") || testParser.getCurrentCommand().contains("("));
        } catch (IOException ex) {
            Logger.getLogger(N2TParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(testParser.getCurrentCommand());
        assertEquals("C_COMMAND", testParser.commandType());
    }

    /**
     * Test of commandType method, of class N2TParser.
     */
    @Test
    public void testCommandTypeL()
    {
        System.out.println("commandTypeL");
        try {
            do {
                testParser.advance();
            } while (!testParser.getCurrentCommand().startsWith("("));
        } catch (IOException ex) {
            Logger.getLogger(N2TParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("L_COMMAND", testParser.commandType());
    }

    /**
     * Test of symbol method, of class N2TParser.
     */
    @Test
    public void testSymbolACommand()
    {
        System.out.println("symbol");
        try {
            testParser.advance();
        } catch (IOException ioe) {
            System.out.println("ioe exception");
        }
        assertEquals("R0", testParser.symbol());
    }

    /**
     * Test of symbol method, of class N2TParser.
     */
    @Test
    public void testSymbolLCommand()
    {
        System.out.println("symbolL");
        try {
            do {
                testParser.advance();
            } while (!testParser.getCurrentCommand().startsWith("("));
        } catch (IOException ex) {
            Logger.getLogger(N2TParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("OUTPUT_FIRST", testParser.symbol());
    }

    /**
     * Test of dest method, of class N2TParser.
     */
    @Test
    public void testDest()
    {
        System.out.println("dest");
        try {
            do {
                testParser.advance();
            } while (!testParser.getCurrentCommand().contains("="));
        } catch (IOException ex) {
            Logger.getLogger(N2TParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("D", testParser.dest());
        
    }

    /**
     * Test of comp method, of class N2TParser.
     */
    @Test
    public void testComp()
    {
        System.out.println("comp");
        try {
            do {
                testParser.advance();
            } while (!testParser.commandType().equals("C_COMMAND"));
        } catch (IOException ex) {
            Logger.getLogger(N2TParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("M", testParser.comp());
    }

    /**
     * Test of jump method, of class N2TParser.
     */
    @Test
    public void testJump()
    {
        System.out.println("jump");
        try {
            do {
                testParser.advance();
            } while (!testParser.getCurrentCommand().contains(";"));
        } catch (IOException ex) {
            Logger.getLogger(N2TParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(testParser.getCurrentCommand());
        System.out.println(testParser.commandType());
        assertEquals("JGT", testParser.jump());
    }
    
}
