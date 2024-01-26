package ast;
import emitter.Emitter;
import environment.Environment;

/**
 * If is a subclass of statement that implements If statements given a
 * condition, a statement, and an optional second statement for the else.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public class If extends Statement
{
    // instance variables
    private Condition cond;
    private Statement stmt1;
    private Statement stmt2;

    /**
     * If constructor to construct an If object with no else.
     *
     * @param cond condition that if statement must satisfy
     * @param stmt1 statement to execute if condition true
     */
    public If(Condition cond, Statement stmt1)
    {
        this.cond = cond;
        this.stmt1 = stmt1;
    }

    /**
     * If constructor to construct an If object with an else.
     *
     * @param cond condition that if statement must satisfy
     * @param stmt1 statement to execute if condition true
     * @param stmt2 statement to execute if condition false
     */
    public If(Condition cond, Statement stmt1, Statement stmt2)
    {
        this.cond = cond;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
    }

    /**
     * This method executes the if statement by executing stmt1 if cond is true,
     * and stmt otherwise (if it is not null).
     *
     * @param env environment containing all the variables and procedures for the current scope
     */
    @Override
    public void exec(Environment env)
    {
        if (cond.eval(env) == 1)
            stmt1.exec(env);
        else
            if (stmt2 != null)
                stmt2.exec(env);
    }

    /**
     * This method emits the MIPS code for an if or if/else statement. It gets the current
     * label number, compiles the conditional, and either jumps to the else, or executes the
     * statement and jumps to endif.
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        int labelNum = e.nextLabelID();
        cond.compile(e, "endif" + labelNum + "\t# if statement");
        stmt1.compile(e);
        e.emit("j else" + labelNum);
        e.emit("endif" + labelNum + ":");
        if (stmt2 != null)
            stmt2.compile(e);
        e.emit("else" +  labelNum + ":");
    }
}