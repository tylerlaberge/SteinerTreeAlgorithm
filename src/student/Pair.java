package student;

/**
 * A class to represent a pair of elements.
 *
 * @param <A>: The type of the first element of the pair.
 * @param <B>: The type of the second elements of the pair.
 */
public class Pair<A, B> {
    private A first;
    private B second;

    /**
     * Create a new Pair object.
     *
     * @param first: The first element of the pair.
     * @param second: The second element of the pair.
     */
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Get the first element of the pair.
     *
     * @return The first element of the pair.
     */
    public A getFirst() {
        return this.first;
    }

    /**
     * Get the second element of the pair.
     *
     * @return The second element of the pair.
     */
    public B getSecond() {
        return this.second;
    }
}
