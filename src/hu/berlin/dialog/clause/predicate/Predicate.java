package hu.berlin.dialog.clause.predicate;

public abstract class Predicate {

    public interface Instance {
        boolean getValueForPredicate(Predicate p);
        void setValueForPredicate(Predicate p, boolean value);
        boolean predicateWasSet(Predicate p);
    }

    private Instance instance;

    // Setter & Getter
    private void setInstance(Instance instance) {
        this.instance = instance;
    }

    protected Instance getInstance() {
        return this.instance;
    }

    // Constructor
    public Predicate(Instance ins) {
        super();
        this.setInstance(ins);
    }

    public void setTrue() {
        this.getInstance().setValueForPredicate(this, true);
    }

    public void setFalse() {
        this.getInstance().setValueForPredicate(this, false);
    }

    public boolean getValue() {
        return this.getInstance().getValueForPredicate(this);
    }

    public boolean wasSet() {
        return this.getInstance().predicateWasSet(this);
    }

    // Methods to be overridden
    public abstract String getIdentifier();

}