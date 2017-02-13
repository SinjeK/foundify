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
                    "Wenn du möchtest, können wir gemeinsam nach passenden Finanzierungsmittel für dich suchen :)\n" +
                    "Was hälst du davon?",
    };

    public enum State {
        ASSISTANCEPROGRAMS,
        UNDEFINIED,
    }

    private YesNoClassifier yesNoClassifier;
    private State nextState;
    private int state = 0;

    public Guide(DialogStateController controller, String identifier, UserProfile profile) {
        super(controller, identifier, profile);
        this.yesNoClassifier = new YesNoClassifier();
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
        YesNoCategory category = this.yesNoClassifier.classify(input);

        if (state == 0) {
            switch (category) {
                case YES:
                    this.nextState = State.ASSISTANCEPROGRAMS;
                    this.leave();
                    break;
                case NO:
                    this.put("tut mir leid, dann kann dir leider nicht weiterhelfen");
                    this.put("würdest du vielleicht doch noch etwas über die Finanzierung wissen?");
                    break;
                case UNSPECIFIED:
                    this.put("Uf..");
                    this.put("ähh - tut mir leid ich konnte dich nicht ganz verstehen :/");
                    this.put("könntest du dich wiederholen?");
                    break;
                default:
                    assert false : "Unhandled switch statement in Guide class @ evaluate()";
            }
        } else if (state == 1) {
            switch (category) {
                case YES:
                    this.nextState = State.ASSISTANCEPROGRAMS;
                    this.leave();
                    break;
                case NO:
                    this.put("Schade, tut mir leid, dass ich dir nicht weiterhelfen kann");
                    this.put("Tschüss");
                    System.exit(0);
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

        state++;
    }

    private String createResponse() {
        int idx = (int) (Math.random()*responses.length);
        return responses[idx];
    }

}
