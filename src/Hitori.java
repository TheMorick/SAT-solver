import java.util.ArrayList;
import java.util.Random;

public class Hitori {

    static private Square[][] board;
    static private Square[] variables;
    static private ArrayList<Violation> violations;
    static private int n;
    static private ArrayList<Chain> chains;

    public static Square[][] getBoard() {
        return board;
    }

    public static int getN() {
        return n;
    }

    public ArrayList<Violation> getViolations() {
        return violations;
    }

    public static Square[] getVariables() {
        return variables;
    }

    public static ArrayList<Chain> getChains() {
        return chains;
    }

    public Hitori(int n) {
        this.board = new Square[n][n];
        this.n = n;
        this.chains = new ArrayList<>();
    }

    public Hitori(int n, Square[] variables) {
        this.board = new Square[n][n];
        this.n = n;
        this.variables = variables;
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Square temp = variables[count++];
                temp.setPlacement(count);
                board[i][j] = temp;
            }
        }
    }

    public static Hitori HitoriRandom(int n) {
        Hitori hitori = new Hitori(n);
        Random random = new Random();
        variables = new Square[n*n];
        int nVars = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Square temp = new Square(random.nextInt(n)+1,i,j);
                temp.setPlacement(nVars + 1);
                hitori.board[i][j] = temp;
                variables[nVars++] = temp;
            }
        }
        return hitori;
    }

    public ArrayList<Violation> checkViolations() {
        violations = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (board[i][j].getNumber() == board[i][k].getNumber()) {
                        violations.add(new Violation((!getBoard()[i][j].isWhite() || !getBoard()[i][k].isWhite()),"Not " + getBoard()[i][j].getPlacement() + " or not " + getBoard()[i][k].getPlacement()));
                    }
                    if (board[j][i].getNumber() == board[k][i].getNumber()) {
                        violations.add(new Violation((!getBoard()[j][i].isWhite() || !getBoard()[k][i].isWhite()),"Not " + getBoard()[j][i].getPlacement() + " or not " + getBoard()[k][i].getPlacement()));
                    }
                }
            }
        }
        return violations;
    }

    public static int reassign(Square[] vars, int n) {
        if (vars[n].isWhite()) {
            vars[n].setFalse();
            return 1; //returning 1 means that a change was made, and a recheck can occur.
        }
        else {
            if (n != 0) {
                vars[n].setTrue();
                return reassign(vars, n-1);
            }
            else {
                return 0; //returning 0 means that no changes can be made, and we must presume this expression unsatisfiable.
            }
        }
    }

    public Boolean solve() {
        int n = 1;
        while (!solved()) {
            //System.out.println("Try number " + n++);
            if (reassign(getVariables(),getVariables().length-1) == 0) {
                System.out.println("No solution here!");
                return false;
            }
        }
        System.out.println("This puzzle has a solution!");
        for (Square square : getVariables()) {
            System.out.println("Square number " + square.getPlacement() + " has to be " + square.isWhite());
        }

        return true;
    }

    public Boolean solved() {
        checkViolations();
        //Check that no row or column contains more than one of each number from 1 to n.
        for (Violation violation : getViolations()) {
            if (!violation.getIsSatisfied()) {
                return false;
            }
        }
        //Check that no black square is adjacent to another black square.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j > 0) {
                    if (!getBoard()[i][j].isWhite() && !getBoard()[i][j-1].isWhite()) {
                        return false;
                    }
                }
                if (i > 0) {
                    if (!getBoard()[i][j].isWhite() && !getBoard()[i-1][j].isWhite()) {
                        return false;
                    }
                }
                if (i < n-1) {
                    if (!getBoard()[i][j].isWhite() && !getBoard()[i+1][j].isWhite()) {
                        return false;
                    }
                }
                if (j < n-1) {
                    if (!getBoard()[i][j].isWhite() && !getBoard()[i][j+1].isWhite()) {
                        return false;
                    }
                }
            }
        }
        //Check that all white spaces are connected via white spaces.
        //checkChains();
        return chainLength();
    }

    public ArrayList<Chain> checkChains() {
        chains = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j].setChainNull();
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j].isWhite()) {
                    if (j > 0) {
                        if (board[i][j - 1].isWhite() && board[i][j - 1].isInChain()) {
                            board[i][j - 1].getChain().addSquare(board[i][j]);
                        }
                    } else if (i > 0) {
                        if (board[i - 1][j].isWhite() && board[i - 1][j].isInChain()) {
                            board[i - 1][j].getChain().addSquare(board[i][j]);
                        }
                    } else if (i < n - 1) {
                        if (board[i + 1][j].isWhite() && board[i + 1][j].isInChain()) {
                            board[i + 1][j].getChain().addSquare(board[i][j]);
                        }
                    } else if (j < n - 1) {
                        if (board[i][j + 1].isWhite() && board[i][j + 1].isInChain()) {
                            board[i][j + 1].getChain().addSquare(board[i][j]);
                        }
                    } else {
                        Chain newChain = new Chain();
                        newChain.addSquare(board[i][j]);
                        chains.add(newChain);
                    }
                }
            }
        }
        System.out.println(chains.size());
        return chains;
    }

    public Boolean chainLength() {
        Chain chain = new Chain();
        if (board[0][0].isWhite()) {
            chain.addSquare(board[0][0]);
            chainRecursive(board[0][0]);
        }
        else {
            //Since this can't also be black.
            chain.addSquare(board[1][0]);
            chainRecursive(board[1][0]);
        }
        return chain.tape() == n*n;
    }

    public void chainRecursive(Square square) {
        int i = square.getiCoord();
        int j = square.getjCoord();
        Square neighbor;
        if (square.getjCoord() > 0) {
            neighbor = board[i][j-1];
            if (square.getChain() != neighbor.getChain() || neighbor.getChain() == null) {
                square.getChain().addSquare(neighbor);
                if (neighbor.isWhite()) {
                    chainRecursive(neighbor);
                }
            }
        }
        if (square.getiCoord() > 0) {
            neighbor = board[i-1][j];
            if (square.getChain() != neighbor.getChain() || neighbor.getChain() == null) {
                square.getChain().addSquare(neighbor);
                if (neighbor.isWhite()) {
                    chainRecursive(neighbor);
                }
            }
        }
        if (square.getiCoord() < n-1) {
            neighbor = board[i+1][j];
            if (square.getChain() != neighbor.getChain() || neighbor.getChain() == null) {
                square.getChain().addSquare(neighbor);
                if (neighbor.isWhite()) {
                    chainRecursive(neighbor);
                }
            }
        }
        if (square.getjCoord() < n-1) {
            neighbor = board[i][j+1];
            if (square.getChain() != neighbor.getChain() || neighbor.getChain() == null) {
                square.getChain().addSquare(neighbor);
                if (neighbor.isWhite()) {
                    chainRecursive(neighbor);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int n = this.n;
        stringBuilder.append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                stringBuilder.append(" " + board[i][j]);
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
