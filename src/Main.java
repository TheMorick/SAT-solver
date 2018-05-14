import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int n = 4;

        //Test with random Hitori board of size n
        //randomSolve(5);

        //Test with typed-in Hitori board.
        //scannerSolve(4);

        //Test with typed-in Hitori board.
        sepScannerSolve(n);

        /*
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

        ArrayList<String> strings = satSolver.solve();
        System.out.println();
        if (strings == null) {
            System.out.println("No solution!");
        }
        else {
            for (String string : strings) {
                System.out.println(string);
            }
        }
        */

        /*
        SATVariable first = new SATVariable("1");
        SATVariable second = new SATVariable("2");
        SATVariable third = new SATVariable("3");
        SATVariable fourth = new SATVariable("4");
        SATClause clause1 = new SATClause("¬"+first.getName() + "∨" + second.getName() + "∨" + first.getName());
        SATClause clause2 = new SATClause(third.getName() + "∨" + fourth.getName());
        System.out.println(clause1);
        for (SATVariable satVariable : clause1.getVariables()) {
            System.out.print(satVariable.getName() + " ");
        }
        System.out.println();
        clause1.unitPropagation("1");
        System.out.println(clause1);
        for (SATVariable satVariable : clause1.getVariables()) {
            System.out.print(satVariable.getName() + " ");
        }
        System.out.println();
        */
    }

    public static void sepScannerSolve(int n) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> solution = HitoriToCNF.HitoriToCNFString(n,scanner);
        if (solution != null) {
            for (String s : solution) {
                System.out.println(s);
            }
        }
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
                //squares[count++] = new Square(scanner.nextInt(),i,j);
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
