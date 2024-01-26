package ast;
import java.util.*;

import emitter.Emitter;
import environment.Environment;

/**
 * Program class represents the entire program that is parsed by the parser.
 * This includes a list of ProcedureDeclarations followed by a single Statement. Its
 * exec method loops through the ProcedureDeclarations and declares them into the
 * environment.
 *
 * @author Juliana Li
 * @version October 20, 2023
 */
public class Program extends Statement
{
    // instance variables
    private List<String> vars;
    private List<ProcedureDeclaration> procedures;
    private Statement stmt;

    /**
     * Program constructor takes in a list of ProcedureDeclarations and a statement
     * and constructs a executable program object.
     *
     * @param procedures the list of procedure declarations at the beginning of
     *                   the program
     * @param stmt the statement to be executed for the program
     */
    public Program(List<String> vars, List<ProcedureDeclaration> procedures, Statement stmt)
    {
        this.vars = vars;
        this.procedures = procedures;
        this.stmt = stmt;
    }

    /**
     * This method executes the program by declaring all the ProcedureDeclarations
     * by setting them in the environment, and then executing the statement, which
     * may call the procedures.
     *
     * @param env environment containing all the variables and procedures for the current scope
     */
    public void exec(Environment env)
    {
        for (ProcedureDeclaration p : procedures)
            p.exec(env);
        stmt.exec(env);
    }

    /**
     * This method makes an Emitter object for the program and emits the MIPS code
     * to a given file name: this includes the data section with all the variables,
     * and the .text section with a global main. The procedures are declared and the
     * main statement is compiled, and the file ends with program termination code.
     *
     * @param fileName name of file to write the generated MIPS code to
     */
    public void compile(String fileName)
    {
        Emitter e = new Emitter(fileName);
        e.emit("# This program contains MIPS code auto-generated from given PASCAL code.\n" +
                "# @author Juliana Li\n" +
                "# @version 1/8/24\n# (:");
        e.emit(".data\n"+
                "newLine: .asciiz \"\\n\"\n");
        for (String var : vars)
            e.emit("var" + var + ": .word 0");
        e.emit(".text\n" +
                ".globl main\n" +
                "main: #QTSPIM will automatically look for main\n");
        stmt.compile(e);
        e.emit("li $v0 10\n" +
                "syscall # halt");
        for (ProcedureDeclaration p : procedures)
            p.compile(e);
    }
}
