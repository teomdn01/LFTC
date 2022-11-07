import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MiniScanner {
    private final ArrayList<String> OPERATORS = new ArrayList<>();
    private final ArrayList<String> SEPARATORS = new ArrayList<>();
    private final ArrayList<String> RESERVED_WORDS = new ArrayList<>();
    private final String problemFilePath;
    private final SymbolTable symbolTable;
    private final PIF pif;

    public MiniScanner(String problemFilePath) {
        this.problemFilePath = problemFilePath;
        this.symbolTable = new SymbolTable(256);
        initializeLexic("IO/token.txt");
        this.pif = new PIF();
    }

    private void initializeLexic(String tokenFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(tokenFilePath))) {
            String line = reader.readLine();
            while (line != null) {
                if (line.equals("OPERATORS")) {
                    line = reader.readLine();
                    while (!line.equals("SEPARATORS")) {
                        OPERATORS.add(line.strip());
                        line = reader.readLine();
                    }
                    line = reader.readLine();
                    while (!line.equals("RESERVED_WORDS")) {
                        if (line.equals("space"))
                            SEPARATORS.add(" ");
                        else if (line.equals("\\n"))
                            SEPARATORS.add("\n");
                        else
                            SEPARATORS.add(line.strip());

                        line = reader.readLine();
                    }
                    line = reader.readLine();
                    while (line != null) {
                        RESERVED_WORDS.add(line.strip());
                        line = reader.readLine();
                    }
                }
                // read next line
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile() throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(this.problemFilePath));
        while (scanner.hasNextLine()) {
            fileContent.append(scanner.nextLine()).append("\n");
        }
        return fileContent.toString().replace("\t", "");
    }


    private List<Pair<String, Integer>> tokenize() {
        try {
            String fileContent = this.readFile();
            String separators = this.SEPARATORS.stream().reduce("", (a, b) -> a + b);
            List<String> tokensIncludingSeparators = Collections.list(new StringTokenizer(fileContent, separators, true)).stream()
                    .map(token -> (String) token)
                    .collect(Collectors.toList());
            return this.tokenizeWithCompleteStringsAndLineNumbers(tokensIncludingSeparators);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Pair<String, Integer>> tokenizeWithCompleteStringsAndLineNumbers(List<String> tokensIncludingSeparators) {
        List<Pair<String, Integer>> tokensWithCompleteStrings = new ArrayList<>();
        boolean inString = false;
        StringBuilder currentString = new StringBuilder();
        int lineNumber = 1;

        for (String token : tokensIncludingSeparators) {
            if (token.equals("\"")) {
                currentString.append(token);
                if (inString) { // end of string
                    tokensWithCompleteStrings.add(new Pair<>(currentString.toString(), lineNumber));
                    currentString = new StringBuilder();
                }
                inString = !inString;
            }
            else if (token.equals("\n")) {
                lineNumber++;
            }
            else {
                if (inString) { // add the current token to the string
                    currentString.append(token);
                }
                else if (!token.equals(" ")) {
                    tokensWithCompleteStrings.add(new Pair<>(token, lineNumber));
                }
            }
        }
        return tokensWithCompleteStrings;
    }

    public void scan() {
        AtomicBoolean foundLexicalError = new AtomicBoolean(false);
        List<Pair<String, Integer>> tokens = this.tokenize();
        tokens.forEach(tokenPair -> {
            String token = tokenPair.getFirst();
            if (this.RESERVED_WORDS.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), -1);
            }
            else if (this.SEPARATORS.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), -1);
            }
            else if (this.OPERATORS.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), -1);
            }
            else if (isIdentifier(token)) {
                this.symbolTable.add(token);
                this.pif.add(new Pair<>(token, symbolTable.findTermPosition(token)), 0);
            }
            else if (isConstant(token)) {
                this.symbolTable.add(token);
                this.pif.add(new Pair<>(token, symbolTable.findTermPosition(token)), 1);
            }
            else {
                System.out.printf("Found lexical error for %s at line %d%n", token, tokenPair.getSecond());
                foundLexicalError.set(true);
            }
        });

        if (foundLexicalError.get())
            System.exit(0);
    }

    private static boolean isIdentifier(String token) {
        return token.matches("^[a-z]+[a-z|A-z|0-9]*$");
    }

    private static boolean isConstant(String token) {
        return token.matches("TRUE|FALSE") // boolean
                || token.matches("^(POZ@0|(NEG|POZ)@[1-9]+[0-9]*)@$") // integers
                || token.matches("^\"[a-z|A-Z|0-9| |_]+\"$"); // non-empty strings containing letters, digits or _
    }

    public void printSymbolTable(String outputFile) {
        try(PrintStream printStream = new PrintStream(outputFile)) {
            printStream.println(symbolTable.toString());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printPIF(String outputFile) {
        try(PrintStream printStream = new PrintStream(outputFile)) {
            printStream.println(pif.toString());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
