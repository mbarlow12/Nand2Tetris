/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package N2TAssembler;

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
public class N2TSymbolTableTest
{
    
    public N2TSymbolTableTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of addEntry method, of class N2TSymbolTable.
     */
    @Test
    public void testAddEntry()
    {
        System.out.println("addEntry");
    }

    /**
     * Test of contains method, of class N2TSymbolTable.
     */
    @Test
    public void testContains()
    {
        System.out.println("contains");
        String symbol = "";
        N2TSymbolTable instance = new N2TSymbolTable();
        boolean expResult = false;
        boolean result = instance.contains(symbol);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAddress method, of class N2TSymbolTable.
     */
    @Test
    public void testGetAddress()
    {
        System.out.println("getAddress");
        String symbol = "KBD";
        N2TSymbolTable instance = new N2TSymbolTable();
        int expResult = 24576;
        int result = instance.getAddress(symbol);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class N2TSymbolTable.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        N2TSymbolTable table = new N2TSymbolTable();
        System.out.println(table.toString());
    }
    
}
