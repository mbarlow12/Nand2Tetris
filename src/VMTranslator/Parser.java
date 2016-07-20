/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VMTranslator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Michael Barlow
 */
public class Parser implements AutoCloseable
{
    private final BufferedReader reader;
    private String currentCommand;
    private static final List<String> ARITHMETIC_COMMANDS = new ArrayList(Arrays.asList("add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"));

    public Parser(String filename) throws FileNotFoundException
    {
        FileReader fr = new FileReader(filename);
        reader = new BufferedReader(fr);
    }
    
    public boolean hasMoreCommands() throws IOException
    {
        return reader.ready();
    }
    
    public void advance() throws IOException
    {
        do {
            currentCommand = reader.readLine();
            removeComments();
            currentCommand = currentCommand.trim();
        } while (currentCommand.startsWith("//") || currentCommand.isEmpty());
    }
    
    public String commandType()
    {
        String comType;
        String[] cArray = currentCommand.split("[\\s]");
        
        if (ARITHMETIC_COMMANDS.contains(cArray[0])) {
            comType = "arithmetic";
        } else if (cArray[0].contains("function")) {
            comType = "function";
        } else if (cArray[0].contains("label")) {
            comType = "label";
        } else if (cArray[0].contains("push")) {
            comType = "push";
        } else if (cArray[0].contains("pop")) {
            comType = "pop";
        } else if (cArray[0].contains("goto")) {
            comType = "goto";
        } else if (cArray[0].contains("if-goto")) {
            comType = "if";
        } else if (cArray[0].contains("call")) {
            comType = "call";
        } else if (cArray[0].equals("return")) {
            comType = "return";
        } else {
            comType = "";
        }
        
        if (comType.equals(""))
            return "";
        else
            return "C_" + comType.toUpperCase();
    }
    
    public String arg1()
    {
        String[] cArray = currentCommand.split("[\\s]");
        
        if (cArray.length > 1)
            return cArray[1];
        else
            return cArray[0];
        
    }
    
    public int arg2()
    {
        // should likely throw an exception in the event that the array isn't long enough
        String[] cArray = currentCommand.split("[\\s]");
        
        return Integer.parseInt(cArray[2]);
    }
    
    private void removeComments()
    {
        int comIdx = currentCommand.indexOf("//");
        if (comIdx >= 0)
            currentCommand = currentCommand.substring(0, comIdx);
    }
    
    public void mark() throws IOException
    {
        reader.mark(5000);
    }
    
    public void reset() throws IOException
    {
        reader.reset();
    }
    
    public String getCurrentCommand()
    {
        return currentCommand;
    }

    @Override
    public void close() throws IOException
    {
        reader.close();
    }
}
