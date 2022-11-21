import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        FiniteAutomaton faLab = new FiniteAutomaton("IO/FA.in");

        String baseFilename = "p3";
        FiniteAutomaton identifierFA = new FiniteAutomaton("IO/FAIdentifier.in");
        FiniteAutomaton constantFA = new FiniteAutomaton("IO/FAConstant.in");
        MiniScanner scanner = new MiniScanner("IO/" + baseFilename + ".txt", identifierFA, constantFA);
        while (true) {
            System.out.println("Select option: 1 -> view FAs, 2 -> run scanner");
            Scanner scannerIO = new Scanner(System.in);
            int option = scannerIO.nextInt();
            if (option == 1) {
                identifierFA.printData();
                constantFA.printData();
            }
            else if (option == 2) {
                scanner.scan();
                scanner.printSymbolTable("IO/" + baseFilename + "ST.txt");
                scanner.printPIF("IO/" + baseFilename + "PIF.txt");;
                System.out.println("Done");
            }
        }

    }
}
