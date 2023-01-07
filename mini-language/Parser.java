import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private Grammar grammar;
    private FirstSet firstSet;
    private FollowSet followSet;
    private ParseTable parseTable;
    private SequenceParser sequenceParser;

    public Parser(Grammar grammar, FirstSet firstSet, FollowSet followSet) {
        this.grammar = grammar;
        this.firstSet = firstSet;
        this.followSet = followSet;
        this.parseTable = new ParseTable(grammar, firstSet, followSet);
        this.sequenceParser = new SequenceParser(grammar.getStartingSymbol(), parseTable);
    }

    public void parse(List<String> tokens) {
        this.firstSet.getFirstSets().forEach((a, b) -> System.out.println("First("+a +"): " + b));
        this.followSet.getFollowSets().forEach((a, b) -> System.out.println("Follow("+a +"): " + b));
        this.parseTable.getParseTable().forEach((a, b) -> b.forEach((c, d) -> System.out.print("[" + a + "][" + c + "] = " + d + "\n")));

        List<Integer> sequenceProductionCodes = this.sequenceParser.evaluateSequence(tokens);
        System.out.println(sequenceProductionCodes);

        OutputTree outputTree = new OutputTree(grammar, sequenceProductionCodes);
        outputTree.printTree();
    }

}
