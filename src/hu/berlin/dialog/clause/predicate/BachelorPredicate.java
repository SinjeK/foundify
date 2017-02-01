package hu.berlin.dialog.clause.predicate;

/**
 * Created by Duc on 20.01.17.
 */
public class BachelorPredicate extends Predicate {

    public BachelorPredicate(Instance ins) {
        super(ins);
    }

    @Override
    public String getIdentifier() {
        return "predicate.hasBachelor";
    }
}
