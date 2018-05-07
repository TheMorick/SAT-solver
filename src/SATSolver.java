import java.util.ArrayList;
import java.util.Scanner;

public class SATSolver {

    private Scanner scanner;
    private ArrayList<SATClause> clauses = new ArrayList<>();
    private ArrayList<String> variables = new ArrayList<>();

    public SATSolver(String CNF) {
        scanner = new Scanner(CNF).useDelimiter("∧");
        while (scanner.hasNext()) {
            clauses.add(new SATClause(scanner.next()));
        }
        for (SATClause clause : clauses) {
            for (SATVariable variable : clause.getVariables()) {
                if (!variables.contains(variable.getName())) {
                    variables.add(variable.getName());
                }
            }
        }
        for (String variable : variables) {
            System.out.println(variable);
        }
    }
}
