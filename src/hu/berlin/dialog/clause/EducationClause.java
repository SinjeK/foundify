package hu.berlin.dialog.clause;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import hu.berlin.dialog.DialogState;
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
import hu.berlin.util.Random;
import json.JSONObject;

/**
 * Created by Duc on 05.01.17.
 *
 * The dialog diagram can be found in the folder /documentation/dialog
 */
public class EducationClause extends Clause implements DialogStateController {

    static private String kEducationCategoryKey = "educategory";

    private enum State implements DialogAtomicState {
        START {
            @Override
            public String[] getQuestions() {
                return new String[] {
                        "Erzähl mir etwas über deine Abschlüsse. Sei es nun eine abgeschlossene Ausbildung, " +
                                "ein Bachelor, Master oder gar ein Doktortitel",
                        "Was hast du so für Abschlüsse? Erzähle mir am besten alles von einer abgeschlossenen " +
                                "Ausbildung über Bachelortitel bis zur Promotion"
                };
            }

            @Override
            public String[] getRepeatStrings() {
                return new String[]{
                        "Entschuldigung ich habe dich nicht verstehen können\n" +
                                "Wäre lieb von dir, wenn du dich anders ausdrücken könntest?"
                };
            }

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
                    case NOEDUCATION:
                        nextState = State.END;
                        break;
                    default:
                        assert false : "Unhandled case for " + educat.toString() + " in EducationClause@getQuestion(Questiontype)";
                        nextState = State.REPEAT;
                }

                return nextState;
            };
        },
        ACADEMIC_QUALIFICATION {
            @Override
            public String[] getQuestions() {
                return new String[] {
                        "Noch eine Frage: Hast du auf Bachelor oder Master studiert?"
                };
            }

            @Override
            public String[] getRepeatStrings() {
                return new String[]{
                        "Ich bin mir nicht sicher, wie ich das interpretieren soll." +
                                "Könntest du dich daher bitte anders formulieren?\n"
                };
            }

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
            public String[] getQuestions() { return new String[] {}; }

            @Override
            public String[] getRepeatStrings() { return new String[]{}; }

            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                return State.END;
            }
        },
        REPEAT {
            @Override
            public String[] getQuestions() { return new String[] {}; }

            @Override
            public String[] getRepeatStrings() { return new String[]{}; }

            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                assert false : "State 'REPEAT' is just an empty state. Do not call its methods";
                return State.REPEAT;
            };
        },
        END {
            @Override
            public String[] getQuestions() {
                return new String[] {
                        "Vielen Dank für die super Antworten!",
                        "Super, das sollte fürs erste ausreichen"
                };
            }

            @Override
            public String[] getRepeatStrings() { return new String[]{}; }

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

    private StanfordCoreNLP coreNLP;
    private RecentAcademicQualificationClause recentAcademicQualificationClause;

    /**
     * True if predicate is currently evaluating a string.
     * Otherwise false.
     */
    private boolean running;

    public EducationClause(DialogStateController controller, String identifier, UserProfile profile, StanfordCoreNLP coreNLP) {
        super(controller, identifier, profile);
        this.classifier = new EducationClassifier();
        this.coreNLP = coreNLP;

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
        this.enterState(State.START);
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

        if (this.getCurrentState() == State.RECENT_ACADEMIC_QUALIFICATION) {
            this.recentAcademicQualificationClause.evaluate(input);
            return;
        }

        // Processing input
        this.setRunning(true);

        EducationCategory category = this.classifier.classify(input);
        String feedback = this.getFeedback(category);
        if (feedback != null) {
            this.put(feedback);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put(kEducationCategoryKey, category);
        State nextState = (State)this.getCurrentState().handle(data, this.getProfile());

        switch (nextState) {
            case END:
                this.enterState(State.END);
                this.leave();
                break;
            case ACADEMIC_QUALIFICATION:
                this.enterState(nextState);
                break;
            case REPEAT:
                break;
            case RECENT_ACADEMIC_QUALIFICATION:
                String academicTitle = "akademischen Titel";

                switch (category) {
                    case PHD:
                        academicTitle = "Doktorgrad";
                        break;
                    case MASTER:
                        academicTitle = "Master";
                        break;
                    case BACHELOR:
                        academicTitle = "Bachelor";
                        break;
                }

                this.recentAcademicQualificationClause = new RecentAcademicQualificationClause(this, "recent", this.getProfile(),
                        this.coreNLP, academicTitle);
                this.recentAcademicQualificationClause.enter();
                this.setCurrentState(nextState);
                break;
            default:
                assert false : "Unhandled switch case in EducationClause. State: " + nextState.toString();
        }

        // Processing finished
        this.setRunning(false);
    }

    private List getAllFeedbacks(EducationCategory category) {
        return this.rootJSON.getJSONObject("feedback").getJSONArray(category.name()).toList();
    }

    private String getFeedback(EducationCategory category) {
        List requests = this.getAllFeedbacks(category);

        assert requests != null : "Could not find feedback array in json. Maybe key was spelled wrong?";

        String question = (String) requests.get((int)(Math.random() * requests.size()));
        return question;
    }

    // Enter state
    private void enterState(State state) {
        this.setCurrentState(state);
        String question = Random.randomElement(state.getQuestions());
        if (question != null) this.put(question);
    }

    // Dialog state controller
    @Override
    public void dialogStateWantsToOutput(DialogState state, String output) {
        this.put(output);
    }

    @Override
    public void dialogStateDidLeave(DialogState state) {
        if (state.getIdentifier().equals("recent")) {
            this.enterState(State.END);
            this.leave();
        }
    }

}
