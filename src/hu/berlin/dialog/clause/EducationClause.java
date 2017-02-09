package hu.berlin.dialog.clause;
import hu.berlin.dialog.clause.Predicates.PredicateConstants;
import hu.berlin.dialog.DialogAtomicState;
import hu.berlin.file.FileLoader;
import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.languageProcessing.EducationClassifier;
import hu.berlin.dialog.languageProcessing.EducationClassifier.EducationCategory;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hu.berlin.user.UserProfile;
import json.JSONObject;

/**
 * Created by Duc on 05.01.17.
 *
 * The dialog diagram can be found in the folder /documentation/dialog
 */
public class EducationClause extends Clause {

    static private String kEducationCategoryKey = "educategory";
    static private String kSinceWhenAcademicQualification = "timeofacademicqualification";

    private enum State implements DialogAtomicState {
        START {
            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                EducationCategory educat = (EducationCategory)data.get(kEducationCategoryKey);
                State nextState;

                switch (educat) {
                    case ABITUR:
                        profile.setValueForPredicate(true, PredicateConstants.hasAbitur);
                        nextState = State.END;
                        break;
                    case QUALIFICATION:
                        profile.setValueForPredicate(true, PredicateConstants.hasQualification);
                        nextState = State.END;
                        break;
                    case MASTER:
                        profile.setValueForPredicate(true, PredicateConstants.hasMaster);
                        nextState = State.RECENT_ACADEMIC_QUALIFICATION;
                        break;
                    case PHD:
                        profile.setValueForPredicate(true, PredicateConstants.hasPHD);
                        nextState = State.RECENT_ACADEMIC_QUALIFICATION;
                        break;
                    case BACHELOR:
                        profile.setValueForPredicate(true, PredicateConstants.hasBachelor);
                        nextState = State.RECENT_ACADEMIC_QUALIFICATION;
                        break;
                    case UNSPECIFIED:
                        nextState = State.REPEAT;
                        break;
                    case STUDIUM:
                        nextState = State.ACADEMIC_QUALIFICATION;
                        break;
                    default:
                        assert false : "Unhandled case for " + educat.toString() + " in EducationClause@getRequest(Questiontype)";
                        nextState = State.REPEAT;
                }

                return nextState;
            };
        },
        ACADEMIC_QUALIFICATION {
            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                EducationCategory category = (EducationCategory)data.get(kEducationCategoryKey);
                State nextState;

                switch (category) {
                    case BACHELOR:
                        profile.setValueForPredicate(true, PredicateConstants.hasBachelor);
                        nextState = State.RECENT_ACADEMIC_QUALIFICATION;
                        break;
                    case MASTER:
                        profile.setValueForPredicate(true, PredicateConstants.hasMaster);
                        nextState = State.RECENT_ACADEMIC_QUALIFICATION;
                        break;
                    case PHD:
                        profile.setValueForPredicate(true, PredicateConstants.hasPHD);
                        nextState = State.RECENT_ACADEMIC_QUALIFICATION;
                        break;
                    default:
                        nextState = State.REPEAT;
                }

                return nextState;
            }
        },
        RECENT_ACADEMIC_QUALIFICATION {
            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                // to be implemented
                return State.END;
            }
        },
        REPEAT {
            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                assert false : "State 'REPEAT' is just an empty state. Do not call its methods";
                return State.REPEAT;
            };
        },
        END {
            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                assert false : "State 'UNSPECIFIED' is just an empty state. Do not call its methods";
                return State.END;
            };
        };
    };

    /**
     * The classifier responsible for assigning a string to
     * a given class. In this case, it recognizes the user's
     * education.
     */
    private EducationClassifier classifier;

    /**
     * This object contains several information. For example
     * `request` holds strings to be presented when the chat
     * requests something from the user. `response` holds
     * strings to be returned when the user has written something.
     */
    private JSONObject rootJSON;

    /**
     * Tracks the current state
     */
    private State currentState;

    /**
     * True if predicate is currently evaluating a string.
     * Otherwise false.
     */
    private boolean running;

    public EducationClause(DialogStateController controller, String identifier, UserProfile profile) {
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

    private State getCurrentState() {
        return this.currentState;
    }

    private void setCurrentState(State state) {
        this.currentState = state;
    }

    // Dialog State
    @Override
    public void enter() {
        super.enter();

        // initialize states
        this.setCurrentState(State.START);
        // print response of the starting state
        put(getRequest(State.START));
    }

    /**
     * Evaluate the input of user. Tries to recognize user' graduation.
     * It also manages the transitions.
     *
     * @param input Input provided by user
     */
    @Override
    public void evaluate(String input) {

        if (this.isRunning()) {
            this.put("Warte ich schaue gerade nach passenden Förderprogrammen");
            return;
        }

        // Processing input
        this.setRunning(true);

        EducationCategory category = this.classifier.classify(input);
        HashMap<String, Object> data = new HashMap<>();
        data.put(kEducationCategoryKey, category);
        State nextState = (State)this.getCurrentState().handle(data, this.getProfile());
        this.put(this.getRequest(nextState));

        switch (nextState) {
            case END:
                this.setCurrentState(State.END);
                this.leave();
                break;
            case REPEAT:
                break;
            default:
                this.setCurrentState(nextState);
        }

        // Processing finished
        this.setRunning(false);
    }

    // Response generation
    private List getAllRequests(State type) {
        return this.rootJSON.getJSONObject("request").getJSONArray(type.name()).toList();
    }

    private String getRequest(State type) {
        List requests = this.getAllRequests(type);
        String question = (String) requests.get((int)(Math.random() * requests.size()));
        return question;
    }
}
