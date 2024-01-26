package parser;
import scanner.ScanErrorException;
import scanner.Scanner;
import ast.*;
import ast.Number;
import java.util.*;

/**
 * Parser is a simple parser that takes an input file of text, uses the scanner class to
 * tokenize it, and parses simple pascal code, supporting basic operations. Parses a program,
 * which contains procedure declarations followed by a single main statement to be executed.
 *
 *
 * @author Juliana Li
 * @version October 2, 2023
 */
public class Parser
{
    // instance variables
    private Scanner scan;
    private String cur;

    /**
     * Parser constructor for construction of a Parser object. Takes in
     * a scanner object to tokenize the text file and sets the current token
     * to the first token in the scanner. Throws an error if scanning fails.
     *
     * @param s Scanner object to tokenize text file
     * @throws ScanErrorException when there is an error in tokenization
     */
    public Parser(Scanner s) throws ScanErrorException
    {
        scan = s;
        cur = scan.nextToken();
    }

    /**
     * Compares the expected argument to the current token and then advances to
     * the next token if they are equal. Throws a ScanErrorException if current
     * token does not match expected.
     *
     * @param expected the expected token
     * @throws ScanErrorException if current is not the same as expected
     */
    private void eat(String expected) throws ScanErrorException
    {
        if (expected.equals(cur))
            cur = scan.nextToken();
        else
            throw new IllegalArgumentException("Illegal token - expected " + expected
                    + " and found " + cur + ".");
    }

    /**
     * Parses a single integer number from a numeric token. Eats the number
     * and then uses parseInt to convert it from string to integer. Returns
     * a number object.
     *
     * @return a number representing the number parsed
     * @throws ScanErrorException when there is an error in tokenization
     */
    private Number parseNumber() throws ScanErrorException
    {
        Number num = new Number(Integer.parseInt(cur));
        eat(cur);
        return num;
    }

    /**
     * Parses and returns a statement, including WRITELN, which prints the
     * argument, a BEGIN/END block, which executes everything inside it,
     * variable assignment, which assigns a value to a variable name, an
     * IF/THEN/(ELSE) conditional block, or a WHILE/DO that executes its
     * statement as long as its condition is true. Each statement may take
     * in arguments that are expressions. Returns a statement object.
     *
     * @return a Statement object representing the statement
     * @throws ScanErrorException when there is an error in tokenization
     */
    public Statement parseStatement() throws ScanErrorException
    {
        Expression num;
        if (cur.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            num = parseExpression();
            eat(")");
            eat(";");
            return new Writeln(num);
        }
        if (cur.equals("READLN"))
        {
            eat("READLN");
            eat("(");
            String var = cur;
            eat(cur);
            eat(")");
            eat(";");
            return new Readln(var);
        }
        if (cur.equals("BEGIN"))
        {
            ArrayList<Statement> lines = new ArrayList<Statement>();
            eat("BEGIN");
            while (!cur.equals("END"))
                lines.add(parseStatement());
            eat("END");
            eat(";");
            return new Block(lines);
        }
        if (cur.equals("IF"))
        {
            eat("IF");
            Expression exp1 = parseExpression();
            String relop = cur;
            eat(cur);
            Expression exp2 = parseExpression();
            Condition cond = new Condition(relop, exp1, exp2);
            eat("THEN");
            Statement stmt1 = parseStatement();
            if (!cur.equals("ELSE"))
                return new If(cond, stmt1);
            else
            {
                eat("ELSE");
                Statement stmt2 = parseStatement();
                return new If(cond, stmt1, stmt2);
            }
        }
        if (cur.equals("WHILE"))
        {
            eat("WHILE");
            Expression exp1 = parseExpression();
            String relop = cur;
            eat(cur);
            Expression exp2 = parseExpression();
            Condition cond = new Condition(relop, exp1, exp2);
            eat("DO");
            Statement stmt = parseStatement();
            return new While(cond, stmt);
        }
        if (!cur.equals("EOF"))
        {
            String id = cur;
            eat(cur);
            eat(":=");
            Expression val = parseExpression();
            eat(";");
            return new Assignment(id, val);
        }
        return null;
    }

    /**
     * Parses and computes a factor, which consists of an expression surrounded by
     * parentheses (expr), a factor preceded by a minus sign -factor, a number num,
     * a variable id, or a procedure call. Returns an Expression object at the end.
     *
     * @return the Expression for the factor
     * @throws ScanErrorException when there is an error in tokenization
     */
    private Expression parseFactor() throws ScanErrorException
    {
        Expression num;
        if (cur.equals("("))
        {
            eat("(");
            num = parseExpression();
            eat(")");
        }
        else if (cur.equals("-"))
        {
            eat("-");
            num = new BinOp("-", new Number(0), parseFactor());
        }
        else if (cur.charAt(0) >= '0' && cur.charAt(0) <= '9')
            num = parseNumber();
        else
        {
            String id = cur;
            eat(cur);
            if (cur.equals("("))
            {
                eat("(");
                List<Expression> args = new ArrayList<Expression>();
                if (!cur.equals(")"))
                    args.add(parseExpression());
                while (cur.equals(","))
                {
                    eat(",");
                    args.add(parseExpression());
                }
                eat(")");
                num = new ProcedureCall(id, args);
            }
            else
                num = new Variable(id);
        }
        return num;
    }

    /**
     * Parses and computes a term, which consists of either a term multiplied
     * by a factor term*factor, a term divided by a factor term/factor, a term mod
     * a factor, or a factor. While the operator is *, /, or mod, the method
     * continuously parses the factors for this term. Returns an Expression
     * representing this term.
     *
     * @return the Expression representing the term
     * @throws ScanErrorException when there is an error in tokenization
     */
    private Expression parseTerm() throws ScanErrorException
    {
        Expression num = parseFactor();
        while (cur.equals("*") || cur.equals("/") || cur.equals("mod"))
        {
            if (cur.equals("*"))
            {
                eat("*");
                num = new BinOp("*", num, parseFactor());
            }
            else if (cur.equals("/"))
            {
                eat("/");
                num = new BinOp("/", num, parseFactor());
            }
            else
            {
                eat("mod");
                num = new BinOp("%", num, parseFactor());
            }
        }
        return num;
    }

    /**
     * Parses and computes an expression, which consists of an expression added
     * to a term expr+term, an expression subtracted by a term expr-term, or a
     * term. Continuously parses terms as long as the current operator is + or
     * -. Returns an Expression at the end.
     *
     * @return the Expression object for the expression parsed and computed
     * @throws ScanErrorException when there is an error in tokenization
     */
    private Expression parseExpression() throws ScanErrorException
    {
        Expression num = parseTerm();
        while (cur.equals("+") || cur.equals("-"))
        {
            if (cur.equals("+"))
            {
                eat("+");
                num = new BinOp("+", num, parseTerm());
            }
            else
            {
                eat("-");
                num = new BinOp("-", num, parseTerm());
            }
        }
        return num;
    }

    /**
     * This method parses a single procedure represented by PROCEDURE name();
     * followed by local variable declarations and a single statement, then
     * creates the ProcedureDeclaration object and returns it.
     *
     * @return a ProcedureDeclaration object representing the procedure parsed
     * @throws ScanErrorException when there is an error in tokenization
     */
    public ProcedureDeclaration parseProcedure() throws ScanErrorException
    {
        eat("PROCEDURE");
        String id = cur;
        eat(cur);
        eat("(");
        List<String> params = new ArrayList<String>();
        if (!cur.equals(")"))
        {
            params.add(cur);
            eat(cur);
        }
        while (cur.equals(","))
        {
            eat(",");
            params.add(cur);
            eat(cur);
        }
        eat(")");
        eat(";");
        List<String> localVars = new ArrayList<String>();
        while (cur.equals("VAR"))
        {
            eat(cur);
            localVars.add(cur);
            eat(cur);
            while (!cur.equals(";"))
            {
                eat(",");
                localVars.add(cur);
                eat(cur);
            }
            eat(";");
        }
        return new ProcedureDeclaration(id, params, parseStatement(), localVars);
    }

    /**
     * This method parses and returns a whole program consisting of procedures
     * and a statement. Creates a list of ProcedureDeclaration objects and
     * continuously parses and appends the procedures. Returns a program with
     * the list of a procedures and the parsed statement at the end.
     *
     * @return the Program object for the parsed program
     * @throws ScanErrorException when there is an error in tokenization
     */
    public Program parseProgram() throws ScanErrorException
    {
        List<String> vars = new ArrayList<String>();
        while (cur.equals("VAR"))
        {
            eat(cur);
            vars.add(cur);
            eat(cur);
            while (!cur.equals(";"))
            {
                eat(",");
                vars.add(cur);
                eat(cur);
            }
            eat(";");
        }
        List<ProcedureDeclaration> procs = new ArrayList<ProcedureDeclaration>();
        while (cur.equals("PROCEDURE"))
            procs.add(parseProcedure());
        return new Program(vars, procs, parseStatement());
    }
}
