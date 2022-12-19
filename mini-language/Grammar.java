import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private static String TERMINAL_AND_NON_TERMINAL_SEPARATOR = "@";
    private static String LEFT_HAND_SIDE_TO_RIGHT_SEPARATOR = "->";
    private static String TRANSITION_OR_SEPARATOR = "\\|";
    private static String TRANSITION_CONCATENATION = " ";


    private Set<String> nonTerminals;
    private Set<String> terminals;
    private String startingSymbol;
    private HashMap<List<String>, List<List<String>>> productions = new HashMap<>();
    private List<Pair<String, List<String>>> productionCodes;

    public Grammar(String filename) {
        this.loadFromFile(filename);
        this.assignCodesToProductions();
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    public HashMap<List<String>, List<List<String>>> getProductions() {
        return productions;
    }

    private void loadFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))){
            nonTerminals = new HashSet<>(List.of(scanner.nextLine().split(TERMINAL_AND_NON_TERMINAL_SEPARATOR)));
            terminals = new HashSet<>(List.of(scanner.nextLine().split(TERMINAL_AND_NON_TERMINAL_SEPARATOR)));
            startingSymbol = scanner.nextLine();


            while (scanner.hasNextLine()) {
                processProduction(scanner.nextLine());

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processProduction(String production) {
        String[] line = production.split(LEFT_HAND_SIDE_TO_RIGHT_SEPARATOR);
        List<String> leftHandSide = List.of(line[0].split(TRANSITION_CONCATENATION));
        this.productions.putIfAbsent(leftHandSide, new ArrayList<>());
        String rightHandSide = line[1];
        String[] rightSplit = rightHandSide.split(TRANSITION_OR_SEPARATOR);
        Arrays.stream(rightSplit).forEach(rs -> this.productions.get(leftHandSide)
                .add(Arrays.stream(rs.split(TRANSITION_CONCATENATION))
                .collect(Collectors.toList())));
    }

    public List<List<String>> getProductionsForNonTerminal(List<String> nonTerminal) {
        if(this.productions.get(nonTerminal) != null){
            return this.productions.get(nonTerminal);
        }
        return new ArrayList<>();
    }

    public boolean performCFGCheck() {
        if (!nonTerminals.contains(startingSymbol)) {
            return false;
        }

        for (List<String> key : productions.keySet()) {
            if (key.size() != 1 || !nonTerminals.contains(key.get(0))) {
                return false;
            }
        }

        return true;
    }

    private void assignCodesToProductions() {
        this.productionCodes = new ArrayList<>();
        this.productions.forEach((LHS, RHSSet) -> RHSSet.forEach(RHS -> this.productionCodes.add(new Pair<>(LHS.get(0), RHS))));
    }

    public int findCodeForProduction(Pair<String, List<String>> production) {
        return productionCodes.indexOf(production);
    }
}
