package hu.berlin.dialog.predicates;
import hu.berlin.dialog.DialogState;
import hu.berlin.dialog.DialogStateController;

public abstract class Predicate extends DialogState {

    public Predicate(DialogStateController controller, String identifier) {
        super(controller, identifier);
    }

    @Override
    public void evaluate(String input) {

    }

    @Override
    public void enter() {

    }

}
