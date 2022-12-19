import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private Grammar grammar;
    private FirstSet firstSet;
    private FollowSet followSet;
    private ParseTable parseTable;

    public Parser(Grammar grammar, FirstSet firstSet, FollowSet followSet) {
        this.grammar = grammar;
        this.firstSet = firstSet;
        this.followSet = followSet;
        this.parseTable = new ParseTable(grammar, firstSet, followSet);
        //TODO: create sequence evaluator
    }

    public void parse(List<String> tokens) {
        this.firstSet.getFirstSets().forEach((a, b) -> System.out.println("First("+a +"): " + b));
        this.followSet.getFollowSets().forEach((a, b) -> System.out.println("Follow("+a +"): " + b));
        this.parseTable.getParseTable().forEach((a, b) -> b.forEach((c, d) -> System.out.print("[" + a + "][" + c + "] = " + d + "\n")));
    }

}
