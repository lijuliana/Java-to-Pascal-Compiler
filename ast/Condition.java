package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * Condition is a subclass of Expression that yields a boolean (0/1) result when
 * evaluated with a relational operator. Operators include =, <>, <, >, <=, =>.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public class Condition extends Expression
{
    // instance variables
    private String relop;
    private Expression expr1;
    private Expression expr2;

    /**
     * Condition constructor constructs a Condition object using two expressions
     * and a relational operator.
     *
     * @param relop given relational operator
     * @param expr1 first expression given
     * @param expr2 second expression given
     */
    public Condition(String relop, Expression expr1, Expression expr2)
    {
        this.relop = relop;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    /**
     * Evaluates the conditional expression, returns 0 if false and 1 if true.
     *
     * @param env environment containing all the variables and procedures for the current scope
     * @precondition relop is one of: =, <>, <, >, <=, =>
     * @return 0 if expression evaluates to false, 1 otherwise
     */
    public int eval(Environment env)
    {
        int num1 = expr1.eval(env);
        int num2 = expr2.eval(env);
        if (relop.equals("="))
            return (num1 == num2) ? 1 : 0;
        if (relop.equals("<>"))
            return (num1 != num2) ? 1 : 0;
        if (relop.equals("<"))
            return (num1 < num2) ? 1 : 0;
        if (relop.equals(">"))
            return (num1 > num2) ? 1 : 0;
        if (relop.equals("<="))
            return (num1 <= num2) ? 1 : 0;
        else // relop is >=
            return (num1 >= num2) ? 1 : 0;
    }

    /**
     * This method emits the MIPS code for a conditional. The condition is negated, so that
     * the branch jumps to the end label if the condition is satisfied, skipping the code
     * before the end label. Branch instructions include: bne for =, beq for <>, bge for
     * <, ble for >, bgt for <=, and blt for >=.
     *
     * @param e the emitter object that writes code to the output file
     * @param label the name of the end label to jump to
     */
    public void compile(Emitter e, String label)
    {
        expr1.compile(e);
        e.emitPush("$v0");
        expr2.compile(e);
        e.emitPop("$t0");
        if (relop.equals("="))
            e.emit("bne $t0 $v0 " + label);
        else if (relop.equals("<>"))
            e.emit("beq $t0 $v0 " + label);
        else if (relop.equals("<"))
            e.emit("bge $t0 $v0 " + label);
        else if (relop.equals(">"))
            e.emit("ble $t0 $v0 " + label);
        else if (relop.equals("<="))
            e.emit("bgt $t0 $v0 " + label);
        else // relop is >=
            e.emit("blt $t0 $v0 " + label);
    }
}