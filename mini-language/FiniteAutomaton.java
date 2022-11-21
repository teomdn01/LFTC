import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FiniteAutomaton {
    private final String SEPARATOR = ";";
    private final String TRANSITION_SEPARATOR = "->";

    private String initialState;
    private List<String> states;
    private List<String> alphabet;
    private Map<String, Set<Pair<String, String>>> transitions;
    private List<String> finalStates;
    private boolean isDFA;

    public FiniteAutomaton(String fileName) {
        this.loadFA(fileName);
    }

    private void loadFA(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            this.states = new ArrayList<>(List.of(scanner.nextLine().split(this.SEPARATOR)));
            this.initialState = this.states.get(0);
            this.alphabet = new ArrayList<>(List.of(scanner.nextLine().split(this.SEPARATOR)));
            this.transitions = new HashMap<>();
            String[] transitionStrings = scanner.nextLine().split(this.SEPARATOR);
            for (String transition : transitionStrings) {
                String[] tokens = transition.split(this.TRANSITION_SEPARATOR);
                this.transitions.putIfAbsent(tokens[0], new HashSet<>());
                this.transitions.get(tokens[0]).add(new Pair<>(tokens[2], tokens[1]));
            }

            this.finalStates = new ArrayList<>(List.of(scanner.nextLine().split(this.SEPARATOR)));
            this.isDFA = checkIfDFA();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfDFA() {
        for (String state : transitions.keySet()) {
            long uniqueSymbols = this.transitions.get(state).stream().map(Pair::getSecond).distinct().count();
            if (uniqueSymbols != this.transitions.get(state).size()) {
                return false;
            }
        }
        return true;
    }

    public boolean isAccepted(String sequence) {
        if (!this.isDFA) {
            return false;
        }

        String currentState = this.initialState;
        for (int i = 0; i < sequence.length(); i++) {
            String currentSymbol = sequence.substring(i, i + 1);
            Set<Pair<String, String>> nextTransitions = this.transitions.get(currentState);
            if (nextTransitions == null) {
                return false;
            }

            boolean foundNext = false;
            for (Pair<String, String> stateSymbolPair : nextTransitions) {
                if (stateSymbolPair.getSecond().equals(currentSymbol)) {
                    currentState = stateSymbolPair.getFirst();
                    foundNext = true;
                    break;
                }
            }

            if (!foundNext) {
                return false;
            }
        }

        return this.finalStates.contains(currentState);
    }

    public void printData() {
        System.out.println("Initial states: { " + Arrays.toString(states.toArray()) + " }");
        System.out.println("Alphabet: { " + Arrays.toString(alphabet.toArray()) + " }");
        System.out.println("Transitions: ");
        for (Map.Entry<String, Set<Pair<String, String>>> entry : transitions.entrySet()) {
            System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue().toArray()));
        }
        System.out.println("Initial state: " + initialState);
        System.out.println("Final states: { " + Arrays.toString(finalStates.toArray()) + " }");
        System.out.println("======================================");
        System.out.println();
    }
}
