package hu.berlin.dialog.predicates;
import hu.berlin.dialog.DialogState;
import hu.berlin.dialog.DialogStateController;
import hu.berlin.user.Profile;

/**
 * A predicate represents a logical formula. As a dialog state
 * it is responsible for assigning values provided by the user
 * to the variables occurring in the logical formula.
 **/
public abstract class Clause extends DialogState {

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
