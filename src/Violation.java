import java.util.ArrayList;

public class Violation {
    private Boolean expression;
    private String clause;
    private ArrayList<Square> squares;
    private int lastModifier = Integer.MAX_VALUE;

    public Violation(Boolean expression, String clause, ArrayList<Square> squares) {
        this.expression = expression;
        this.clause = clause;
        this.squares = squares;
    }

    public Boolean getExpression() {
        return expression;
    }

    public String getClause() {
        return clause;
    }

    public Boolean isSatisfied() {
        for (Square square : squares) {
            if (square.satisfied() != -1) {
                return true;
            }
        }
        return false;
    }

    public int getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(int n) {
        this.lastModifier = n;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }
}
