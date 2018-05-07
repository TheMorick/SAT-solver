import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //Test with random Hitori board of size n
        //randomSolve(5);

        //Test with typed-in Hitori board.
        //scannerSolve(4);

        int n = 5;
        HitoriToCNF hitori = HitoriToCNF.setUp(n);
        System.out.println(hitori);
        StringBuilder stringBuilder = new StringBuilder();
        int check = 0;
        for (Violation violation : hitori.getViolations()) {
            stringBuilder.append(violation.getClause());
            check++;
            if (check < hitori.getViolations().size()){
                stringBuilder.append("∧");
            }
        }
        SATSolver satSolver = new SATSolver(stringBuilder.toString());
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

    public static void scannerSolve(int n) {
        Scanner scanner = new Scanner(System.in);
        Square[] squares = new Square[n*n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //squares[count++] = new Square(scanner.nextInt(),i,j),;
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
