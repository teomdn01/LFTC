import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseTable {
    private Grammar grammar;
    private FirstSet firstSet;
    private FollowSet followSet;

    private Map<String, Map<String, Pair<List<String>, Integer>>> parseTable;

    public ParseTable(Grammar grammar, FirstSet firstSet, FollowSet followSet) {
        this.grammar = grammar;
        this.firstSet = firstSet;
        this.followSet = followSet;
        this.parseTable = generateParseTable();
    }

    public Map<String, Map<String, Pair<List<String>, Integer>>> getParseTable() {
        return parseTable;
    }

    private Map<String, Map<String, Pair<List<String>, Integer>>> generateParseTable() {
        Map<String, Map<String, Pair<List<String>, Integer>>> table = new HashMap<>();
        this.applyFirstRule(table);
        this.applySecondRule(table);
        this.applyThirdRule(table);
        return table;
    }

    private void applyFirstRule(Map<String, Map<String, Pair<List<String>, Integer>>> table) {
        this.grammar.getProductions().forEach((LHSAsList, productionSet) ->
                productionSet.forEach(productionRHS -> {
                    String LHS = LHSAsList.get(0); // we know it's a CFG
                    int productionCode = this.grammar.findCodeForProduction(new Pair<>(LHS, productionRHS));
                    assert productionCode != -1;
                    table.putIfAbsent(LHS, new HashMap<>());

                    if (productionRHS.size() == 1 && productionRHS.get(0).equals("EPS")) {
                        this.followSet.getFollowSets().get(LHS)
                                .forEach(element -> {
                                    if (table.get(LHS).containsKey(element)) {
                                        throw new RuntimeException("Not LL1: " + LHS + " - " + element);
                                    }
                                    table.get(LHS).put(element, new Pair<>(List.of("EPS"), productionCode));
                                });
                    }
                    else {
                        this.firstSet.computeFIRSTConcatenationRHS(productionRHS).forEach(element -> {
                            if (table.get(LHS).containsKey(element)) {
                                throw new RuntimeException("Not LL1: " + LHS + " - " + element);
                            }
                            productionRHS.remove("EPS");
                            table.get(LHS).put(element, new Pair<>(productionRHS, productionCode));
                        });
                    }
                }));
    }

    private void applySecondRule(Map<String, Map<String, Pair<List<String>, Integer>>> table) {
        this.grammar.getTerminals().forEach(terminal -> {
            table.putIfAbsent(terminal, new HashMap<>());
            table.get(terminal).put(terminal, new Pair<>(List.of("POP"), -1)); //POP_CODE
        });
    }

    private void applyThirdRule(Map<String, Map<String, Pair<List<String>, Integer>>> table) {
        table.putIfAbsent("$", Map.of("$", new Pair<>(List.of("ACC"), -2))); // ACCEPTED_CODE
    }
}
