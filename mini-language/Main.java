public class Main {
    public static void main(String[] args) {
        String baseFilename = "perr";
        MiniScanner scanner = new MiniScanner("IO/" + baseFilename + ".txt");
        scanner.scan();
        scanner.printSymbolTable("IO/" + baseFilename + "ST.txt");
        scanner.printPIF("IO/" + baseFilename + "PIF.txt");;
        System.out.println("Done");
    }
}
