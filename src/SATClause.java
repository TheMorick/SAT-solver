import java.util.ArrayList;
import java.util.Scanner;

public class SATClause {

    private Scanner scanner;
    private ArrayList<SATVariable> variables = new ArrayList<SATVariable>();

    public SATClause(String clause) {
        scanner = new Scanner(clause).useDelimiter("∨");
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (next.startsWith("¬")) {
                next = next.subSequence(1,next.length()).toString();
            }
            variables.add(new SATVariable(next));
        }
    }

    public ArrayList<SATVariable> getVariables() {
        return variables;
    }
}
