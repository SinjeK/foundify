package hu.berlin.dialog;
import hu.berlin.user.Profile;

import java.lang.Math;

/**
 * This state is only responsible for welcoming the user.
 */
public class Welcome extends DialogState {

    private static String[] welcomeResponses = {
            "Hey, ich bin Foundify - dein persönlicher Gründungsbot",
            "Hi mein Name ist Foundify und ich kann dir beim Gründen deines Startups helfen",
    };

    public Welcome (DialogStateController controller, String identifier, Profile profile) {
        super(controller, identifier, profile);
    }

    @Override
    public void evaluate(String input) { /* empty implementation */}

    @Override
    public void enter() {
        this.put("\n\n");
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
