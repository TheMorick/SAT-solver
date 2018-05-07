public class SATVariable {

    String name;
    Boolean assignment = null;

    public SATVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
