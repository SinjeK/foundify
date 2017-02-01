package hu.berlin.dialog.clause.predicate;

/**
 * Created by Duc on 20.01.17.
 */
public class QualificationPredicate extends Predicate {

    public QualificationPredicate(Instance ins) {
        super(ins);
    }

    @Override
    public String getIdentifier() {
        return "predicate.hasQualification";
    }
}
