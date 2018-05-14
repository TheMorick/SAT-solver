import java.util.ArrayList;
import java.util.Scanner;

public class SATSolver {

    private Scanner scanner;
    private ArrayList<SATClause> clauses = new ArrayList<>();
    private ArrayList<SATVariable> variables = new ArrayList<>();
    private ArrayList<ArrayList<String>> solutions = new ArrayList<>();

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
        //Could add solve() here, to make the whole thing automatic, maybe.
    }

    public ArrayList<ArrayList<String>> solve() {
        ArrayList<Integer> assignments = new ArrayList<>();
        ArrayList<Boolean> changes = new ArrayList<>();
        for (int i = 0; i < variables.size(); i++) {
            assignments.add(0);
            changes.add(false);
        }
        assignments.set(1,1);
        recursiveSatisfy(clauses,variables,assignments,changes);
        if (solutions.size() != 0) {
            return solutions;
        }
        else return null;
    }

    //Now unnecessary.
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

    public void recursiveSatisfy(ArrayList<SATClause> clauses, ArrayList<SATVariable> variables, ArrayList<Integer> assignments, ArrayList<Boolean> changes) {
        setAssignments(variables,assignments);
        ArrayList<SATClause> newClauses = clauses;
        ArrayList<SATClause> newerClauses = new ArrayList<>();
        ArrayList<Integer> newAssignments = assignments;
        ArrayList<Boolean> newChanges = changes;
        System.out.println(variables.size() + " variables at the start!");
        System.out.println(newClauses.size() + " clauses at the start!");
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i) != 0) {
                if (!changes.get(i)) {
                    for (SATClause clause : newClauses) {
                        SATClause temp = clause.unitPropagation(variables.get(i));
                        if (temp != null) {
                            newerClauses.add(temp);
                        }
                    }
                    newChanges.set(i,true);
                    newClauses = newerClauses;
                }
            }
        }
        //Update the list of variables to reflect the new clauses.
        //variables = findNewVariables(newClauses);
        System.out.println(variables.size() + " variables at the end!");
        System.out.println(newClauses.size() + " clauses at the end!");

        Boolean isSolved = true;
        for (int i = 0; i < newClauses.size(); i++) {
            if (newClauses.get(i).getVariables().size() == 1) {
                for (int j = 0; j < variables.size(); j++) {
                    if (newClauses.get(i).getVariables().get(0).getName() == variables.get(j).getName()) {
                        if (newClauses.get(i).getVariables().get(0).isNegated()) {
                            if (newAssignments.get(j) == 1) {
                                isSolved = false;
                            }
                            else {
                                newAssignments.set(j, -1);
                            }
                        }
                        else {
                            if (newAssignments.get(j) == -1) {
                                isSolved = false;
                            }
                            else {
                                newAssignments.set(j, 1);
                            }
                        }
                    }
                }
            }
        }

        //An issue might arise from the list of variables being empty.
        //This keeps an eye out for that.
        if (variables.isEmpty()) {
            try {
                throw new Exception("Out of variables!");
            } catch (Exception e) {
                System.out.println("Exception-exception!");
            }
        }
        System.out.println(howManyZeroes() + " variables are unassigned!");
        if (isSolved) {
            if (!compareChecks(changes, newChanges)) {
                recursiveSatisfy(newClauses, variables, newAssignments, newChanges);
            }
            int checkeroo = findZero(newAssignments);
            System.out.println("Found a zero at spot " + checkeroo);
            System.out.println();
            if (checkeroo != -1) {
                newAssignments.set(checkeroo, 1);
                recursiveSatisfy(newClauses, variables, newAssignments, newChanges);
                newAssignments.set(checkeroo, -1);
                recursiveSatisfy(newClauses, variables, newAssignments, newChanges);
            } else {
                setAssignments(variables, newAssignments);
                //If we add isSatisfied(), it says there is no solution.
                //If we leave it out, it comes up with a wrong solution.
                if (isSatisfied()) {
                    //solutions.add(variables);
                    solutions.add(getAssignments(variables, newAssignments));
                }
            }
        }
        System.out.println();
    }

    //This method is for testing only.
    public int howManyZeroes() {
        int number = 0;
        for (SATVariable variable : variables) {
            if (variable.getAssignment() == 0) {
                number++;
            }
        }
        return number;
    }

    public ArrayList<SATVariable> findNewVariables(ArrayList<SATClause> newClauses) {
        ArrayList<SATVariable> variables = new ArrayList<>();
        System.out.println("We have " + newClauses.size() + " clauses to work with!");
        variables.add(newClauses.get(0).getVariables().get(0));
        for (SATClause newClause : newClauses) {
            for (SATVariable satVariable : newClause.getVariables()) {
                Boolean varContains = false;
                for (SATVariable variable : variables) {
                    if (satVariable == variable) {
                        varContains = true;
                    }
                }
                if (!varContains) {
                    variables.add(satVariable);
                }
            }
        }
        return variables;
    }

    public Boolean isSatisfied() {
        for (SATClause clause : clauses) {
            if (!clause.clauseSatisfied()) {
                return false;
            }
        }
        return true;
    }

    //This method returns true if the two ArrayLists are identical.
    public Boolean compareChecks(ArrayList<Boolean> changes, ArrayList<Boolean> newChanges) {
        for (int i = 0; i < changes.size(); i++) {
            if (changes.get(i) != newChanges.get(i)) {
                return false;
            }
        }
        return true;
    }

    //This method goes through the given ArrayList, looking for a zero.
    //If it finds a zero, it returns the index of that zero.
    //If not, it returns -1.
    public int findZero(ArrayList<Integer> assignments) {
        for (int i = 0; i < assignments.size(); i++) {
            //embarrassing...
            if (assignments.get(i) == 0) {
                return i;
            }
        }
        return -1;
    }

    public void setAssignments(ArrayList<SATVariable> variables, ArrayList<Integer> assignments) {
        for (int i = 0; i < variables.size(); i++) {
            variables.get(i).setAssignment(assignments.get(i));
        }
    }

    public ArrayList<String> getAssignments(ArrayList<SATVariable> variables, ArrayList<Integer> assignments) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < variables.size(); i++) {
            if (assignments.get(i) == 1) {
                strings.add(variables.get(i).getName() + ": true");
            }
            else {
                strings.add(variables.get(i).getName() + ": false");
            }
        }
        return strings;
    }
}
