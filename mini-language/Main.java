import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

       // FiniteAutomaton faLab = new FiniteAutomaton("IO/FA.in");
        Grammar grammar = new Grammar("IO/G3.txt");

        String baseFilename = "p3";
        FiniteAutomaton identifierFA = new FiniteAutomaton("IO/FAIdentifier.in");
        FiniteAutomaton constantFA = new FiniteAutomaton("IO/FAConstant.in");
        MiniScanner scanner = new MiniScanner("IO" + baseFilename + ".txt", identifierFA, constantFA);

        FirstSet firstSet = new FirstSet(grammar);
        FollowSet followSet = new FollowSet(grammar, firstSet);
        Parser parser = new Parser(grammar, firstSet, followSet);

        parser.parse(loadSequenceFromFile());
        System.out.println("=============================================================");
     //   System.out.println(firstSet.getFirstSets());
        while (true) {
            System.out.println("Select option: ");
            System.out.println("1 -> View FAs ");
            System.out.println("2 -> Run scanner");
            System.out.println("3 -> Print grammar set of non terminals");
            System.out.println("4 -> Print grammar set of terminals");
            System.out.println("5 -> Print grammar set of productions");
            System.out.println("6 -> Print grammar productions for a given non terminal");
            System.out.println("7 -> Perform grammar CFG check");

            Scanner scannerIO = new Scanner(System.in);
            int option = scannerIO.nextInt();
            if (option == 1) {
                identifierFA.printData();
                constantFA.printData();
            }
            else if (option == 2) {
                scanner.scan();
                scanner.printSymbolTable("D:\\LFTC\\mini-language\\IO\\" + baseFilename + "ST.txt");
                scanner.printPIF("D:\\LFTC\\mini-language\\IO\\" + baseFilename + "PIF.txt");;
                System.out.println("Done");
            }
            else if (option == 3) {
                System.out.println(grammar.getNonTerminals());
            }
            else if (option == 4) {
                System.out.println(grammar.getTerminals());
            }
            else if (option == 5) {
                System.out.println(grammar.getProductions());
            }
            else if (option == 6) {
                scannerIO.nextLine();
                System.out.println("Choose non terminal: " + grammar.getNonTerminals());
                String nonTerminal = scannerIO.nextLine();
                System.out.println(grammar.getProductionsForNonTerminal(List.of(nonTerminal)));
            }
            else if (option == 7) {
                System.out.println(grammar.performCFGCheck());
            }
        }

    }

    private static List<String> loadSequenceFromFile() throws FileNotFoundException {
        List<String> sequence = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("IO/seq_error.txt"))) {
            while (scanner.hasNextLine()) {
                sequence.add(scanner.nextLine());
            }
        }
        return sequence;
    }
}
