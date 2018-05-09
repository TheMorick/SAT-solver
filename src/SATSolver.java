import java.util.ArrayList;
import java.util.Scanner;

public class SATSolver {

    private Scanner scanner;
    private ArrayList<SATClause> clauses = new ArrayList<>();
    private ArrayList<SATVariable> variables = new ArrayList<>();
    private ArrayList<ArrayList<SATVariable>> solutions = new ArrayList<>();

    public SATSolver(String CNF) {
        scanner = new Scanner(CNF).useDelimiter("∧");
        while (scanner.hasNext()) {
            clauses.add(new SATClause(scanner.next()));
        }
        for (SATClause clause : clauses) {
            for (SATVariable variable : clause.getVariables()) {
                if (!variables.contains(variable.getName())) {
                    variables.add(variable);
                }
            }
        }
    }

    public ArrayList<String> solve() {
        ArrayList<Integer> changes = new ArrayList<>();
        ArrayList<Boolean> check = new ArrayList<>();
        for (int i = 0; i < variables.size(); i++) {
            changes.add(0);
            check.add(false);
        }
        changes.set(1,1);
        recursiveSatisfy(clauses,variables,changes,check);
        if (solutions != null) {
            return stringer(solutions);
        }
        else return null;
    }

    public ArrayList<String> stringer(ArrayList<ArrayList<SATVariable>> solutions) {
        ArrayList<String> strings = new ArrayList<>();
        for (ArrayList<SATVariable> solution : solutions) {
            StringBuilder stringBuilder = new StringBuilder();
            for (SATVariable satVariable : solution) {
                if (satVariable.getAssignment() == 1) {
                    stringBuilder.append(satVariable.getName() + ": " + true);
                }
                else {
                    stringBuilder.append(satVariable.getName() + ": " + false);
                }
                stringBuilder.append(",\n");
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            strings.add(stringBuilder.toString());
        }
        return strings;
    }

    public void recursiveSatisfy(ArrayList<SATClause> clauses, ArrayList<SATVariable> variables, ArrayList<Integer> changes, ArrayList<Boolean> check) {
        setAssignments(variables,changes);
        ArrayList<SATClause> newClauses = clauses;
        ArrayList<Boolean> newCheck = check;
        ArrayList<Integer> newChanges = changes;
        for (int i = 0; i < changes.size(); i++) {
            if (changes.get(i) != 0) {
                if (check.get(i) == false) {
                    ArrayList<SATClause> newerClauses = new ArrayList<>();
                    for (SATClause clause : newClauses) {
                        SATClause temp = clause.unitPropagation(variables.get(i));
                        if (temp != null) {
                            newerClauses.add(temp);
                        }
                    }
                    newCheck.set(i,true);
                    newClauses = newerClauses;
                }
            }
        }

        Boolean satisfiable = true;
        for (int i = 0; i < newClauses.size(); i++) {
            if (newClauses.get(i).getVariables().size() == 1) {
                for (int j = 0; j < variables.size(); j++) {
                    if (newClauses.get(i).getVariables().get(0) == variables.get(j)) {
                        if (newClauses.get(i).getVariables().get(0).isNegated()) {
                            if (newChanges.get(j) == 1) {
                                satisfiable = false;
                            }
                            else {
                                newChanges.set(j, -1);
                            }
                        }
                        else {
                            if (newChanges.get(j) == -1) {
                                satisfiable = false;
                            }
                            else {
                                newChanges.set(j, 1);
                            }
                        }
                    }
                }
            }
        }

        if (satisfiable) {
            if (!compareChecks(check, newCheck)) {
                recursiveSatisfy(newClauses, variables, newChanges, newCheck);
            }
            int checkeroo = findZero(newChanges);
            if (checkeroo != -1) {
                newChanges.set(checkeroo, 1);
                recursiveSatisfy(newClauses, variables, newChanges, newCheck);
                newChanges.set(checkeroo, -1);
                recursiveSatisfy(newClauses, variables, newChanges, newCheck);
            } else {
                setAssignments(variables, newChanges);
                solutions.add(variables);
            }
        }
    }

    //This method returns true if the two ArrayLists are identical.
    public Boolean compareChecks(ArrayList<Boolean> check, ArrayList<Boolean> newCheck) {
        for (int i = 0; i < check.size(); i++) {
            if (check.get(i) != newCheck.get(i)) {
                return false;
            }
        }
        return true;
    }

    //This method goes through the given ArrayList, looking for a zero.
    //If it finds a zero, it returns the index of that zero.
    //If not, it returns -1.
    public int findZero(ArrayList<Integer> changes) {
        for (int i = 0; i < changes.size(); i++) {
            if (changes.get(i) == 0);
            return i;
        }
        return -1;
    }

    public void setAssignments(ArrayList<SATVariable> variables, ArrayList<Integer> changes) {
        for (int i = 0; i < variables.size(); i++) {
            variables.get(i).setAssignment(changes.get(i));
        }
    }
}
