package hu.berlin.dialog.clause;

import java.io.IOException;
import java.util.List;

import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.clause.Predicates.PredicateConstants;
import hu.berlin.dialog.languageProcessing.TeamClassifier;
import hu.berlin.dialog.languageProcessing.TeamClassifier.TeamCategory;
import hu.berlin.file.FileLoader;
import hu.berlin.user.UserProfile;
import json.JSONObject;

public class TeamClause extends Clause {  
	
	public enum ResponseType {
		BUSISCITECH,
		BUSITECH,
		BUSISCI,
		SCITECH,
		BUSI,
		SCI,
		TECH,
		UNSPECIFIED, 
		GENERAL
}

private TeamClassifier classifier;
private JSONObject rootJSON;

/**
 * True if predicate is currently evaluating a string.
 * Otherwise false.
 */
private boolean running;

	public TeamClause(DialogStateController controller, String identifier, UserProfile profile) {
		super(controller, identifier, profile);
		this.classifier = new TeamClassifier();
	
		try {
            String JSONContent = FileLoader.loadContentOfFile("hu/berlin/dialog/responses/team.json");
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
		 
		 //TODO: Teamgröße herausfinden und zum Response Type hinzufügen... 

	        if (this.isRunning()) {
	            this.put("Einen Moment bitte! Ich schaue noch nach geeigneten Programmen.");
	            return;
	        }

	        this.setRunning(true);
	        TeamCategory category = this.classifier.classify(input);

	        switch (category) {
	            case BUSISCITECH:
	                put(getResponse(ResponseType.BUSISCITECH));
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasBusinessExpertise);
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasScientificExpertise);
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasTechnicalExpertise);
	                this.leave();
	                break;
	            case BUSITECH:
	                put(getResponse(ResponseType.BUSITECH));
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasBusinessExpertise);
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasTechnicalExpertise);
	                this.leave();
	                break;
	            case BUSISCI:
	                put(getResponse(ResponseType.BUSISCI));
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasBusinessExpertise);
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasScientificExpertise);
	                this.leave();
	                break;
	            case SCITECH:
	                put(getResponse(ResponseType.SCITECH));
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasTechnicalExpertise);
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasScientificExpertise);
	                this.leave();
	                break;
	            case BUSI:
	                put(getResponse(ResponseType.BUSI));
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasBusinessExpertise);
	                this.leave();
	                break;
	            case SCI:
	                put(getResponse(ResponseType.SCI));
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasScientificExpertise);
	                this.leave();
	                break;
	            case TECH:
	                put(getResponse(ResponseType.TECH));
	                this.getProfile().setValueForPredicate(true, PredicateConstants.memberHasTechnicalExpertise);
	                this.leave();
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
        return "Super, dann machen wir mit dem nächsten Schritt weiter! Kannst du kurz dein Team vorstellen "
        		+ " und dabei besonders relevante Erfahrungen in Wirtschaft und Wissenschaft beschreiben?";
    }

    private String getResponse(ResponseType type) {
        List responses = this.getAllResponses(type);
        String question = (String) responses.get((int)(Math.random() * responses.size()));

        switch (type) {
            case GENERAL:
                break;
            case BUSISCITECH:
                break;
            case UNSPECIFIED:
                break;
            case BUSITECH:
            	break;
            case BUSISCI:
            	break;
            case BUSI:
            	break;
            case SCI:
            	break;
            case TECH:
            	break;
            default:
                assert false : "Unhandled case for " + type.toString() + " in IdeaPredicate@getResponse(Questiontype)";
        }

        return question;
    }

}
