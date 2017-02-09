package hu.berlin.dialog;
import hu.berlin.dialog.clause.EducationClause;
import hu.berlin.dialog.clause.EmploymentClause;
import hu.berlin.dialog.clause.IdeaClause;
import hu.berlin.dialog.evaluation.AssistanceProgramsEvaluator;
import hu.berlin.dialog.evaluation.AssistanceProgramsEvaluator.AssistancePrograms;
import hu.berlin.user.UserProfile;
import java.util.List;

/**
 * Dialog state responsible for finding the right assistance programs.
 */
public class AssistanceProgramsState extends DialogState implements DialogStateController {

    private DialogState currentState;

    public AssistanceProgramsState(DialogStateController controller, String identifier, UserProfile profile) {
        super(controller, identifier, profile);
    }

    // Dialogstate
    @Override
    public void evaluate(String input) {
        assert this.currentState != null : "Try to evaluate input but current state is null! " +
                "AssistanceProgramsState@evaluate(String input)";

        this.currentState.evaluate(input);
    }

    @Override
    public void enter() {
        EducationClause education = new EducationClause(this, "education", this.getProfile());
        this.enterState(education);
    }

    // DialogStateController
    @Override
    public void dialogStateDidLeave(DialogState state) {
        assert this.getDialogController() != null : "Dialog Controller is null in class AssistanceProgramsState@dialogStateDidLeave";

        if (state.getIdentifier().equals("education")) {
            // go to next state
            EmploymentClause employment = new EmploymentClause(this, "employment", this.getProfile());
            this.enterState(employment);
        } else if (state.getIdentifier().equals("employment")) {
            IdeaClause idea = new IdeaClause(this, "idea", this.getProfile());
            this.enterState(idea);
        } else if (state.getIdentifier().equals("idea")) {
            // implement end

            List<AssistancePrograms> suitablePrograms = AssistanceProgramsEvaluator.findSuitableAssistancePrograms(this.getProfile());

            this.put("Ich konnte folgende für dich passende Förderprogramme finden:");
            for (AssistancePrograms p : suitablePrograms) {
                this.put(p.toString());
            }

            this.put("Hier noch Informationen zu den Förderprogrammen");
            for (AssistancePrograms p : suitablePrograms) {
                this.put(p.description());
                this.put(p.url());
            }

            this.put("Ich hoffe, ich konnte dir bei der Suche nach passenden Förderprogrammen helfen.");
            this.leave();

        } else {
            assert false : "Unhandled state with identifier: " + state.getIdentifier();
        }

        state = null;
    }

    @Override
    public void dialogStateWantsToOutput(DialogState state, String content) {
        assert this.getDialogController() != null : "Dialog Controller is null in class AssistanceProgramsState@dialogStateWantsToOutput()";

        // forward content output to its dialog controller
        this.getDialogController().dialogStateWantsToOutput(this, content);
    }

    // Helper method
    private void enterState(DialogState state) {
        this.currentState = state;
        state.enter();
    }

}