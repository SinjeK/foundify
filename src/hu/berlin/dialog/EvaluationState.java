package hu.berlin.dialog;

import hu.berlin.user.UserProfile;

/**
 * Created by Duc on 10.02.17.
 */
public class EvaluationState extends DialogState {

    private int state;

    public EvaluationState(DialogStateController controller, String identifier, UserProfile profile) {
        super(controller, identifier, profile);
    }

    @Override
    public void enter() {
        state = 0;
        this.put("test");
    }

    @Override
    public void evaluate(String input) {

    }

}
