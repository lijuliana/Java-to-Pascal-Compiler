package scanner;

import java.io.*;

/**
 * Scanner is a simple scanner for Compilers and Interpreters lab exercise 1
 * @author Juliana Li
 * @version August 30, 2023
 *
 * Usage:
 * Scanner takes an input file of text, reads characters one by one, tokenizes
 * these characters, and returns them in the nextToken() function.
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;
    /**
     * Scanner constructor for construction of a scanner that
     * uses an InputStream object for input.
     * Usage:
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }

    /**
     * Scanner constructor for constructing a scanner that
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * This method retrieves the next character from the input and sets
     * currentChar to this character. Catches an error and exits the program if
     * there is an IOException.
     */
    private void getNextChar()
    {
        try
        {
            int nextChar = in.read();
            if (nextChar == -1)
                eof = true;
            currentChar = (char) nextChar;
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * This method compares a parameter character with the current character and then advances
     * to the next character.
     *
     * @param expected the character expected to be the current char
     * @exception ScanErrorException if the current char is not the expected char
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (currentChar == expected)
            getNextChar();
        else
            throw new ScanErrorException("Illegal character - expected " + expected +
                    " and found " + currentChar + ".");
    }

    /**
     * This method checks if there is a next character to be read by returning
     * the opposite of the eof boolean.
     *
     * @return true if end of file not reach, false otherwise
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * This method checks if the given char is a digit.
     *
     * @param c char to check
     * @return true if char is a digit, false otherwise
     */
    public static boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }

    /**
     * This method checks if the given char is a letter.
     *
     * @param c char to check
     * @return true if char is a letter, false otherwise
     */
    public static boolean isLetter(char c)
    {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * This method checks if the given char is a white space. A white space
     * is defined as one of: \n \t \r or a space.
     *
     * @param c char to check
     * @return true if char is a white space, false otherwise
     */
    public static boolean isWhiteSpace(char c)
    {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    /**
     * This method checks if the given char is an operand. An operand
     * is defined as one of: '=', '+', '-', '*', '/', '%', '(', ')'
     *
     * @param c char to check
     * @return true if char is an operand, false otherwise
     */
    public static boolean isOperand(char c)
    {
        char[] operands = new char[]{'=', '+', '-', '*', '/', '%', '(', ')', '<', '>', ','};
        for (char oper : operands)
            if (c == oper)
                return true;
        return false;
    }

    /**
     * This method checks if the given char is the first character of a two-symbol
     * operand. A long operand can start with any of: '<', '>', ':'
     *
     * @param c char to check
     * @return true if char is start of a long operand, false otherwise
     */
    public static boolean isLongOperand(char c)
    {
        char[] operands = new char[]{'<', '>', ':'};
        for (char oper : operands)
            if (c == oper)
                return true;
        return false;
    }

    /**
     * This method continuously scans digits and concatenates them into a number,
     * or throws an error if a number is not found.
     *
     * @return a string representing a number made up of one or more digits
     * @throws ScanErrorException when the scanned characters are not a number
     */
    private String scanNumber() throws ScanErrorException
    {
        String number = "";
        boolean isNumber = false;
        while (isDigit(currentChar))
        {
            isNumber = true;
            number += currentChar;
            if (hasNext())
                eat(currentChar);
            else
                return number;
        }
        if (!isNumber)
            throw new ScanErrorException("Not a number");
        return number;
    }

    /**
     * This method scans a letter and then continuously scans letters or digits
     * and concatenates them into an identifier, or throws an error if a letter
     * is not found as the first digit.
     *
     * @return a string representing an identifier
     * @throws ScanErrorException when an identifier is not found
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String identifier = "";
        if (isLetter(currentChar))
            identifier += currentChar;
        else
            throw new ScanErrorException("Not an identifier");
        if (hasNext())
            eat(currentChar);
        else
            return identifier;
        while (isLetter(currentChar) || isDigit(currentChar))
        {
            identifier += currentChar;
            if (hasNext())
                eat(currentChar);
            else
                return identifier;
        }
        return identifier;
    }

    /**
     * This method scans an operand and returns it, or throws an error if an
     * operand is not found.
     *
     * @return a string representing an operand
     * @throws ScanErrorException when the scanned character is not an operand
     */
    private String scanOperand() throws ScanErrorException
    {
        String operand = "" + currentChar;
        // special operators
        if (isLongOperand(currentChar))
        {
            if (hasNext())
                eat(currentChar);
            if ((operand.equals("<") && currentChar == '=') || (operand.equals(">") && currentChar == '=') ||
                    (operand.equals("<") && currentChar == '>') || (operand.equals(":") && currentChar == '=')) {
                operand += currentChar;
                eat(currentChar);
            }
            return operand;
        }
        if (isOperand(currentChar))
        {
            if (hasNext())
                eat(currentChar);
            return operand;
        }
        throw new ScanErrorException("Not an operand: " + operand);
    }

    /**
     * This method gets the next token in the input stream by removing all whitespaces,
     * comments, and unrecognized characters.
     *
     * @return a string representing a token: number, identifier, operand,
     * or end of file
     * @throws ScanErrorException when one of the scan functions does not find
     * a number/identifier/operand, respectively
     */
    public String nextToken() throws ScanErrorException
    {
        if (hasNext())
        {
            while (isWhiteSpace(currentChar))
                eat(currentChar);
            try
            {
                // comments
                while (currentChar == '/')
                {
                    if (hasNext())
                        eat(currentChar);
                    if (currentChar == '/')
                    {
                        while (currentChar != '\n' && hasNext())
                            eat(currentChar);
                        eat(currentChar);
                    }
                    else
                        return "/";
                }

                // pascal multiline comments
                if (currentChar == '(')
                {
                    if (hasNext())
                        eat(currentChar);
                    if (currentChar == '*')
                    {
                        while (true)
                        {
                            if (hasNext())
                                eat(currentChar);
                            if (currentChar == '*')
                            {
                                if (hasNext())
                                    eat(currentChar);
                                if (currentChar == ')')
                                {
                                    if (hasNext())
                                        eat(currentChar);
                                    break;
                                }
                            }
                        }
                    }
                    else
                        return "(";
                }
                while (isWhiteSpace(currentChar))
                    eat(currentChar);
                // digits, letters, operands
                if (isDigit(currentChar))
                    return scanNumber();
                if (isLetter(currentChar))
                    return scanIdentifier();
                if (isOperand(currentChar) || isLongOperand(currentChar))
                    return scanOperand();
                // end of line
                if (currentChar == ';')
                {
                    eat(currentChar);
                    return ";";
                }
            }
            catch (ScanErrorException e)
            {
                e.printStackTrace();
            }
            if (currentChar == '.')
                eof = true;
            if (eof)
                return "EOF";
            // unrecognized character
            throw new ScanErrorException("Character not recognized: " + currentChar);
        }
        // if no tokens to return, return end of file
        return "EOF";
    }
}
