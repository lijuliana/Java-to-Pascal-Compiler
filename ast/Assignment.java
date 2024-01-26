package ast;
import emitter.Emitter;
import environment.Environment;

/**
 * Assignment is a subclass of Statement that supports assigning a value
 * to a variable using the environment setVariable method.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public class Assignment extends Statement
{
    // instance variables
    private String id;
    private Expression exp;

    /**
     * Assignment constructor for construction of an Assignment object.
     *
     * @param id name of the variable
     * @param exp Expression representing value of the variable
     */
    public Assignment(String id, Expression exp)
    {
        this.id = id;
        this.exp = exp;
    }

    /**
     * Exec function executes this statement by assigning the value to the variable.
     *
     * @param env environment containing all the variables and procedures for the current scope
     */
    @Override
    public void exec(Environment env)
    {
        env.setVariable(id, exp.eval(env));
    }

    /**
     * This method emits the MIPS code for a variable assignment: it puts the value into v0
     * and then saves it into the var name in data. If variable is local, saves to stack.
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        exp.compile(e);
        if (e.isLocalVariable(id))
            e.emit("sw $v0 " + e.getOffset(id) + "($sp)");
        else
            e.emit("la $t0 var" + id + "\t# variable assignment\nsw $v0 ($t0)");
    }
}
