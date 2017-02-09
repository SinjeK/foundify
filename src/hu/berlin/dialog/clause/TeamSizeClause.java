package hu.berlin.dialog.clause;

import java.io.IOException;
import java.util.List;

import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.clause.Predicates.PredicateConstants;
import hu.berlin.dialog.clause.TeamClause.ResponseType;
import hu.berlin.dialog.languageProcessing.TeamClassifier;
import hu.berlin.dialog.languageProcessing.TeamClassifier.TeamCategory;
import hu.berlin.dialog.languageProcessing.TeamSizeClassifier;
import hu.berlin.dialog.languageProcessing.TeamSizeClassifier.SizeCategory;
import hu.berlin.file.FileLoader;
import hu.berlin.user.UserProfile;
import json.JSONObject;

public class TeamSizeClause extends Clause {
	
	public enum ResponseType {
		GENERAL,
		UNSPECIFIED,
		THREEORFEWER,
		FOUR,
		MORETHANFOUR
	}

	private TeamSizeClassifier classifier;
	private JSONObject rootJSON;
	
	/**
	 * True if predicate is currently evaluating a string.
	 * Otherwise false.
	 */
	private boolean running;
	
	public TeamSizeClause(DialogStateController controller, String identifier, UserProfile profile) {
		super(controller, identifier, profile);
		this.classifier = new TeamSizeClassifier();
	
		try {
            String JSONContent = FileLoader.loadContentOfFile("hu/berlin/dialog/responses/teamsize.json");
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
	        SizeCategory category = this.classifier.classify(input);

	        switch (category) {
	            case THREEORFEWER:
	                put(getResponse(ResponseType.THREEORFEWER));
	                this.getProfile().setValueForPredicate(true, PredicateConstants.threeMembersOrFewer);
	                this.leave();
	                break;
	            case FOUR:
	                put(getResponse(ResponseType.FOUR));
	                this.getProfile().setValueForPredicate(true, PredicateConstants.fourMembers);
	                this.leave();
	                break;
	            case MORETHANFOUR:
	                put(getResponse(ResponseType.MORETHANFOUR));
	                this.getProfile().setValueForPredicate(true, PredicateConstants.moreThanFourMembers);
	                this.leave();
	                break;
	            case UNSPECIFIED:
	                put(getResponse(ResponseType.UNSPECIFIED));
	                break;
	            default:
	                //assert false : "Unhandled case in switch! category: " + category.toString();
	            	put(getResponse(ResponseType.UNSPECIFIED));
		            break;
	        }

	        this.setRunning(false);
	}
	
	// Response generation
    private List getAllResponses(ResponseType type) {
        return this.rootJSON.getJSONObject("data").getJSONArray(type.name()).toList();
    }

    private String getWelcomeResponse() {
        return "Super, dann machen wir mit dem nächsten Schritt weiter! Kannst du kurz dein Team vorstellen - wie viele  "
        		+ " und dabei besonders relevante Erfahrungen in Wirtschaft und Wissenschaft beschreiben?";
    }

    private String getResponse(ResponseType type) {
        List responses = this.getAllResponses(type);
        String question = (String) responses.get((int)(Math.random() * responses.size()));

        switch (type) {
            case GENERAL:
                break;
            case THREEORFEWER:
                break;
            case UNSPECIFIED:
                break;
            case FOUR:
            	break;
            case MORETHANFOUR:
            	break;
            default:
                assert false : "Unhandled case for " + type.toString() + " in IdeaPredicate@getResponse(Questiontype)";
        }
        return question;
    }
}
