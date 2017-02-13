package hu.berlin.dialog.clause;
import java.io.IOException;
import java.util.List;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.clause.Predicates.PredicateConstants;
import hu.berlin.dialog.languageProcessing.PersonCounter;
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

	private PersonCounter personCounter;
	private StanfordCoreNLP coreNLP;
	// private TeamSizeClassifier classifier;
	private JSONObject rootJSON;
	
	/**
	 * True if predicate is currently evaluating a string.
	 * Otherwise false.
	 */
	private boolean running;
	
	public TeamSizeClause(DialogStateController controller, String identifier, UserProfile profile, StanfordCoreNLP coreNLP) {
		super(controller, identifier, profile);
		this.coreNLP = coreNLP;
		this.personCounter = new PersonCounter(coreNLP);
	
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

        put(getResponse(ResponseType.GENERAL));
    }
    

	public void evaluate(String input) {
        super.evaluate(input);

        if (this.isRunning()) {
            this.put("Warte bitte");
            return;
        }

        this.setRunning(true);

        int size = this.personCounter.countPersons(input);

        if (size > 0 && size <= 3) {
            put(getResponse(ResponseType.THREEORFEWER));
            this.getProfile().setIntForPredicate(size, PredicateConstants.teamSize);
            //this.getProfile().setValueForPredicate(true, PredicateConstants.threeMembersOrFewer);
            this.leave();
        } else if (size == 4) {
            put(getResponse(ResponseType.FOUR));
            this.getProfile().setIntForPredicate(size, PredicateConstants.teamSize);
            //this.getProfile().setValueForPredicate(true, PredicateConstants.fourMembers);
            this.leave();
        } else if (size > 4) {
            put(getResponse(ResponseType.MORETHANFOUR));
            this.getProfile().setIntForPredicate(size, PredicateConstants.teamSize);
            //this.getProfile().setValueForPredicate(true, PredicateConstants.moreThanFourMembers);
            this.leave();
        } else {
            this.put("sorry ich konnte dich nicht verstehen");
        }


        this.setRunning(false);
	}
	
	// Response generation
    private List getAllResponses(ResponseType type) {
        return this.rootJSON.getJSONObject("data").getJSONArray(type.name()).toList();
    }

    private String getResponse(ResponseType type) {
        List responses = this.getAllResponses(type);
        String question = (String) responses.get((int)(Math.random() * responses.size()));
        return question;
    }
}
