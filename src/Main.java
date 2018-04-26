import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //Test with random Hitori board of size n
        //randomSolve(6);

        //Test with hardcoded Hitori board.
        //manualSolve();

        //Test with typed-in Hitori board.
        scannerSolve(5);
    }

    public static void randomSolve(int n) {
        Hitori hitori = Hitori.HitoriRandom(n);
        System.out.println(hitori);
        System.out.println("");
        doAndMeasure( () -> hitori.solve());
        /*
        for (Violation violation : hitori.getViolations()) {
            System.out.println(violation.getClause());
        }
        */
    }

    public static void manualSolve() {
        Square[] squares = {
                new Square(3,0,0), new Square(2,0,1), new Square(5,0,2), new Square(6,0,3), new Square(5,0,4), new Square(1,0,5),
                new Square(6,1,0), new Square(3,1,1), new Square(1,1,2), new Square(3,1,3), new Square(4,1,4), new Square(3,1,5),
                new Square(4,2,0), new Square(3,2,1), new Square(5,2,2), new Square(4,2,3), new Square(1,2,4), new Square(6,2,5),
                new Square(5,3,0), new Square(3,3,1), new Square(6,3,2), new Square(1,3,3), new Square(6,3,4), new Square(3,3,5),
                new Square(4,4,0), new Square(5,4,1), new Square(4,4,2), new Square(2,4,3), new Square(6,4,4), new Square(2,4,5),
                new Square(1,5,0), new Square(6,5,1), new Square(3,5,2), new Square(5,5,3), new Square(3,5,4), new Square(5,5,5)
        };
        Hitori hitori = new Hitori(6,squares);
        System.out.println(hitori);
        /*for (Violation violation : hitori.checkViolations()) {
            System.out.println(violation.getClause());
        }
        */
        doAndMeasure( () -> hitori.solve());
    }

    public static void scannerSolve(int n) {
        Scanner scanner = new Scanner(System.in);
        Square[] squares = new Square[n*n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                squares[count++] = new Square(scanner.nextInt(),i,j);
            }
        }
        Hitori hitori = new Hitori(n,squares);
        System.out.println(hitori);
        doAndMeasure( () -> hitori.solve());
    }

    public static void doAndMeasure(Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long total = System.currentTimeMillis() - start;
        System.out.println("This task took " + total + " milliseconds, or " + total/1000 + " seconds!");
    }
}
