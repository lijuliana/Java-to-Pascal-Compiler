package ast;
import emitter.Emitter;
import environment.Environment;

/**
 * Statement is an abstract class with an abstract method exec to be implemented
 * by subclasses.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public abstract class Statement
{
    /**
     * Executes the statement; abstract method to be implemented
     *
     * @param env environment containing all the variables and procedures for the current scope
     */
    public abstract void exec(Environment env);

    /**
     * Abstract method to compile a statement by emitting MIPS code; to be implemented
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!!!");
    }
}