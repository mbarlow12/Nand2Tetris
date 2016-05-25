/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package N2TAssembler;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Michael Barlow
 */
public class N2TSymbolTable
{
    private final Map<String, Integer> symbolTable;

    public N2TSymbolTable()
    {
        symbolTable = new HashMap<>();
        
        String[] preSymbols = {"SP", "LCL", "ARG", "THIS", "THAT", "SCREEN", "KBD"};
        Integer[] preAddr = {0, 1, 2, 3, 4, 16384, 24576};
        
        int x = 0;
        for (String symbol : preSymbols) {
            symbolTable.put(symbol, preAddr[x++]);
        }
        
        for (int i = 0; i <= 15; i++) {
            String reg = "R" + Integer.toString(i);
            symbolTable.put(reg, i);
        }
    }
    
    public void addEntry(String symbol, int addr)
    {
        symbolTable.put(symbol, addr);
    }
    
    public boolean contains(String symbol)
    {
        return symbolTable.containsKey(symbol);
    }
    
    public int getAddress(String symbol)
    {
        return symbolTable.get(symbol);
    }

    @Override
    public String toString()
    {
        StringBuilder out = new StringBuilder();
        for (String key : symbolTable.keySet()) {
            String toAppend = String.format("%s => %s\n", key, Integer.toString(symbolTable.get(key)));
            out.append(toAppend);
        }
        
        return out.toString();
    }
    
    
}
