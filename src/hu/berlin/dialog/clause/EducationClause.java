package hu.berlin.dialog.clause;
import hu.berlin.file.FileLoader;
import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.languageProcessing.EducationClassifier;
import hu.berlin.dialog.languageProcessing.EducationClassifier.EducationCategory;
import hu.berlin.user.Profile;
import java.io.IOException;
import java.util.List;
import json.JSONObject;

/**
 * Created by Duc on 05.01.17.
 */
public class EducationClause extends Clause {

    private enum ResponseType {
        GENERAL,
        STUDIUM,
        UNSPECIFIED,
        PHD,
        BACHELOR,
        MASTER,
        ABITUR,
        QUALIFICATION
    };

    private EducationClassifier classifier;
    private JSONObject rootJSON;

    /**
     * True if predicate is currently evaluating a string.
     * Otherwise false.
     */
    private boolean running;

    public EducationClause(DialogStateController controller, String identifier, Profile profile) {
        super(controller, identifier, profile);
        this.classifier = new EducationClassifier();

        try {
            String JSONContent = FileLoader.loadContentOfFile("hu/berlin/dialog/responses/education.json");
            this.rootJSON = new JSONObject(JSONContent);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // Setter & Getter
    private void setRunning(boolean r) {
        this.running = r;
    }

    /**
     * @return true if predicate is currently evaluating, otherwise false
     */
    public boolean isRunning() {
        return this.running;
    }

    // Dialog State
    @Override
    public void enter() {
        super.enter();

        put(getWelcomeResponse());
        put(getResponse(ResponseType.GENERAL));
    }

    /**
     * Evaluate the input of user. Tries to recognize user' graduation.
     * It also manages the transitions.
     *
     * @param input Input provided by user
     */
    @Override
    public void evaluate(String input) {
        super.evaluate(input);

        if (this.isRunning()) {
            this.put("Warte ich schaue gerade nach passenden Förderprogrammen");
            return;
        }

        this.setRunning(true);
        EducationCategory category = this.classifier.classify(input);

        switch (category) {
            case QUALIFICATION:
                put(getResponse(ResponseType.QUALIFICATION));
                break;
            case ABITUR:
                put(getResponse(ResponseType.ABITUR));
                break;
            case BACHELOR:
                put(getResponse(ResponseType.BACHELOR));
                break;
            case MASTER:
                put(getResponse(ResponseType.MASTER));
                break;
            case PHD:
                put(getResponse(ResponseType.PHD));
                break;
            case STUDIUM:
                put(getResponse(ResponseType.STUDIUM));
                break;
            case UNSPECIFIED:
                put(getResponse(ResponseType.UNSPECIFIED));
                break;
            default:
                assert false : "Unhandled case in switch! category: " + category.toString();
        }

        this.setRunning(false);
    }

    // Response generation
    private List getAllResponses(ResponseType type) {
        return this.rootJSON.getJSONObject("data").getJSONArray(type.name()).toList();
    }

    private String getWelcomeResponse() {
        return "So dann lass uns nach passenden Förderprogrammen schauen";
    }

    private String getResponse(ResponseType type) {
        List responses = this.getAllResponses(type);
        String question = (String) responses.get((int)(Math.random() * responses.size()));

        switch (type) {
            case GENERAL:
                break;
            case STUDIUM:
                break;
            case UNSPECIFIED:
                break;
            case QUALIFICATION:
            case PHD:
            case ABITUR:
            case MASTER:
            case BACHELOR:
                break;
            default:
                assert false : "Unhandled case for " + type.toString() + " in EducationClause@getResponse(Questiontype)";
        }

        return question;
    }


}
