public class SATVariable {

    private String name;

    //The assignment of the variable is determined with this byte.
    // If the byte is 0, the variable is unassigned.
    // If the byte is 1, the variable is true.
    // If the byte is -1, the variable is false.
    private int assignment = 0;

    //This Boolean determines whether or not a variable is negated
    // in the clause. If the Boolean is true, the variable is not
    // negated. If the Boolean is false, it is.
    private Boolean trueness;

    public SATVariable(String name, Boolean trueness) {
        this.name = name;
        this.trueness = trueness;
    }

    public String getName() {
        return name;
    }

    public int getAssignment() {
        return assignment;
    }

    public void setAssignment(int assignment) {
        this.assignment = assignment;
    }

    public Boolean isNegated() {
        return !trueness;
    }

    public Boolean variableSatisfied() {
        if (trueness) {
            return assignment == 1;
        }
        else {
            return assignment == -1;
        }
    }
}
