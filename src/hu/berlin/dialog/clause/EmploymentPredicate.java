package hu.berlin.dialog.clause;

import java.io.IOException;
import java.util.List;

import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.clause.EmploymentPredicate.ResponseType;
import hu.berlin.dialog.languageProcessing.EmploymentClassifier;
import hu.berlin.dialog.languageProcessing.EmploymentClassifier.EmploymentCategory;
//import hu.berlin.dialog.clause;
import hu.berlin.file.FileLoader;
import hu.berlin.user.Profile;
import json.JSONObject;

public class EmploymentPredicate extends Clause {

	public enum ResponseType {
			GENERAL,
			UNEMPLOYED,
			STUDENT,
			SCIENTIST,
			OTHER_EMPLOYMENT,
			UNSPECIFIED
	}
	
	private EmploymentClassifier classifier;
    private JSONObject rootJSON;
    

    /**
     * True if predicate is currently evaluating a string.
     * Otherwise false.
     */
    private boolean running;
	
	public EmploymentPredicate(DialogStateController controller, String identifier, Profile profile) {
		super(controller, identifier, profile);
		this.classifier = new EmploymentClassifier();
		
		try {
            String JSONContent = FileLoader.loadContentOfFile("hu/berlin/dialog/responses/emplyoment.json");
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
            this.put("Einen Moment bitte! Ich schaue noch nach geeigneten Programmen.");
            return;
        }

        this.setRunning(true);
        EmploymentCategory category = this.classifier.classify(input);

        switch (category) {
            case STUDENT:
                put(getResponse(ResponseType.STUDENT));
                break;
            case SCIENTIST:
                put(getResponse(ResponseType.SCIENTIST));
                break;
            case UNEMPLOYED:
                put(getResponse(ResponseType.UNEMPLOYED));
                break;
            case OTHER_EMPLOYMENT:
                put(getResponse(ResponseType.OTHER_EMPLOYMENT));
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
        return "Super, vielen Dank! Jetzt müsste ich erstmal noch etwas über deine Arbeitserfahrung wissen.";
    }

    private String getResponse(ResponseType type) {
        List responses = this.getAllResponses(type);
        String question = (String) responses.get((int)(Math.random() * responses.size()));

        switch (type) {
            case GENERAL:
                break;
            case STUDENT:
                break;
            case UNSPECIFIED:
                break;
            case SCIENTIST:
            	break;
            case OTHER_EMPLOYMENT:
            	break;
            case UNEMPLOYED:
                break;
            default:
                assert false : "Unhandled case for " + type.toString() + " in EmploymentPredicate@getResponse(Questiontype)";
        }

        return question;
    }

}
