package environment;
import java.util.Map;
import java.util.HashMap;
import ast.*;

/**
 * Environment class remembers variables, and it supports setting and getting variables.
 * procedures have environments that extend the global environment, but there is no
 * hierarchy. Environment supports public methods to declare, set, and get variables
 * and procedures.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public class Environment
{
    // instance variables
    private Environment parent;
    private Map<String, Integer> vars;
    private Map<String, ProcedureDeclaration> procs;

    /**
     * Environment constructor to construct an Environment object. Creates a list
     * for the variables to be stored.
     *
     * @param parent the parent environment (global) if procedure, or null if current
     *               environment is global.
     */
    public Environment(Environment parent)
    {
        this.parent = parent;
        vars = new HashMap<String, Integer>();
        procs = new HashMap<String, ProcedureDeclaration>();
    }

    /**
     * This method associates the given variable name with the given value.
     *
     * @param id variable name given
     * @param value value to associate with the variable name
     */
    public void declareVariable(String id, int value)
    {
        vars.put(id, value);
    }

    /**
     * If the variable exists in the current environment, it is set to the
     * new value. Otherwise, if the variable exists in the parent environment,
     * it is set to the new value there. If neither of these are true, a new variable
     * is created in the current environment and set to the new value.
     *
     * @param id the id of the variable to set to the given value
     * @param value the integer given for the variable value
     */
    public void setVariable(String id, int value)
    {
        if (vars.containsKey(id))
            vars.put(id, value);
        else if (parent != null && parent.hasVariable(id))
            parent.setVariable(id, value);
        else
            vars.put(id, value);
    }

    /**
     * This method checks if the current environment (not including parent
     * environments) contains a variable by checking if the variable name
     * exists in the key.
     *
     * @param id name of the variable
     * @return true if variable exists, false otherwise
     */
    public boolean hasVariable(String id)
    {
        return vars.containsKey(id);
    }

    /**
     * This method returns the value associated with the given variable name
     *
     * @param id given variable name
     * @return the value associated with the given variable name
     */
    public int getVariable(String id)
    {
        if (vars.containsKey(id))
            return vars.get(id);
        if (parent != null && parent.hasVariable(id))
            return parent.getVariable(id);
        vars.put(id, 0);
        return 0;
    }

    /**
     * This method returns the parent environment if it exists, meaning
     * the environment is the global one. Otherwise it returns the current
     * environment (global).
     *
     * @return the global Environment object
     */
    public Environment getGlobal()
    {
        if (parent != null)
            return parent;
        return this;
    }

    /**
     * This method associates a procedure name with the given procedure.
     *
     * @param name given procedure name
     * @param proc given ProcedureDeclaration object
     */
    public void setProcedure(String name, ProcedureDeclaration proc)
    {
        if (parent == null)
            procs.put(name, proc);
        else
            parent.setProcedure(name, proc);
    }

    /**
     * This method returns the procedure associated with the given name.
     *
     * @param name name of the procedure
     * @return the ProcedureDeclaration object associated with the given name
     */
    public ProcedureDeclaration getProcedure(String name)
    {
        if (parent == null)
            return procs.get(name);
        return parent.getProcedure(name);
    }
}
