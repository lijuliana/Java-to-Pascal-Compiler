package ast;
import emitter.Emitter;
import environment.Environment;

/**
 * Writeln is a subclass of statement that implements Writeln statements
 * to output expressions.
 *
 * @author Juliana Li
 * @version October 17, 2023
 */
public class Writeln extends Statement
{
    // instance variables
    private Expression exp;

    /**
     * Writeln constructor constructs a Writeln object.
     *
     * @param exp expression to output
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }

    /**
     * Executes the Writeln statement by printing the expression.
     *
     * @param env environment containing all the variables and procedures for the current scope
     */
    @Override
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
    }

    /**
     * This method emits MIPS code to write a number to output
     *
     * @param e the emitter object that writes code to the output file
     */
    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("move $a0 $v0\nli $v0 1\t # print number \nsyscall");
        e.printLine();
    }
}
