import java.util.ArrayList;
import java.util.Random;

public class HitoriToCNF {

    static private Square[][] board;
    static private Square[] variables;
    static private ArrayList<Violation> violations;
    private int n;
    private ArrayList<Chain> chains;
    private ArrayList<Square> violators;

    public static ArrayList<Violation> getViolations() {
        return violations;
    }

    public HitoriToCNF(int n) {
        this.board = new Square[n][n];
        this.n = n;
        this.chains = new ArrayList<>();
    }

    public static HitoriToCNF HitoriRandom(int n) {
        HitoriToCNF hitori = new HitoriToCNF(n);
        Random random = new Random();
        variables = new Square[n*n];
        int nVars = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Square temp = new Square(random.nextInt(n)+1,i,j,board);
                temp.setPlacement(nVars + 1);
                hitori.board[i][j] = temp;
                variables[nVars++] = temp;
            }
        }
        return hitori;
    }

    public static HitoriToCNF setUp(int n) {
        HitoriToCNF hitori = HitoriRandom(n);
        hitori.checkViolations();
        return hitori;
    }

    public ArrayList<Violation> checkViolations() {
        this.violations = new ArrayList<>();
        this.violators = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (this.board[i][j].getNumber() == this.board[i][k].getNumber()) {
                        if (!violators.contains(board[i][j])) {
                            violators.add(board[i][j]);
                        }
                        if (!violators.contains(board[i][k])) {
                            violators.add(board[i][k]);
                        }
                        ArrayList<Square> list = new ArrayList<>();
                        list.add(this.board[i][j]);
                        list.add(this.board[i][k]);
                        violations.add(new Violation((!this.board[i][j].isWhite() || !this.board[i][k].isWhite()),"¬" + this.board[i][j].getPlacement() + "∨¬" + this.board[i][k].getPlacement(),list));
                    }
                    if (this.board[j][i].getNumber() == this.board[k][i].getNumber()) {
                        if (!violators.contains(board[j][i])) {
                            violators.add(board[j][i]);
                        }
                        if (!violators.contains(board[k][i])) {
                            violators.add(board[j][i]);
                        }
                        ArrayList<Square> list = new ArrayList<>();
                        list.add(this.board[j][i]);
                        list.add(this.board[k][i]);
                        violations.add(new Violation((!this.board[j][i].isWhite() || !this.board[k][i].isWhite()),"¬" + this.board[j][i].getPlacement() + "∨¬" + this.board[k][i].getPlacement(),list));
                    }
                }
            }
        }
        for (Square violator : violators) {
            for (Square neighbor : violator.neighbors()) {
                ArrayList<Square> list = new ArrayList<>();
                list.add(violator);
                list.add(neighbor);
                violations.add(new Violation(violator.isWhite() || neighbor.isWhite(),violator.getPlacement() + "∨" + neighbor.getPlacement(),list));
            }
        }
        return violations;
    }

    public void neighborRule() {

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
