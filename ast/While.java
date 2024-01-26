package ast;
import emitter.Emitter;
import environment.Environment;

/**
 * While is a subclass of statement that implements while statements given a
 * condition and a statement.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public class While extends Statement
{
    // instance variables
    private Condition cond;
    private Statement stmt;

    /**
     * While constructor to construct a while object given a condition and statement.
     *
     * @param cond condition for the while loop
     * @param stmt statement to execute
     */
    public While(Condition cond, Statement stmt)
    {
        this.cond = cond;
        this.stmt = stmt;
    }

    /**
     * Executes the while statement by executing stmt while cond is true.
     *
     * @param env environment containing all the variables and procedures for the current scope
     */
    @Override
    public void exec(Environment env)
    {
        while (cond.eval(env) == 1)
            stmt.exec(env);
    }

    /**
     * This method outputs the MIPS code for a while loop. MIPS code: gets the next label number,
     * makes a loop label, and jumps to an endloop label if condition is no longer satisfied,
     * otherwise repeatedly jumps back to the beginning of the loop.
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        int labelNum = e.nextLabelID();
        e.emit("loop" + labelNum + ":\t # while loop");
        cond.compile(e, "endloop" + labelNum);
        stmt.compile(e);
        e.emit("j loop" + labelNum);
        e.emit("endloop" + labelNum + ":");
    }
}
