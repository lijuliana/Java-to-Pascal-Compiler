package ast;
import java.util.*;
import environment.Environment;
import emitter.Emitter;

/**
 * ProcedureDeclaration class represents the declaration of a procedure
 * with optional parameters. It has public methods to get the parameters and
 * the statement for the procedure.
 *
 * @author Juliana Li
 * @version October 20, 2023
 */
public class ProcedureDeclaration extends Statement
{
    // instance variables
    private String id;
    private List<String> params;
    private Statement stmt;
    private List<String> localVars;

    /**
     * ProcedureDeclaration constructor constructs a ProcedureDeclaration object
     * using its name, parameters, and statement.
     *
     * @param id name of the procedure
     * @param params parameters of the procedure
     * @param stmt the statement within the procedure
     * @param localVars local variables of the procedure
     */
    public ProcedureDeclaration(String id, List<String> params, Statement stmt, List<String> localVars)
    {
        this.id = id;
        this.params = params;
        this.stmt = stmt;
        this.localVars = localVars;
    }

    /**
     * This method returns the procedure's parameters as a List of Strings.
     *
     * @return the parameters
     */
    public List<String> getParams()
    {
        return params;
    }

    /**
     * This method returns the statement for the procedure.
     *
     * @return the Statement object
     */
    public Statement getStatement()
    {
        return stmt;
    }

    /**
     * This method executes the ProcedureDeclaration by setting it in the environment.
     *
     * @param env environment containing all the variables and procedures for the current scope
     */
    public void exec(Environment env)
    {
        env.setProcedure(id, this);
    }

    /**
     * This method retrieves the name of the current procedure.
     *
     * @return the ID of the procedure
     */
    public String getId()
    {
        return id;
    }

    /**
     * This method retrieves the local variable names of the current procedure.
     *
     * @return the list of local variable names
     */
    public List<String> getLocalVars()
    {
        return localVars;
    }

    /**
     * This method emits the MIPS code for a procedure declaration: creates the label,
     * pushes the local variables, compiles the statement, and pops the vars. Finally
     * jump returns.
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        e.emit("proc" + id + ":\n");
        for (String var : localVars)
        {
            e.emit("li $v0 0");
            e.emitPush("$v0");
        }
        e.setProcedureContext(this);
        stmt.compile(e);
        for (String var : localVars)
            e.emitPop("$v0");
        e.emit("jr $ra");
        e.clearProcedureContext();
    }
}