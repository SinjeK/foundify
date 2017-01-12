package hu.berlin.dialog.clause;
import hu.berlin.dialog.DialogState;
import hu.berlin.dialog.DialogStateController;
import hu.berlin.user.Profile;

/**
 * A predicate represents a logical formula. As a dialog state
 * it is responsible for assigning values provided by the user
 * to the variables occuring in the logical formula.dnjdkcnjdndjncdjcnjdkcnd
 **/
public abstract class Clause extends DialogState {

    interface Predicate {
        boolean isTrue(Profile profile, String identifier);
    }

    public Clause(DialogStateController controller, String identifier, Profile profile) {
        super(controller, identifier, profile);
    }

    @Override
    public void evaluate(String input) {

    }

    @Override
    public void enter() {

    }

}
