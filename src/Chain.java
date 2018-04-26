import java.util.ArrayList;

public class Chain {
    private static ArrayList<Square> whites;

    public static ArrayList<Square> getWhites() {
        return whites;
    }

    public Chain() {
        whites = new ArrayList<>();
    }

    public void addSquare(Square square) {
        whites.add(square);
        square.setChain(this);
    }

    public int tape() {
        return whites.size();
    }
}
