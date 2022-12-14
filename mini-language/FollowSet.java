//If S is the start symbol, FOLLOW (S) ={$}
//If production is of form A → α B β, β ≠ ε.

//(a) If FIRST (β) does not contain ε then, FOLLOW (B) = {FIRST (β)}
//
//Or
//
//(b) If FIRST (β) contains ε (i. e. , β ⇒* ε), then
//FOLLOW (B) = FIRST (β) − {ε} ∪ FOLLOW (A)
//∵ when β derives ε, then terminal after A will follow B.
//If production is of form A → αB, then Follow (B) ={FOLLOW (A)}.

import java.util.*;
import java.util.stream.Collectors;

public class FollowSet {
    private Map<String, Set<String>> followSets;
    private final Grammar grammar;
    private final FirstSet firstSets;

    public Map<String, Set<List<String>>> findProductionsWhichContainNonterminal(String nonterminal) {
        Map<String, Set<List<String>>> productionsWhichContainNonterminal = new HashMap<>();
        this.grammar.getProductions().forEach((productionLHS, productionRHS) -> {
            Set<List<String>> productionRHSWhichStartFromCurrentNonterminal = productionRHS
                    .stream()
                    .filter(possibleProductionRHS -> possibleProductionRHS.contains(nonterminal))
                    .collect(Collectors.toSet());
            if (! productionRHSWhichStartFromCurrentNonterminal.isEmpty()) {
                productionsWhichContainNonterminal.put(productionLHS.get(0), productionRHSWhichStartFromCurrentNonterminal);
            }
        });
        return productionsWhichContainNonterminal;
    }

    private void processPossibleProductionRHS(Map<String, Set<String>> temporaryFollowSets, String nonterminal, List<String> possibleProductionRHS, String LHSNonterminal) {
        List<String> elementsAfterNonterminalFirstOccurrence = possibleProductionRHS.subList(possibleProductionRHS.indexOf(nonterminal) + 1, possibleProductionRHS.size());
        if (elementsAfterNonterminalFirstOccurrence.size() == 0) {
            elementsAfterNonterminalFirstOccurrence.add("EPS");
        }

        Set<String> FIRST = this.firstSets.computeFIRSTConcatenationRHS(elementsAfterNonterminalFirstOccurrence);
        FIRST.forEach(element -> {
            if (element.equals("EPS")) {
                temporaryFollowSets.get(nonterminal).addAll(this.followSets.get(LHSNonterminal));
            }
            else {
                Set<String> previousFIRST = this.firstSets.getPreviousFirst(element);
                previousFIRST.remove("EPS");
                temporaryFollowSets.get(nonterminal).addAll(previousFIRST);
            }
        });
    }

    private void processNonterminal(Map<String, Set<String>> temporaryFollowSets, String nonterminal) {
        temporaryFollowSets.put(nonterminal, new HashSet<>(this.followSets.get(nonterminal)));

        Map<String, Set<List<String>>> productionsWhichContainNonterminal = this.findProductionsWhichContainNonterminal(nonterminal);
        productionsWhichContainNonterminal
                .forEach((LHSNonterminal, productionRHS) -> productionRHS
                        .forEach(possibleProductionRHS -> this.processPossibleProductionRHS(temporaryFollowSets, nonterminal, possibleProductionRHS, LHSNonterminal)));
    }

    private void initializeFollowSets() {
        this.grammar.getNonTerminals().forEach(nonterminal -> {
            Set<String> newSet = new HashSet<>();
            if (nonterminal.equals(this.grammar.getStartingSymbol())) {
                newSet.add("$");
            }
            this.followSets.put(nonterminal, newSet);
        });
    }

    private void computeFollowSets() {
        this.followSets = new HashMap<>();
        this.initializeFollowSets();

        boolean sameSets = false;
        while (! sameSets) {
            sameSets = true;
            Map<String, Set<String>> temporaryFollowSets = new HashMap<>();
            for (String nonterminal : this.grammar.getNonTerminals()) {
                processNonterminal(temporaryFollowSets, nonterminal);
                if (! temporaryFollowSets.get(nonterminal).equals(this.followSets.get(nonterminal))) {
                    sameSets = false;
                }
            }
            this.followSets = temporaryFollowSets;
        }
    }

    public FollowSet(Grammar grammar, FirstSet firstSets) {
        this.grammar = grammar;
        this.firstSets = firstSets;
        this.computeFollowSets();
    }

    public Map<String, Set<String>> getFollowSets() {
        return this.followSets;
    }
}