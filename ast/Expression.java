package ast;
import emitter.Emitter;
import environment.Environment;

/**
 * Expression is an abstract class for expressions with an abstract method
 * eval to be implemented in subclasses.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public abstract class Expression
{
    /**
     * Abstract method to evaluate the expression
     *
     * @param env environment containing all the variables and procedures for the current scope
     * @return the numerical value of the evaluated expression
     */
    public abstract int eval(Environment env);

    /**
     * Abstract compile method for subclasses of expression to implement
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!!!");
    }
}
