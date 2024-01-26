package parser;
import ast.*;
import scanner.ScanErrorException;
import scanner.Scanner;
import java.io.*;
import environment.Environment;

/**
 * Tests the Parser class by running it on a text file and continuously parsing the statements.
 *
 * @author Juliana
 * @version 10/17/23
 */
public class ParserTester
{
    /**
     * Main tester method that runs the parser on a text file. Continuously parses statements
     * and executes them.
     *
     * @param str array of String objects
     * @throws IOException for input/output exceptions
     * @throws ScanErrorException when there is an error in scanning
     */
    public static void main(String[] str) throws IOException, ScanErrorException {
        FileInputStream inStream = new FileInputStream(new File("src/parser/parserTest/testing0.txt"));
        Scanner scanner = new Scanner(inStream);
        Parser parser = new Parser(scanner);
        Environment env = new Environment(null);

        Program p = parser.parseProgram();
        p.exec(env);
        p.compile("src/parser/output.asm");
    }
}