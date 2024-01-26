package ast;
import java.util.*;
import environment.Environment;
import emitter.Emitter;

/**
 * ProcedureCall class represents a call to a procedure with optional
 * arguments in the form of Expressions. Its exec method executes the
 * statement of the procedure given the arguments.
 *
 * @author Juliana Li
 * @version October 20, 2023
 */
public class ProcedureCall extends Expression
{
    // instance variables
    private String id;
    private List<Expression> args;

    /**
     * ProcedureCall constructor constructs a ProcedureCall object
     * given its name and arguments.
     *
     * @param id name of the procedure to call
     * @param args arguments to call the procedure with
     */
    public ProcedureCall(String id, List<Expression> args)
    {
        this.id = id;
        this.args = args;
    }

    /**
     * This method evaluates the procedure call given the arguments.
     *
     * @param env environment containing all the variables and procedures for the current scope
     * @return the number obtained by evaluating the procedure
     */
    @Override
    public int eval(Environment env)
    {
        Environment child = new Environment(env.getGlobal());
        ProcedureDeclaration proc = env.getProcedure(id);
        List<String> params = proc.getParams();
        child.declareVariable(id, 0);
        for (int i=0; i<args.size(); i++)
            child.declareVariable(params.get(i), args.get(i).eval(env));
        List<String> localVars = proc.getLocalVars();
        for (int i=0; i<localVars.size(); i++)
            child.declareVariable(localVars.get(i), 0);
        proc.getStatement().exec(child);
        return child.getVariable(id);
    }

    /**
     * This method emits the MIPS code for a procedure call: pushes $ra onto the stack, then
     * the return value and the arguments, then jumps and links to the procedure. After, it
     * pops what was pushed.
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        e.emitPush("$ra");
        e.emit("li $v0 0    #return val");
        e.emitPush("$v0");
        // push args to stack
        for (Expression arg : args)
        {
            arg.compile(e);
            e.emitPush("$v0");
        }
        e.emit("jal proc" + id);
        // pop args from stack
        for (Expression arg : args)
            e.emitPop("$v0");
        e.emitPop("$v0");
        e.emitPop("$ra");
    }
}