/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VMTranslator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 *
 * @author Michael Barlow
 */
public class VMTranslator
{
    private static boolean compLoopsExist = false;
    
    public static void main(String[] args)
    {
        Path currRelPath = Paths.get("");
        String path = currRelPath.toAbsolutePath().toString();
        // take directory or single file
        String fileInName = args[0];
        String fileOutName = fileInName.split("\\.")[0] + ".asm";
        
        File input = new File(path + "/" + fileInName);
        System.out.println(input.getAbsolutePath());
        System.out.println(input.isDirectory());
        
        if (input.isDirectory()) {
            File[] files = input.listFiles();
            
            for (File file : files)
                    if (file.getName().contains(".vm"))
                    translateFile(file.getAbsolutePath(), fileOutName);
                
        } else {
            translateFile(fileInName, fileOutName);
            
            if (compLoopsExist)
                appendCompLoops(fileOutName);
        }
    }
    
    private static void translateFile(String inputFile, String outputFile)
    {
        try (
            Parser vmParser = new Parser(inputFile);
            CodeWriter vmWriter = new CodeWriter(outputFile);
        ) {
            vmWriter.setFileName(inputFile);
            
            while (vmParser.hasMoreCommands()) {
                
                vmParser.advance();
                
                switch (vmParser.commandType()) {
                    case "C_ARITHMETIC": vmWriter.writeArithmetic(vmParser.getCurrentCommand());
                        break;
                    case "C_PUSH": vmWriter.writePushPop("push", vmParser.arg1(), vmParser.arg2());
                        break;
                    case "C_POP": vmWriter.writePushPop("pop", vmParser.arg1(), vmParser.arg2());
                        break;
                }
            }
            
            compLoopsExist = vmWriter.containsComps();
            
        } catch (FileNotFoundException fnf) {
            System.out.println(fnf.getMessage());
            System.exit(100);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            System.exit(101);
        }
    }
    
    private static void appendCompLoops(String outputFile)
    {
        // append comparison loops only once at end of file
        try (
            CodeWriter vmWriter = new CodeWriter(outputFile);
        ) {
            
            vmWriter.writeCompLoops();
            
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            System.exit(101);
        }
    }
}
