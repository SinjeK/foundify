package hu.berlin.dialog;
import de.tuebingen.uni.sfs.germanet.api.GermaNet;
import de.tuebingen.uni.sfs.germanet.relatedness.Relatedness;
import hu.berlin.dialog.languageProcessing.YesNoClassifier;
import hu.berlin.dialog.languageProcessing.YesNoClassifier.YesNoCategory;

/**
 * This state is responsible for finding the needs of the user
 * and choosing the appropriate state.
 */
public class Guide extends DialogState {

    private static String[] responses = {
            "Wobei brauchst du hilfe? Zurzeit kann ich dir nur beim Finden von passenden Förderprogramm helfen."
    };

    public enum State {
        ASSISTANCEPROGRAMS,
        UNDEFINIED,
    }

    private YesNoClassifier yesNoClassifier;
    private State nextState;

    public Guide(DialogStateController controller, String identifier, GermaNet gn, Relatedness relatedness) {
        super(controller, identifier);
        this.yesNoClassifier = new YesNoClassifier(gn, relatedness);
    }

    public State getNextState() {
        return this.nextState;
    }

    @Override
    public void enter() {
        put(createResponse());
    }

    @Override
    public void evaluate(String input) {
        State nextState;
        YesNoCategory category = this.yesNoClassifier.classify(input);

        switch (category) {
            case YES:
                nextState = State.ASSISTANCEPROGRAMS;
                break;
            case NO:
                nextState = State.UNDEFINIED;
                break;
            case UNSPECIFIED:
                nextState = State.UNDEFINIED;
                break;
            default:
                nextState = State.UNDEFINIED;
                assert false : "Unhandled switch statement in Guide class @ evaluate()";
        }

        this.nextState = nextState;
        this.leave();
    }

    private String createResponse() {
        int idx = (int) (Math.random()*responses.length);
        return responses[idx];
    }

}
