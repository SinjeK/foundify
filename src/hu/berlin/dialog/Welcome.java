package hu.berlin.dialog;
import hu.berlin.user.UserProfile;

import java.lang.Math;

/**
 * This state is only responsible for welcoming the user.
 */
public class Welcome extends DialogState {

    private static String[] welcomeResponses = {
            "Hey, ich bin Foundify - dein persönlicher Assistent und Berater beim Gründen von Startups",
    };

    public Welcome (DialogStateController controller, String identifier, UserProfile profile) {
        super(controller, identifier, profile);
    }

    @Override
    public void evaluate(String input) { /* empty implementation */}

    @Override
    public void enter() {
        for (int i=0; i<4; i++) {
            this.put("");
        }

        this.put(this.createResponse());
        this.leave();
    }

    /**
     * Generate and return a random response from a database (in this
     * case from welcomeResponses)
     *
     * @return A random string from welcomeResponses
     */
    private String createResponse() {
        int idx = (int) (Math.random() * welcomeResponses.length);
        return welcomeResponses[idx];
    }

}
