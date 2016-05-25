/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package N2TAssembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Michael Barlow
 */
public class N2TParser implements AutoCloseable
{
    private final BufferedReader reader;
    private String currentCommand;

    /**
     *
     * @param filename
     * @throws FileNotFoundException
     */
    public N2TParser(String filename) throws FileNotFoundException
    {
        FileReader fr = new FileReader(filename);
        reader = new BufferedReader(fr);
    }
    
    /**
     *
     * @return
     * @throws IOException
     */
    public Boolean hasMoreCommands() throws IOException
    {
        return reader.ready();
    }
    
    /**
     *
     * @throws IOException
     */
    public void advance() throws IOException
    {
        do {
            currentCommand = reader.readLine();
            removeComments();
            currentCommand = currentCommand.trim();
            
        } while (currentCommand.startsWith("//") || currentCommand.isEmpty());
    }
    
    /**
     *
     * @return
     */
    public String commandType()
    {
        String cType;
        
        switch (currentCommand.substring(0, 1)) {
            case "": cType = "";
                break;
            case "@": cType = "A";
                break;
            case "(": cType = "L";
                break;
            default: cType = "C";
                break;
        }
        
        return String.format("%s_COMMAND", cType);
    }
    
    /**
     *
     * @return
     */
    public String symbol()
    {
        return currentCommand.replaceAll("[@()]", "");
    }
    
    /**
     *
     * @return
     */
    public String dest()
    {
        int eqIdx = currentCommand.indexOf('=');
        
        if (eqIdx >= 0)
            return currentCommand.substring(0, eqIdx);
        else 
            return "";
        
    }
    
    /**
     *
     * @return
     */
    public String comp()
    {
        int eqIdx = (currentCommand.indexOf('=') >= 0) ? currentCommand.indexOf('=') + 1 : 0;
        int colIdx = currentCommand.indexOf(';');
        int endIdx = colIdx >= 0 ? colIdx : currentCommand.length();
        
        return currentCommand.substring(eqIdx, endIdx);
    }
    
    /**
     *
     * @return
     */
    public String jump()
    {
        int colIdx = currentCommand.indexOf(';');
        
        if (colIdx < 0) {
            return "";
        } else {
            return currentCommand.substring(colIdx + 1);
        }
    }
    
    public void removeComments()
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
