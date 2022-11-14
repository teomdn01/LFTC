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

    public boolean isAccepted() {
        //TODO: define the acceptance criteria
        return true;
    }
}
