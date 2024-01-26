package ast;
import emitter.Emitter;
import environment.Environment;

/**
 * Variable is a subclass of Expression that represents a variable, returning
 * the value of the variable using the Environment getVariable method.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public class Variable extends Expression
{
    // instance variables
    private String id;

    /**
     * Constructor for Variable class that constructs a Variable object.
     *
     * @param id identifier of the variable
     */
    public Variable(String id)
    {
        this.id = id;
    }

    /**
     * Evaluates the variable by returning its value gotten from the environment.
     *
     * @param env environment containing all the variables and procedures for the current scope
     * @return the value of the variable
     */
    @Override
    public int eval(Environment env)
    {
        return env.getVariable(id);
    }

    /**
     * This method emits the MIPS code to load the value of a variable into the v0 register
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        if (e.isLocalVariable(id))
            e.emit("lw $v0 " + e.getOffset(id) + "($sp)\t# get local var " + id);
        else
            e.emit("la $t0 var" + id + "\nlw $v0 ($t0)\t# get global var " + id);
    }
}
