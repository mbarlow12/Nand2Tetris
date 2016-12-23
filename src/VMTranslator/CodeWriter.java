/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VMTranslator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Michael Barlow
 */
public class CodeWriter implements AutoCloseable
{
    
    private static final List<String> L_COMS = new ArrayList(Arrays.asList("and", "or"));
    private static final List<String> C_COMS = new ArrayList(Arrays.asList("eq", "gt", "lt"));
    private String fileName;
    private final BufferedWriter writer;
    private int compCount = 0;
    private boolean containsComps = false;
    private String functionName = null;

    public CodeWriter(String fileOut) throws IOException
    {
        FileWriter fw = new FileWriter(fileOut, true);
        writer = new BufferedWriter(fw);
    }
    
    public void setFileName(String fileName)
    {
        String[] splitName = fileName.split("\\.");
        this.fileName = splitName[0];
    }
    
    public void writeInit() throws IOException
    {
        writeLine("@256");
        writeLine("D=A");
        // write code to call 'Sys.init'
    }
    
    public void writeLabel(String label) throws IOException
    {
        writeLine(String.format("(%s)", getLabelName(label)));
    }
    
    public void writeGoTo(String label) throws IOException
    {
        writeLine(String.format("@%s", getLabelName(label)));
        writeLine("0;JMP");
    }
    
    public void writeIf(String label) throws IOException
    {
        // pop top stack item to D register
        writeLine("@SP");
        writeLine("AM=M-1");
        writeLine("D=M");
        // if D != 0 (akd D not false) jump to @{LABEL}
        writeLine(String.format("@%s", getLabelName(label)));
        writeLine("D;JNE");
    }
    
    public void writeCall(String functionName, int args) throws IOException
    {
        
    }
    
    public void writeArithmetic(String command) throws IOException
    {
        command = command.toLowerCase();
        
        if (command.equals("add"))
            writeAdd();
        else if (command.equals("sub"))
            writeSub();
        else if (command.equals("neg"))
            writeNeg();
        else if (command.equals("not"))
            writeNot();
        else if (L_COMS.contains(command))
            writeLogic(command);
        else if (C_COMS.contains(command)) {
            writeCompPlaceholder(command);
            containsComps = true;
        }
        else
            throw new IllegalArgumentException("Improper arithmetic command entered");
    }
    
    private void writeNot() throws IOException
    {
        // @SP
        writeLine("@SP");
        // A=M-1
        writeLine("A=M-1");
        // M=!M
        writeLine("M=!M");
    }
    
    private void writeNeg() throws IOException
    {
        // @SP
        writeLine("@SP");
        // A=M-1
        writeLine("A=M-1");
        // M=-M
        writeLine("M=-M");
    }
    
    private void writeAdd() throws IOException
    {
        // @SP
        writeLine("@SP");
        // AM=M-1
        writeLine("AM=M-1");
        // D=M
        writeLine("D=M");
        // A=A-1
        writeLine("A=A-1");
        // M=M+D
        writeLine("M=D+M");
    }
    
    private void writeSub() throws IOException
    {
        // @SP
        writeLine("@SP");
        // AM=M-1
        writeLine("AM=M-1");
        // D=M
        writeLine("D=M");
        // A=A-1
        writeLine("A=A-1");
        // M=M-D
        writeLine("M=M-D");
    }
    
    private void writeCompPlaceholder(String command) throws IOException 
    {
        String label = String.format("COMP_%d_%s", compCount, command.toUpperCase());
        writeLine("@" + label);
        writeLine("D=A");
        // @GT_LOOP || @EQ_LOOP
        writeLine(String.format("@%s_LOOP", command.toUpperCase()));
        writeLine("0;JMP");
        writeLine(String.format("(%s)", label));
        compCount++;
    }
    
    private void writeComp(String command) throws IOException
    {
        String label = command.toUpperCase() + "_END";
        
        writeLine(String.format("(%s_LOOP)", command.toUpperCase()));
        writeLine("@R13");
        writeLine("M=D");
        
        // @SP
        writeLine("@SP");
        // AM=M-1
        writeLine("AM=M-1");
        // D=M
        writeLine("D=M");
        // A=A-1
        writeLine("A=A-1");
        // M=M-D
        writeLine("D=M-D");
        // M=-1 - always set value to true
        writeLine("M=0");
        // @EQ_1 || @LT_4 || @GT_2 ...
        writeLine("@COMP_TRUE");
        // D;JEQ
        writeLine("D;J" + command.toUpperCase());
        // @SP
        writeLine("@COMP_END");
        writeLine("0;JMP");
    }
    
    public boolean containsComps() 
    {
        return containsComps;
    }
    
    public void writeCompLoops() throws IOException
    {
        writeLine("@END_OF_FILE");
        writeLine("0;JMP");
        
        for (String comp : C_COMS)
            writeComp(comp);
        
        writeLine("(COMP_TRUE)");
        writeLine("@SP");
        writeLine("A=M-1");
        writeLine("M=-1");
        writeLine("(COMP_END)");
        writeLine("@R13");
        writeLine("A=M");
        writeLine("0;JMP");
        writeEndOfProgram();
    }
    
    public void writeEndOfProgram() throws IOException
    {
        writeLine("(END_OF_FILE)");
        writeLine("@END_OF_FILE");
        writeLine("0;JMP");
    }
    
    private void writeLogic(String command) throws IOException
    {
        // @SP
        writeLine("@SP");
        // AM=M-1
        writeLine("AM=M-1");
        // D=M
        writeLine("D=M");
        // A=A-1
        writeLine("A=A-1");
        
        switch (command) {
            case "and" : writeLine("M=M&D");
                break;
            case "or" : writeLine("M=M|D");
                break;
        }
    }
    
    public void writePushPop(String command, String segment, int index) throws IOException
    {
        
        if (command.equals("push")) {
            // pushes a single value from specified segment onto the stack
            setDFromSegment(segment, index);
            writePushDOnStack();
        } else if (command.equals("pop")) {
            pushToSegment(segment, index);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    private void setDFromSegment(String segment, int index) throws IOException
    {
        if (segment.toLowerCase().equals("constant")) {
            // set A to constant value
            writeLine("@" + Integer.toString(index));
            // set D to value in A
            writeLine("D=A");
        } else {
            
            writeSegmentIndex(segment, index);
            
            writeLine("D=M");
        }
    }
    
    private void writePushDOnStack() throws IOException
    {
        // @SP
        writeLine("@SP");
        // M=M+1 - Increment SP to point new available register
        writeLine("M=M+1");
        // A=M-1  -  A pointing to original available stack register
        writeLine("A=M-1");
        // M=D  -  Memory at address pointed to by SP now set to value in D
        writeLine("M=D");
    }
    
    private void pushToSegment(String segment, int index) throws IOException
    {
        // set A to point to Memory[segment + index]
        writeSegmentIndex(segment, index);
        // set D to address
        writeLine("D=A");
        // save D value (address for segment[index]) to @R13
        writeLine("@R13");
        writeLine("M=D");
        // @SP
        writeLine("@SP");
        // AM=M-1 - A and Memory[0]/SP both point to 
        writeLine("AM=M-1");
        // D=M  D = value of topmost stack register
        writeLine("D=M");
        
        writeLine("@R13");
        // set A to value stored in R13
        writeLine("A=M");
        // store D value in memory[address]
        writeLine("M=D");
        
    }
    
    private void writeSegmentIndex(String segment, int index) throws IOException
    {
        boolean pointOrTemp = false;
        String aRegister;
        switch (segment.toLowerCase()) {
            case "local" : aRegister = "@LCL";
                break;
            case "argument" : aRegister = "@ARG";
                break;
            case "this" : aRegister = "@THIS";
                break;
            case "that" : aRegister = "@THAT";
                break;
            case "pointer" : aRegister = "@R" + Integer.toString(3 + index);
                pointOrTemp = true;
                break;
            case "temp" : aRegister = "@R" + Integer.toString(5 + index);
                pointOrTemp = true;
                break;
            case "static" : aRegister = "@" + fileName + "." + Integer.toString(index);
                break;
            default : aRegister = null;
                break;
        }

        if (!pointOrTemp) {
            // set A to index value
            writeLine("@" + Integer.toString(index));
            // set D=index
            writeLine("D=A");
            // @LCL or @THAT or @ARG...
            writeLine(aRegister);
            // A=M+D -- point A to Memory[base + index]
            writeLine("A=M+D");
        } else {
            // @R6 or @R3 ...
            writeLine(aRegister);
        }
    }
    
    private String getLabelName(String label)
    {
        String start = functionName == null ? "null$" : functionName + "$";
        return start + label;
    }
    
    private void writeLine(String output) throws IOException
    {
        writer.write(output);
        writer.newLine();
    }

    @Override
    public void close() throws IOException
    {
        writer.close();
    }
}
