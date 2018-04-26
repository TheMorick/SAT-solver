public class Violation {
    private Boolean isSatisfied;
    private String clause;

    public Violation(Boolean isSatisfied, String clause) {
        this.isSatisfied = isSatisfied;
        this.clause = clause;
    }

    public Boolean getIsSatisfied() {
        return isSatisfied;
    }

    public String getClause() {
        return clause;
    }
}
