import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SymbolTable {
    private int size;
    private List<List<String>> table;

    public SymbolTable(int size) {
        this.size = size;
        this.table = new ArrayList<>();
        for (int i = 0; i < this.size; i++) {
            this.table.add(new ArrayList<>());
        }
    }
    private int hash(String key) {
        int ASCIISum = 0;
        for (int i = 0; i < key.length(); i++) {
            ASCIISum = (ASCIISum + key.charAt(i)) % size;
        }
        return ASCIISum % size;
    }

    public Pair add(String term) {
        Pair position = findTermPosition(term);
        if (Objects.nonNull(position)) {
            return position;
        }

        int key = this.hash(term);
        this.table.get(key).add(term);
        return new Pair(key, table.get(key).size() - 1);
    }


    public Pair findTermPosition(String term) {
        int key = this.hash(term);
        if (!this.table.get(key).isEmpty()) {
            List<String> collisions = this.table.get(key);

            for (int i = 0; i < collisions.size(); i++) {
                if (collisions.get(i).equals(term)) {
                    return new Pair(key, i);
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("");
        for (int row = 0; row < table.size(); ++row) {
            if (table.get(row).size() > 0) {
                int finalRow = row;
                for (int col = 0; col < table.get(row).size(); ++col) {
                    result.append(table.get(finalRow).get(col) + " - <" + finalRow + ", " + col + ">\n");
                }
            }
        }

        return result.toString();
    }
}

