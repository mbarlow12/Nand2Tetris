/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package N2TAssembler;

/**
 *
 * @author Michael Barlow
 */
public class N2TCode
{
    public static String dest(String dest)
    {
        int[] binArray = new int[3];
        String binString = "";
        
        binArray[2] = dest.contains("M") ? 1 : 0;
        binArray[1] = dest.contains("D") ? 1 : 0;
        binArray[0] = dest.contains("A") ? 1 : 0;
        
        for (int i : binArray) {
            binString += Integer.toString(i);
        }
        
        return binString;
    }
    
    public static String comp(String comp)
    {
        String a, compString, compNoAorM;
        a = comp.contains("M") ? "1" : "0";
        compNoAorM = comp.replaceAll("[aA]|[mM]", "X");
        
        switch (compNoAorM)  {
            case "0"   : compString = "101010";
                break;
            case "1"   : compString = "111111";
                break;
            case "-1"  : compString = "111010";
                break;
            case "D"   : compString = "001100";
                break;
            case "X"   : compString = "110000";
                break;
            case "!D"  : compString = "001101";
                break;
            case "!X"  : compString = "110001";
                break;
            case "-D"  : compString = "001111";
                break;
            case "-X"  : compString = "110011";
                break;
            case "D+1" : compString = "011111";
                break;
            case "X+1" : compString = "110111";
                break;
            case "D-1" : compString = "001110";
                break;
            case "X-1" : compString = "110010";
                break;
            case "D+X" : compString = "000010";
                break;
            case "D-X" : compString = "010011";
                break;
            case "X-D" : compString = "000111";
                break;
            case "D&X" : compString = "000000";
                break;
            case "D|X" : compString = "010101";
                break;
            default : compString = null;
                break;
            
        }
        
        return a + compString;
        
    }
    
    public static String jump (String jump)
    {
        int[] binArray = {0,0,0};
        String binString = "";
        
        binArray[2] = jump.contains("G") ? 1 : 0;
        binArray[1] = jump.contains("E") ? 1 : 0;
        binArray[0] = jump.contains("L") ? 1 : 0;
        
        if (jump.contains("NE")) {
            binArray[0] = 1;
            binArray[1] = 0;
            binArray[2] = 1;
        }
        
        if (jump.equals("JMP")) {
            binArray[0] = 1;
            binArray[1] = 1;
            binArray[2] = 1;
        }
        
        for (int i : binArray) {
            binString += Integer.toString(i);
        }
        
        return binString;
        
    }
    
    public static String address (int addr)
    {
        String binStr = Integer.toBinaryString(addr);
        return ("000000000000000" + binStr).substring(binStr.length());
    }
}
