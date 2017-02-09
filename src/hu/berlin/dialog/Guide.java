package hu.berlin.dialog;
import de.tuebingen.uni.sfs.germanet.api.GermaNet;
import de.tuebingen.uni.sfs.germanet.relatedness.Relatedness;
import hu.berlin.dialog.languageProcessing.YesNoClassifier;
import hu.berlin.dialog.languageProcessing.YesNoClassifier.YesNoCategory;
import hu.berlin.user.UserProfile;

/**
 * This state is responsible for finding the needs of the user
 * and choosing the appropriate state.
 */
public class Guide extends DialogState {

    private static String[] responses = {
            "Wusstest du, dass 70% aller Startup Gründer nicht genau wussten, wie sie ihr Startup finanzieren solten?\n"+
                    "Wenn du möchtest, können wir gemeinsam nach passenden Finanzierungsmittel für dich suchen :)",
    };

    public enum State {
        ASSISTANCEPROGRAMS,
        UNDEFINIED,
    }

    private YesNoClassifier yesNoClassifier;
    private State nextState;

    public Guide(DialogStateController controller, String identifier, UserProfile profile, GermaNet gn, Relatedness relatedness) {
        super(controller, identifier, profile);
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
                this.nextState = nextState;
                this.leave();
                break;
            case NO:
                this.put("ups dabei kann ich dir zurzeit nicht helfen, da ich ehrlicherweise nicht ganz dafür qualifiziert bin");
                this.put("aber würdest du gerne etwas über die finanzierung von startups wissen?");
                break;
            case UNSPECIFIED:
                this.put("Uf..");
                this.put("ähh - tut mir leid ich konnte dich nicht ganz verstehen :/");
                this.put("könntest du dich wiederholen?");
                break;
            default:
                assert false : "Unhandled switch statement in Guide class @ evaluate()";
        }
    }

    private String createResponse() {
        int idx = (int) (Math.random()*responses.length);
        return responses[idx];
    }

}
