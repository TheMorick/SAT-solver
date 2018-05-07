import java.util.ArrayList;

public class Square {
    private int number;
    /*Here, the square's color is represented with a boolean object.
    In this implementation, the value true means the square is white,
    while the value false means that the square is black.
    */
    private boolean isWhite;
    /*
    This number represents a square's placement on the board, when see
    as a single-dimensional array. Number 1 is the first square, 2 is
    the second, and so on.
     */
    private int placement;
    private Chain chain = null;
    private int iCoord;
    private int jCoord;
    //The below is a way of determining a given square's assignment.
    //1 means the assignment of True, meaning that the square is white.
    //-1 mean the assignment of False, meaning that the square is black.
    //0 means the assignment of null, meaning that the square has not received an assignment yet.
    private Byte satisfied;
    private int lastModifier;
    private Square[][] board;

    public Square(int number, int i, int j, Square[][] board) {
        this.number = number;
        this.isWhite = true;
        this.iCoord = i;
        this.jCoord = j;
        this.lastModifier = Integer.MAX_VALUE;
        this.board = board;
    }

    public int getNumber() {
        return number;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setTrue() {
        this.isWhite = true;
    }

    public void setFalse() {
        this.isWhite = false;
    }

    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int n) {
        this.placement = n;
    }

    public Chain getChain() {
        return this.chain;
    }

    public void setChain(Chain chain) {
        this.chain = chain;
    }

    public void setChainNull() {
        this.chain = null;
    }

    public Boolean isInChain() {
        return (chain != null);
    }

    public int getiCoord() {
        return iCoord;
    }

    public int getjCoord() {
        return jCoord;
    }

    public void setSatisfied(Byte satisfied) {
        this.satisfied = satisfied;
    }

    public Byte satisfied() {
        return satisfied;
    }

    public void modify(Byte assignment, int layer) {
        this.satisfied = assignment;
        this.lastModifier = layer;
    }

    public int getLastModifier() {
        return lastModifier;
    }

    public ArrayList<Square> neighbors() {
        ArrayList<Square> neighbors = new ArrayList<>();
        if (this.getjCoord() > 0) {
            neighbors.add(board[getiCoord()][getjCoord()-1]);
        }
        if (this.getiCoord() > 0) {
            neighbors.add(board[getiCoord()-1][getjCoord()]);
        }
        if (this.getiCoord() < board.length - 1) {
            neighbors.add(board[getiCoord()+1][getjCoord()]);
        }
        if (this.getjCoord() < board.length - 1) {
            neighbors.add(board[getiCoord()][getjCoord()+1]);
        }
        return neighbors;
    }

    @Override
    public String toString() {
        return Integer.toString(getNumber());
    }
}
