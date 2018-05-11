import java.util.ArrayList;
import java.util.Scanner;

public class SATClause {

    private Scanner scanner;
    private ArrayList<SATVariable> variables = new ArrayList<>();
    private String clause;

    public SATClause(String clause) {
        this.clause = clause;
        Boolean negated = false;
        scanner = new Scanner(clause).useDelimiter("∨");
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (next.startsWith("¬")) {
                negated = true;
                next = next.subSequence(1,next.length()).toString();
            }
            SATVariable current = new SATVariable(next, !negated);
            variables.add(current);
        }
    }

    public SATClause(ArrayList<SATVariable> variabs) {
        StringBuilder stringBuilder = new StringBuilder();
        for (SATVariable variab : variabs) {
            stringBuilder.append(variab.getName());
            stringBuilder.append("∨");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
    }

    public ArrayList<SATVariable> getVariables() {
        return variables;
    }

    public Boolean clauseSatisfied() {
        for (SATVariable variable : variables) {
            if (variable.variableSatisfied()) {
                return true;
            }
        }
        return false;
    }

    //This method returns the resulting clause object, after the given
    // variable has been processed.
    //If the given variable is in this clause, some checks for negation
    // ensure that the correct course of action is taken.
    public SATClause unitPropagation(SATVariable variable) {
        if (variables.size() > 1) {
            for (SATVariable satVariable : variables) {
                if (satVariable.getName().equals(variable.getName())) {
                    if (!variable.isNegated()) {
                        if (satVariable.isNegated()) {
                            variables.remove(satVariable);
                            return new SATClause(variables);
                        }
                        else {
                            return null;
                        }
                    }
                    else {
                        if (!satVariable.isNegated()) {
                            variables.remove(satVariable);
                            return new SATClause(variables);
                        }
                        else {
                            return null;
                        }
                    }
                }
            }
        }
        return this;
    }

    //Is now no longer required.
    /*
    public void removeVariable(String variable) {
        map.remove(variable);
        //This part doesn't work. It's the part from here --
        String[] array = new String[variables.size()];
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).getName().equals(variable)) {
                array[i] = variables.get(i).getName();
            }
        }
        for (String s : array) {
            variables.remove(s);
        }
        // -- to here.
        StringBuilder stringBuilder = new StringBuilder();
        scanner = new Scanner(clause).useDelimiter("∨");
        while (scanner.hasNext()) {
            String next = scanner.next();
            String withoutNegation = next;
            if (next.startsWith("¬")) {
                withoutNegation = next.subSequence(1,next.length()).toString();
            }
            if (!withoutNegation.equals(variable)) {
                stringBuilder.append(next);
                stringBuilder.append("∨");
            }
        }
        if ( "∨".toCharArray()[0] == (stringBuilder.charAt(stringBuilder.length()-1)) ) {
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        this.clause = stringBuilder.toString();
    }
    */

    @Override
    public String toString() {
        return clause;
    }
}
