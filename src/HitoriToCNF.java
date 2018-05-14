import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class HitoriToCNF {

    static private Square[][] board;
    static private Square[] variables;
    static private ArrayList<Violation> violations;
    static private int n;
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

    public static ArrayList<String> HitoriToCNFString(int n, Scanner scanner) {
        HitoriToCNF hitori = new HitoriToCNF(n);
        variables = new Square[n*n];
        int nVars = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.println("Square at position " + (i+1) + "," + (j+1));
                Square temp = new Square(scanner.nextInt(),i,j,hitori.board);
                temp.setPlacement(nVars + 1);
                hitori.board[i][j] = temp;
                variables[nVars++] = temp;
            }
        }
        System.out.println(hitori);
        hitori.checkViolations();
        StringBuilder stringBuilder = new StringBuilder();
        int check = 0;
        for (Violation violation : getViolations()) {
            stringBuilder.append(violation.getClause());
            check++;
            if (check < getViolations().size()) {
                stringBuilder.append("∧");
            }
        }
        System.out.println(stringBuilder.toString());
        SATSolver solver = new SATSolver(stringBuilder.toString());
        ArrayList<ArrayList<String>> stringLists = solver.solve();
        System.out.println();
        if (stringLists == null) {
            System.out.println("No solution!");
        } else {
            for (ArrayList<String> stringList : stringLists) {
                assign(stringList);
                if (chainLength()) {
                    //One of possibly many solutions.
                    return stringList;
                }
            }
        }
        //Should be null-check at the other end.
        return null;
    }

    public static void assign(ArrayList<String> stringList) {
        varLoop: for (Square variable : variables) {
            for (String string : stringList) {
                if (string.startsWith(Integer.toString(variable.getPlacement()))) {
                    if (string.endsWith("true")) {
                        variable.setTrue();
                    } else {
                        variable.setFalse();
                    }
                    continue varLoop;
                }
            }
        }
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

    //No longer used (?)
    /*
    public static void solve(HitoriToCNF hitori) {
        hitori.checkViolations();
        if (hitori.getViolations().size() == 0) {
            System.out.println("Easy solution!");
        }
        else {
            StringBuilder stringBuilder = new StringBuilder();
            int check = 0;
            for (Violation violation : hitori.getViolations()) {
                stringBuilder.append(violation.getClause());
                check++;
                if (check < hitori.getViolations().size()) {
                    stringBuilder.append(" ∧ ");
                }
            }
            System.out.println(stringBuilder.toString());
            SATSolver solver = new SATSolver(stringBuilder.toString());
            ArrayList<String> strings = solver.solve();
            if (strings == null) {
                System.out.println("No solution here!");
            } else {
                for (String string : strings) {
                    System.out.println(string);
                }
            }
        }
    }
    */

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

    public static Boolean chainLength() {
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

    public static void chainRecursive(Square square) {
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
