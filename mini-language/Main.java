public class Main {
    public static void main(String[] args) {

        FiniteAutomaton faLab = new FiniteAutomaton("IO/FA.in");

        String baseFilename = "p2";
        MiniScanner scanner = new MiniScanner("IO/" + baseFilename + ".txt");
        scanner.scan();
        scanner.printSymbolTable("IO/" + baseFilename + "ST.txt");
        scanner.printPIF("IO/" + baseFilename + "PIF.txt");;
        System.out.println("Done");
    }
}
