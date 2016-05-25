/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package N2TAssembler;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Barlow
 */
public class N2TAssembler
{
    
    public static void main(String[] args)
    {
        String filename = args[0];
//        create ArrayList for binary lines
        List<String> binaryOut = new ArrayList<>();
        
//        create symbol table
        N2TSymbolTable symbolTable = new N2TSymbolTable();
//        create new parser object from 1st arg
        try (N2TParser parser = new N2TParser(filename);)
        {
//            pass 1: populate symbol table with appropriate line numbers
            int lineNumber = 0;
            while (parser.hasMoreCommands()) {
                
                parser.advance();
                
                if (parser.commandType().equals("L_COMMAND")) 
                    symbolTable.addEntry(parser.symbol(), lineNumber);
                else
                    lineNumber++;
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("No file found.");
            fnfe.printStackTrace(System.out);
            System.exit(2);
        } catch (IOException ioe) {
            System.out.println("Close error.");
            ioe.printStackTrace(System.out);
            System.exit(3);
        }
            
        try (N2TParser parser = new N2TParser(filename);)
        {
//            pass 2: parse each line into binary string, add string to output list
            
            int nextAvailableRAMAddr = 16;
            
            while (parser.hasMoreCommands()) {
                String binLine = "";
                parser.advance();
                
                if (parser.commandType().equals("L_COMMAND"))
                    continue;

                if (parser.commandType().equals("A_COMMAND")) {
                    binLine += "0";
                    String symbol = parser.symbol();
                    try {
                        Integer addr = Integer.parseInt(symbol);
                        binLine += N2TCode.address(addr);
                    } catch (NumberFormatException nfe) {
                        
                        if (!symbolTable.contains(symbol)) {
                            System.out.println(String.format("%s not in table", symbol));
                            symbolTable.addEntry(symbol, nextAvailableRAMAddr);
                            nextAvailableRAMAddr++;
                        }
                        
                        binLine += N2TCode.address(symbolTable.getAddress(symbol));       
                    }
                        
                } else if (parser.commandType().equals("C_COMMAND")) {
                    binLine += "111";
                    binLine += N2TCode.comp(parser.comp());
                    binLine += N2TCode.dest(parser.dest());
                    binLine += N2TCode.jump(parser.jump());
                }
                
                binaryOut.add(binLine);
            }
            System.out.println(symbolTable.getAddress("R15"));
            System.out.println(N2TCode.address(15));
            
        } catch (FileNotFoundException fnfe) {
            System.out.println("No file found.");
            fnfe.printStackTrace(System.out);
            System.exit(2);
        } catch (IOException ioe) {
            System.out.println("Close error.");
            ioe.printStackTrace(System.out);
            System.exit(3);
        }
        
        String[] file = filename.split("\\.");
        String writeFilename = file[0] + ".hack";
        String path = args[1];
        
        try (
            FileWriter fw = new FileWriter(path + writeFilename);
            BufferedWriter bw = new BufferedWriter(fw);
        ) {
            
            for (String string : binaryOut) {
                bw.write(string);
                bw.newLine();
            }
            
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            ioe.printStackTrace(System.out);
            System.exit(3);
        }
    }
}
