package hu.berlin.dialog;
import hu.berlin.dialog.predicates.EducationPredicate;

/**
 * Dialog state responsible for finding the right assistance programs.
 */
public class AssistancePrograms extends DialogState implements DialogStateController {

    private DialogState currentState;

    public AssistancePrograms(DialogStateController controller, String identifier) {
        super(controller, identifier);
    }

    // Dialogstate
    @Override
    public void evaluate(String input) {
        assert this.currentState != null : "Try to evaluate input but current state is null! " +
                "AssistancePrograms@evaluate(String input)";

        this.currentState.evaluate(input);
    }

    @Override
    public void enter() {
        EducationPredicate education = new EducationPredicate(this, "education");
        this.enterState(education);
    }

    // DialogStateController
    @Override
    public void dialogStateDidLeave(DialogState state) {
        assert this.getDialogController() != null : "Dialog Controller is null in class AssistancePrograms@dialogStateDidLeave";

        if (state.getIdentifier().equals("education")) {
            // go to next state
        } else {
            assert false : "Unhandled state with identifier: " + state.getIdentifier();
        }
    }

    @Override
    public void dialogStateWantsToOutput(DialogState state, String content) {
        assert this.getDialogController() != null : "Dialog Controller is null in class AssistancePrograms@dialogStateWantsToOutput()";

        // forward content output to its dialog controller
        this.getDialogController().dialogStateWantsToOutput(this, content);
    }

    // Helper method
    private void enterState(DialogState state) {
        this.currentState = state;
        state.enter();
    }

}
