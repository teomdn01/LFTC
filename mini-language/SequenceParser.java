import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SequenceParser {
    private List<String> inputStack;
    private List<String> workingStack;
    private List<Integer> output;
    private String startingSymbol;
    private ParseTable parseTable;

    public SequenceParser(String startingSymbol, ParseTable parseTable) {
        output = new ArrayList<>();
        workingStack = new ArrayList<>();
        this.startingSymbol = startingSymbol;
        this.parseTable = parseTable;
    }

    public List<Integer> evaluateSequence(List<String> sequence) {
        inputStack = new ArrayList<>(sequence);
        inputStack.add("$");

        workingStack.add(this.startingSymbol);
        workingStack.add("$");

        boolean finished = false;
        while (! finished) {
            String inputTop = inputStack.get(0);
            String workingStackTop = workingStack.remove(0);

            if (this.parseTable.getParseTable().containsKey(workingStackTop)) {
                if (this.parseTable.getParseTable().get(workingStackTop).containsKey(inputTop)) {
                    Pair<List<String>, Integer> tableEntry = this.parseTable.getParseTable().get(workingStackTop).get(inputTop);

                    if (tableEntry.getSecond() == -1) { // pop code
                        inputStack.remove(0);
                    }
                    else if (tableEntry.getSecond() == -2 && inputTop.equals("$") && workingStackTop.equals("$")) { // accept
                        finished = true;
                    }
                    else { // push
                        for (int i = tableEntry.getFirst().size() - 1; i >= 0; i--) {
                            String symbol = tableEntry.getFirst().get(i);
                            if (! symbol.equals("EPS")) {
                                workingStack.add(0, symbol);
                            }
                        }
                        output.add(tableEntry.getSecond());
                    }
                }
                else {
                    throw new RuntimeException(inputTop + " is not a valid terminal. No parsing table entry available for pair " + workingStackTop + "-" + inputTop);
                }
            }
            else {
                throw new RuntimeException(workingStackTop + " is not a valid terminal / nonterminal");
            }
        }
        return output;
    }
}
