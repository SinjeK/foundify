package hu.berlin.dialog.clause;
import java.io.IOException;
import java.util.List;
import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.clause.Predicates.PredicateConstants;
import hu.berlin.dialog.languageProcessing.IdeaClassifier;
import hu.berlin.dialog.languageProcessing.IdeaClassifier.InnoCategory;
import hu.berlin.dialog.languageProcessing.NumberNormalizer;
import hu.berlin.dialog.languageProcessing.NumberTagger;
import hu.berlin.dialog.languageProcessing.YesNoClassifier;
import hu.berlin.dialog.languageProcessing.YesNoClassifier.YesNoCategory;
import hu.berlin.file.FileLoader;
import hu.berlin.user.UserProfile;
import json.JSONObject;

public class IdeaClause extends Clause {

	/*
	private enum ResponseType {
		GENERAL,     //first question
		INNOVATIVE,
		INNORISKY,
		NOT_INNOVATIVE,
		UNSPECIFIED
	}
	*/

	private enum State {
		START,
		ISINNO,
		ISRISKY
	}

	private State currentState;
	private IdeaClassifier classifier;
	private JSONObject rootJSON;

	public IdeaClause(DialogStateController controller, String identifier, UserProfile profile) {
		super(controller, identifier, profile);
		this.classifier = new IdeaClassifier();
		
		try {
            String JSONContent = FileLoader.loadContentOfFile("hu/berlin/dialog/responses/idea.json");
            this.rootJSON = new JSONObject(JSONContent);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
		
	}

    // Dialog State
    @Override
    public void enter() {
        super.enter();

        this.currentState = State.START;
        put(getResponse(State.START));
    }
    

	public void evaluate(String input) {
		 super.evaluate(input);

		 switch (currentState) {
			 case START:
			 	// feedback
			 	this.currentState = State.ISINNO;
			 	this.put(this.getResponse(State.ISINNO));
			 	break;
			 case ISINNO:
				 try {
					 Number i = NumberNormalizer.normalizeStringToNumber(input);
					 if (i.intValue() >= 11) {
					 	this.put("Da ist jemand wohl sehr überzeugt von seiner Idee");
						 this.getProfile().setValueForPredicate(true, PredicateConstants.isInnovative);
					 } else if (i.intValue() >= 7) {
						 this.put("Also du schätzt die Idee schon als innnovativ ein");
						 this.getProfile().setValueForPredicate(true, PredicateConstants.isInnovative);
					 } else {
						 this.put("Ok danke für die Antwort. Dies dient hauptsächlich der Selbstreflexion");
						 this.getProfile().setValueForPredicate(false, PredicateConstants.isInnovative);
					 }
					 this.currentState = State.ISRISKY;
					 this.put(this.getResponse(State.ISRISKY));
				 } catch (Exception e) {
					 this.put("Ich konnte das leider nicht interpretieren");
				 }
				 break;
			 case ISRISKY:
				 YesNoClassifier yesNoClassifier = new YesNoClassifier();
				 YesNoCategory yesNoCategory = yesNoClassifier.classify(input);

				 switch (yesNoCategory) {
					 case UNSPECIFIED:
						 this.put("Wie bitte?");
						 break;
					 case YES:
					 case NO:
						 this.getProfile().setValueForPredicate(yesNoCategory == YesNoCategory.YES, PredicateConstants.failureIsPossible);
						 this.leave();
						 break;
				 }
				 break;
		 }
	}
	
	// Response generation
    private List getAllResponses(State state) {
        return this.rootJSON.getJSONObject("data").getJSONArray(state.name()).toList();
    }

    private String getResponse(State state) {
        List responses = this.getAllResponses(state);
        String question = (String) responses.get((int)(Math.random() * responses.size()));
        return question;
    }

}
