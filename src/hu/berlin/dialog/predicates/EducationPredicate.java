package hu.berlin.dialog.predicates;
import hu.berlin.File.FileLoader;
import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.languageProcessing.EducationClassifier;
import hu.berlin.dialog.languageProcessing.EducationClassifier.EducationCategory;
import json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by Duc on 05.01.17.
 */
public class EducationPredicate extends Predicate {

    private enum QuestionType {
        GENERAL,
    };

    private EducationClassifier classifier;
    private JSONObject rootJSON;

    /**
     * True if predicate is currently evaluating a string.
     * Otherwise false.
     */
    private boolean running;

    public EducationPredicate(DialogStateController controller, String identifier) {
        super(controller, identifier);
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

        this.getDialogController().dialogStateWantsToOutput(this, getWelcomeResponse());
        this.getDialogController().dialogStateWantsToOutput(this, getQuestion(QuestionType.GENERAL));
    }

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
                break;
            case ABITUR:
                break;
            case BACHELOR:
                break;
            case MASTER:
                break;
            case PHD:
                break;
            case STUDIUM:
                break;
            case UNSPECIFIED:
                break;
            default:
                assert false : "Unhandled case in switch! category: " + category.toString();
        }

        this.setRunning(false);
    }

    // Response generation
    private List getAllResponses(QuestionType type) {
        return this.rootJSON.getJSONObject("data").getJSONArray(type.name()).toList();
    }


    private String getWelcomeResponse() {
        return "So dann lass uns nach passenden Förderprogrammen schauen";
    }

    private String getQuestion(QuestionType type) {
        List responses = this.getAllResponses(type);
        String question = (String) responses.get((int)(Math.random() * responses.size()));

        switch (type) {
            case GENERAL:
                break;
            default:
                assert false : "Unhandled case for " + type.toString() + " in EducationPredicate@getQuestion(Questiontype)";
        }

        return question;
    }

}
