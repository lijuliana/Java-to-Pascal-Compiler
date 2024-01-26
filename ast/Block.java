package ast;
import emitter.Emitter;
import environment.Environment;
import java.util.ArrayList;

/**
 * Block is a subclass of Statement that executes a block of statements surrounded
 * by BEGIN and END.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public class Block extends Statement
{
    // instance variables
    private ArrayList<Statement> statements;

    /**
     * Block constructor to construct a Block object given the statements
     *
     * @param statements list of statements within the block
     */
    public Block(ArrayList<Statement> statements)
    {
        this.statements = statements;
    }

    /**
     * Executes the Block by looping through and executing all the statements.
     * @param env environment containing all the variables and procedures for the current scope
     */
    @Override
    public void exec(Environment env)
    {
        for (Statement stmt : statements)
            stmt.exec(env);
    }

    /**
     * This method emits the MIPS code for a BEGIN/END block by using a for loop to compile
     * all the statements within it.
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        for (Statement stmt : statements)
            stmt.compile(e);
    }
}
