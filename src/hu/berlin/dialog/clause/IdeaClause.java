package hu.berlin.dialog.clause;

import java.io.IOException;
import java.util.List;

import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.languageProcessing.EmploymentClassifier;
import hu.berlin.dialog.languageProcessing.EmploymentClassifier.EmploymentCategory;
import hu.berlin.dialog.languageProcessing.IdeaClassifier;
import hu.berlin.dialog.languageProcessing.IdeaClassifier.InnoCategory;
//import hu.berlin.dialog.predicates.Clause;
import hu.berlin.file.FileLoader;
import hu.berlin.user.Profile;
import json.JSONObject;

public class IdeaClause extends Clause {  //extends Predicate
	
	public enum ResponseType {
		GENERAL,     //first question
		INNOVATIVE,
		INNORISKY,
		NOT_INNOVATIVE,
		UNSPECIFIED
}

private IdeaClassifier classifier;
private JSONObject rootJSON;

/**
 * True if predicate is currently evaluating a string.
 * Otherwise false.
 */
private boolean running;

	public IdeaClause(DialogStateController controller, String identifier, Profile profile) {
		super(controller, identifier, profile);
		this.classifier = new IdeaClassifier();
		
		//TODO: IdeaClassifier.json schreiben... 
		try {
            String JSONContent = FileLoader.loadContentOfFile("hu/berlin/dialog/responses/idea.json");
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
    

	public void evaluate(String input) {
		 super.evaluate(input);

	        if (this.isRunning()) {
	            this.put("Einen Moment bitte! Ich schaue noch nach geeigneten Programmen.");
	            return;
	        }

	        this.setRunning(true);
	        InnoCategory category = this.classifier.classify(input);

	        switch (category) {
	            case INNOVATIVE:
	                put(getResponse(ResponseType.INNOVATIVE));
	                break;
	            case INNORISKY:
	                put(getResponse(ResponseType.INNORISKY));
	                break;
	            case NOT_INNOVATIVE:
	                put(getResponse(ResponseType.NOT_INNOVATIVE));
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
        return "Super, vielen Dank! Kannst du deine Gründungsidee jetzt einmal genauer beschreiben "
        		+ "- worum geht es, wie sieht es mit Markt, Mitbewerbern und Innovationsgehalt aus?";
    }

    private String getResponse(ResponseType type) {
        List responses = this.getAllResponses(type);
        String question = (String) responses.get((int)(Math.random() * responses.size()));

        switch (type) {
            case GENERAL:
                break;
            case INNOVATIVE:
                break;
            case UNSPECIFIED:
                break;
            case INNORISKY:
            	break;
            case NOT_INNOVATIVE:
            	break;
            default:
                assert false : "Unhandled case for " + type.toString() + " in IdeaPredicate@getResponse(Questiontype)";
        }

        return question;
    }

}
