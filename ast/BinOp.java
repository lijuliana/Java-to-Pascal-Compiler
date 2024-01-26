package ast;
import emitter.Emitter;
import environment.Environment;

/**
 * BinOp is a subclass of Expression that represents two expressions
 * combined by an binary operator. Operators include +, -, *, /, and mod.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public class BinOp extends Expression
{
    // instance variables
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * This method combines two expressions using an operator.
     *
     * @param op operator to use on the two expressions
     * @param exp1 the first expression given
     * @param exp2 the second expression given
     */
    public BinOp(String op, Expression exp1, Expression exp2)
    {
        this.op = op;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    /**
     * Evaluates the BinOp expression by adding if op is "+", subtracting if op
     * is "-", multiplying if "*", dividing if "/", and taking modulus if "mod".
     *
     * @param env environment containing all the variables and procedures for the current scope
     * @return the numerical value of the evaluated BinOp expression
     */
    @Override
    public int eval(Environment env)
    {
        if (op.equals("+"))
            return exp1.eval(env) + exp2.eval(env);
        if (op.equals("-"))
            return exp1.eval(env) - exp2.eval(env);
        if (op.equals("*"))
            return exp1.eval(env) * exp2.eval(env);
        if (op.equals("/"))
            return exp1.eval(env) / exp2.eval(env);
        else // op is mod
            return exp1.eval(env) % exp2.eval(env);
    }

    /**
     * This method emits the MIPS code for a binary operation between the first expression,
     * stored in v0, and the second, stored in t0. Binops include addu for +, subu for -,
     * mult + mflo for *, div + mflo for /, and div + mfhi for mod.
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");

        if (op.equals("+"))
            e.emit("addu $v0 $t0 $v0");
        else if (op.equals("-"))
            e.emit("subu $v0 $t0 $v0");
        else if (op.equals("*"))
            e.emit("mult $t0 $v0\nmflo $v0");
        else if (op.equals("/"))
            e.emit("div $t0 $v0\nmflo $v0");
        else // op is mod
            e.emit("div $t0 $v0\nmfhi $v0");
    }
}
