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

    public Square(int number, int i, int j) {
        this.number = number;
        this.isWhite = true;
        this.iCoord = i;
        this.jCoord = j;
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

    @Override
    public String toString() {
        return Integer.toString(getNumber());
    }
}
