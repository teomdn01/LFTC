import java.util.*;
import java.util.stream.Collectors;

public class FirstSet {
    private Map<String, Set<String>> firstSets;
    private final Grammar grammar;

    // get the first terminal from a production (if it has a terminal on the first position)
    private Set<String> getFirstTerminalsForNonterminal(String nonterminal) {
        return this.grammar.getProductionsForNonTerminal(List.of(nonterminal))
                .stream()
                .filter(rhs -> this.grammar.getTerminals().contains(rhs.get(0)))
                .map(rhs -> rhs.get(0))
                .collect(Collectors.toSet());
    }

    // if the element is a terminal or epsilon return a list containing only the element
    // otherwise the value form the map corresponding to the element
    public Set<String> getPreviousFirst(String element) {
        if (this.grammar.getTerminals().contains(element) || "EPS".equals(element)) {
            Set<String> newSet = new HashSet<>();
            newSet.add(element);
            return newSet;
        }
        return this.firstSets.get(element) == null ? new HashSet<>() : this.firstSets.get(element);
    }

    private Set<String> concatenationOfLength1(Set<String> l1, Set<String> l2) {
        Set<String> concatenationResult = new HashSet<>();
        l1.forEach(element -> {
            if (element.equals("EPS")) {
                concatenationResult.addAll(l2);
            }
            else {
                concatenationResult.add(element);
            }
        });
        return concatenationResult;
    }

    public Set<String> computeFIRSTConcatenationRHS(List<String> productionRHS) {
        if (productionRHS.size() == 0) {
            throw new RuntimeException("Empty production RHS");
        }

        if (productionRHS.size() == 1) {
            return this.getPreviousFirst(productionRHS.get(0));
        }

        Set<String> union = this.concatenationOfLength1(this.getPreviousFirst(productionRHS.get(0)), this.getPreviousFirst(productionRHS.get(1)));
        for (int i = 2; i < productionRHS.size(); i++) {
            union = this.concatenationOfLength1(union, this.getPreviousFirst(productionRHS.get(i)));
        }
        return union;
    }

    private void computeFirstSets() {
        this.firstSets = new HashMap<>();
        this.grammar.getNonTerminals().forEach(nonterminal -> this.firstSets.put(nonterminal, this.getFirstTerminalsForNonterminal(nonterminal)));

        boolean sameSets = false;
        while (!sameSets) {
            sameSets = true;
            Map<String, Set<String>> temporaryFirstSets = new HashMap<>();
            for (String nonterminal : this.grammar.getNonTerminals()) {
                temporaryFirstSets.put(nonterminal, new HashSet<>(this.firstSets.get(nonterminal)));
                this.grammar.getProductionsForNonTerminal(List.of(nonterminal))
                        .forEach(productionRHS -> temporaryFirstSets.get(nonterminal).addAll(this.computeFIRSTConcatenationRHS(productionRHS)));
                if (!temporaryFirstSets.get(nonterminal).equals(this.firstSets.get(nonterminal))) {
                    sameSets = false;
                }
            }
            this.firstSets = temporaryFirstSets;
        }
    }

    public FirstSet(Grammar grammar) {
        this.grammar = grammar;
        this.computeFirstSets();
    }

    public Map<String, Set<String>> getFirstSets() {
        return this.firstSets;
    }
}