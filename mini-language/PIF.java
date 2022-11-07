import java.util.ArrayList;
import java.util.List;

public class PIF {
    private final List<Pair<String, Pair<Integer, Integer>>> tokenPositionPairs;
    private final List<Integer> type;

    public PIF() {
        this.tokenPositionPairs = new ArrayList<>();
        this.type = new ArrayList<>();
    }

    public void add(Pair<String, Pair<Integer, Integer>> element, Integer type) {
        this.tokenPositionPairs.add(element);
        this.type.add(type);

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.tokenPositionPairs.size(); i++) {
            int tokenType = this.type.get(i);
            if (tokenType == 0) {
                stringBuilder.append("id - " + tokenPositionPairs.get(i).getSecond());
            }
            else if (tokenType == 1) {
                stringBuilder.append("const - " + tokenPositionPairs.get(i).getSecond());
            }
            else {
                stringBuilder.append(this.tokenPositionPairs.get(i).getFirst() + " - " + new Pair<>(-1, -1));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString().stripTrailing();
    }

}
