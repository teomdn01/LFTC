import java.util.Objects;

public class Pair<F, S> {
    private final F first;
    private final S second;

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + this.first.toString() + ", " + this.second.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> position = (Pair<?, ?>) o;
        return first.equals(position.first) && second.equals(position.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}