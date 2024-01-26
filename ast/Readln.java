package ast;
import emitter.Emitter;
import environment.Environment;
import java.util.*;

/**
 * Readln is a subclass of statement that implements READLN statements
 * to input expressions.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public class Readln extends Statement
{
    // instance variables
    private String var;

    /**
     * Readln constructor constructs a Readln object.
     *
     * @param var the variable name to input the value to
     */
    public Readln(String var)
    {
        this.var = var;
    }

    /**
     * Executes the Readln statement by reading the user input into the given variable.
     *
     * @param env environment containing all the variables and procedures for the current scope
     */
    @Override
    public void exec(Environment env)
    {
        Scanner s = new Scanner(System.in);
        int val = Integer.parseInt(s.nextLine());
        env.declareVariable(var, val);
    }

    /**
     * This method emits the MIPS code to read a value from the user and store it
     * into a variable
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0 5\nsyscall\nsw $v0 var" + var + "\t# read user input");
        e.printLine();
    }
}
