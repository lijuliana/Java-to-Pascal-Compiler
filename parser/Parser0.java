package parser;
import scanner.ScanErrorException;
import scanner.Scanner;
import java.util.Map;
import java.util.HashMap;

/**
 * Parser is a simple parser that takes an input file of text, uses the scanner class to
 * tokenize it, and parses simple pascal code, supporting basic operations. Parses statements
 * that can be executed. Statements can be WRITELN for an expression, BEGIN/END blocks
 * consisting of statements, or a variable assignment to an expression. Expressions contain
 * terms added and subtracted, terms contain factors multiplied, divided, and modded, and
 * factors consist of expressions enclosed in parentheses, negated factors, numbers, and
 * variables.
 *
 * @author Juliana Li
 * @version October 2, 2023
 */
public class Parser0
{
    // instance variables
    private Scanner scan;
    private String cur;
    private Map<String, Integer> variables;

    /**
     * Parser constructor for construction of a Parser object. Takes in
     * a scanner object to tokenize the text file and sets the current token
     * to the first token in the scanner. Throws an error if scanning fails.
     *
     * @param s Scanner object to tokenize text file
     * @throws ScanErrorException when there is an error in tokenization
     */
    public Parser0(Scanner s) throws ScanErrorException
    {
        scan = s;
        cur = scan.nextToken();
        variables = new HashMap<String, Integer>();
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
     * an integer.
     *
     * @return a number representing the number parsed
     * @throws ScanErrorException when there is an error in tokenization
     */
    private int parseNumber() throws ScanErrorException
    {
        int num;
        if (variables.containsKey(cur))
            num = variables.get(cur);
        else
            num = Integer.parseInt(cur);
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
     * @throws ScanErrorException when there is an error in tokenization
     */
    public void parseStatement() throws ScanErrorException
    {
        int num;
        if (cur.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            num = parseExpression();
            eat(")");
            eat(";");
            System.out.println(num);
        }
        else if (cur.equals("BEGIN"))
        {
            eat("BEGIN");
            while (!cur.equals("END"))
                parseStatement();
            eat("END");
            eat(";");
        }
        else if (!cur.equals("EOF"))
        {
            String id = cur;
            eat(cur);
            eat(":=");
            Integer val = parseExpression();
            eat(";");
            variables.put(id, val);
        }
    }

    /**
     * Parses and computes a factor, which consists of an expression surrounded by
     * parentheses (expr), a factor preceded by a minus sign -factor, a number num,
     * a variable id, or a procedure call. Returns an Expression object at the end.
     *
     * @return the numerical representation of the factor parsed and computed
     * @throws ScanErrorException when there is an error in tokenization
     */
    private int parseFactor() throws ScanErrorException
    {
        int num;
        if (cur.equals("("))
        {
            eat("(");
            num = parseExpression();
            eat(")");
        }
        else if (cur.equals("-"))
        {
            eat("-");
            num = -parseFactor();
        }
        else
            num = parseNumber();
        return num;
    }

    /**
     * Parses and computes a term, which consists of either a term multiplied
     * by a factor term*factor, a term divided by a factor term/factor, a term mod
     * a factor, or a factor. While the operator is *, /, or mod, the method
     * continuously parses the factors for this term. Returns an Expression
     * representing this term.
     *
     * @return the numerical representation of the term parsed and computed
     * @throws ScanErrorException when there is an error in tokenization
     */
    private int parseTerm() throws ScanErrorException
    {
        int num = parseFactor();
        while (cur.equals("*") || cur.equals("/") || cur.equals("mod"))
        {
            if (cur.equals("*"))
            {
                eat("*");
                num *= parseFactor();
            }
            else if (cur.equals("/"))
            {
                eat("/");
                num /= parseFactor();
            }
            else
            {
                eat("mod");
                num %= parseFactor();
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
     * @return the numerical representation of the expression parsed and computed
     * @throws ScanErrorException when there is an error in tokenization
     */
    private int parseExpression() throws ScanErrorException
    {
        int num = parseTerm();
        while (cur.equals("+") || cur.equals("-"))
        {
            if (cur.equals("+"))
            {
                eat("+");
                num += parseTerm();
            }
            else
            {
                eat("-");
                num -= parseTerm();
            }
        }
        return num;
    }
}
