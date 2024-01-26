package scanner;

import java.io.*;

/**
 * Tests the Scanner class by running it on a text file and tokenizing the text.
 *
 * @author Anu Datar
 * @version 05/17/2018
 */
public class ScannerTester
{
    /**
     *  Main tester method
     *
     * @param  str array of String objects
     */
    public static void main(String[] str) throws IOException, ScanErrorException {
        FileInputStream inStream = new FileInputStream(new File("src/scanner/testing.txt"));
        Scanner scanner = new Scanner(inStream);

        String nextToken = "";
        while(scanner.hasNext() && !nextToken.equals("END")) {
            nextToken = scanner.nextToken();
            System.out.println(nextToken);
        }
    }
}

//public class ScannerTester
//{
//    /**
//     * Main tester method
//     *
//     * @param  args array of String objects
//     * @throws FileNotFoundException if the input file is not found
//     * @throws ScanErrorException if scanner throws an exception
//     */
//    public static void main(String[] args) throws IOException, FileNotFoundException
//    {
//        FileReader in = new FileReader(new File("/Users/julianali/IdeaProjects/Compiler_Li/src/scanner/jflextest.txt"));
//        ScannerJFlex scanner = new ScannerJFlex(in);
//
//        String token = scanner.nextToken();
//
//        try
//        {
//            while(!scanner.yyatEOF())
//            {
//                System.out.println(token);
//                token = scanner.nextToken();
//            }
//            System.out.println("END");
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            System.exit(-1);
//        }
//    }
//}