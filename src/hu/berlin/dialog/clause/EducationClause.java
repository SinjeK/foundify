package hu.berlin.dialog.clause;
import hu.berlin.dialog.languageProcessing.Classifier.*;
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
 *
 * The dialog diagram can be found in the folder /documentation/dialog
 */
public class EducationClause extends Clause {

    private interface StateAction {
        State handle(Category category);
    };

    private enum State implements StateAction {
        START {
            @Override
            public State handle(Category category) {
                EducationCategory educat = (EducationCategory)category;
                State nextState;

                switch (educat) {
                    case ABITUR:
                        nextState = State.END;
                        break;
                    case QUALIFICATION:
                        nextState = State.END;
                        break;
                    case MASTER:
                    case PHD:
                    case BACHELOR:
                        nextState = State.RECENT_ACADEMIC_QUALIFICATION;
                        break;
                    case UNSPECIFIED:
                        nextState = State.REPEAT;
                        break;
                    case STUDIUM:
                        nextState = State.ACADEMIC_QUALIFICATION;
                        break;
                    default:
                        assert false : "Unhandled case for " + category.toString() + " in EducationClause@getResponse(Questiontype)";
                        nextState = State.REPEAT;
                }

                return nextState;
            };
        },
        ACADEMIC_QUALIFICATION {
            @Override
            public State handle(Category category) {

            }
        },
        RECENT_ACADEMIC_QUALIFICATION {
            @Override
            public State handle(Category category) {

            }
        },
        REPEAT {
            @Override
            public State handle(Category category) {
                assert false : "State 'REPEAT' is just an empty state. Do not call its methods";
                return State.REPEAT;
            };
        },
        END {
            @Override
            public State handle(Category category) {
                assert false : "State 'UNSPECIFIED' is just an empty state. Do not call its methods";
                return State.END;
            };
        };
    };

    private EducationClassifier classifier;
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
        put(getResponse(State.START));
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
        State nextState = this.getCurrentState().handle(category);
        this.setCurrentState(nextState);

        if (nextState == State.END) {
            this.leave();
        }

        // Processing finished
        this.setRunning(false);
    }

    // Response generation
    private List getAllResponses(State type) {
        return this.rootJSON.getJSONObject("data").getJSONArray(type.name()).toList();
    }

    private String getResponse(State type) {
        List responses = this.getAllResponses(type);
        String question = (String) responses.get((int)(Math.random() * responses.size()));
        return question;
    }

}
