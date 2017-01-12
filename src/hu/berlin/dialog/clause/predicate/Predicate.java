package hu.berlin.dialog.clause.predicate;

public interface Predicate {

    interface Instance {

    }

    boolean isTrue(Instance instance);

}
