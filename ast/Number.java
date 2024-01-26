package ast;
import emitter.Emitter;
import environment.Environment;

/**
 * Number is a subclass of Expression that represents a single integer.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public class Number extends Expression
{
    // instance variables
    private int num;

    /**
     * Constructor for Number class to construct a Number object.
     *
     * @param num the integer value of the number
     */
    public Number(int num)
    {
        this.num = num;
    }

    /**
     * Evaluates this expression by returning the number's value.
     *
     * @param env environment containing all the variables and procedures for the current scope
     * @return the value of the number.
     */
    @Override
    public int eval(Environment env)
    {
        return num;
    }

    /**
     * This method emits the MIPS code to load a value into the register v0
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0 " + num);
    }
}
