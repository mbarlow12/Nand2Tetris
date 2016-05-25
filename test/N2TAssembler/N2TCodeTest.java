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
public class N2TCodeTest
{
    
    public N2TCodeTest()
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
     * Test of dest method, of class N2TCode.
     */
    @Test
    public void testDest1()
    {
        System.out.println("dest");
        String destAD = "AD";
        assertEquals("110", N2TCode.dest(destAD));
    }
    
    
    /**
     * Test of dest method, of class N2TCode.
     */
    @Test
    public void testDest2()
    {
        System.out.println("dest");
        String destAD = "MD";
        assertEquals("011", N2TCode.dest(destAD));
    }
    
    
    
    /**
     * Test of dest method, of class N2TCode.
     */
    @Test
    public void testDest3()
    {
        System.out.println("dest");
        String destAD = "MA";
        assertEquals("101", N2TCode.dest(destAD));
    }

    /**
     * Test of comp method, of class N2TCode.
     */
    @Test
    public void testComp1()
    {
        System.out.println("comp");
        String compString = "A+1";
        assertEquals("0110111", N2TCode.comp(compString));
    }

    /**
     * Test of comp method, of class N2TCode.
     */
    @Test
    public void testComp2()
    {
        System.out.println("comp");
        String compString = "D+M";
        assertEquals("1000010", N2TCode.comp(compString));
    }

    /**
     * Test of jump method, of class N2TCode.
     */
    @Test
    public void testJump1()
    {
        System.out.println("jump");
        String jmpStr = "JGT";
        assertEquals("001", N2TCode.jump(jmpStr));
    }

    /**
     * Test of jump method, of class N2TCode.
     */
    @Test
    public void testJump2()
    {
        System.out.println("jump");
        String jmpStr = "JNE";
        assertEquals("101", N2TCode.jump(jmpStr));
    }

    /**
     * Test of address method, of class N2TCode.
     */
    @Test
    public void testAddress()
    {
        System.out.println("address");
        int addr = 16893;
        System.out.println(N2TCode.address(addr));
        System.out.println(Integer.toBinaryString(addr));
    }
    
}
