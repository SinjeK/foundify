package hu.berlin.dialog.clause;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import hu.berlin.dialog.DialogAtomicState;
import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.languageProcessing.PastTimespanRecognizer;
import hu.berlin.dialog.languageProcessing.PastTimespanRecognizerException;
import hu.berlin.dialog.languageProcessing.PastTimespanRecognizerException.*;
import hu.berlin.dialog.clause.Predicates.PredicateConstants;
import hu.berlin.file.FileLoader;
import hu.berlin.user.UserProfile;
import json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Duc on 12.02.17.
 */
public class RecentAcademicQualificationClause extends Clause {

    final static private String kTimespanValueKey = "timespanvalue";
    final static private String kTimespanErrorKey = "timespanerror";
    private enum TimespanError {
        NOERROR,
        MISSINGTIMEUNIT,
        MISSINGTIMEVALUE,
        MISSINGTIMEUNITANDVALUE,
        NOTIMESPAN
    }

    private enum State implements DialogAtomicState {
        START {
            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                State nextState;
                TimespanError error = (TimespanError) data.get(kTimespanErrorKey);
                int sec = (Integer) data.get(kTimespanValueKey);

                switch (error) {
                    case NOERROR:
                        int year = sec / (3600 * 24 * 365);
                        profile.setValueForPredicate(year <= 3, PredicateConstants.isRecentGraduate);
                        nextState = END;
                        break;
                    case MISSINGTIMEUNIT:
                        nextState = MISSINGTIMEUNIT;
                        break;
                    case MISSINGTIMEUNITANDVALUE:
                        nextState = MISSINGTIMEUNITANDVALUE;
                        break;
                    case MISSINGTIMEVALUE:
                        nextState = MISSINGTIMEVALUE;
                        break;
                    case NOTIMESPAN:
                        nextState = NOTIMESPAN;
                        break;
                    default:
                        assert false : "Unhandled case in RecentAcademicQualificationClause";
                        nextState = START;
                }

                return nextState;
            }
        },
        MISSINGTIMEUNIT {
            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                assert false : "State 'REPEAT' is just an empty state. Do not call its methods";
                return State.REPEAT;
            };
        },
        MISSINGTIMEUNITANDVALUE {
            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                assert false : "State 'REPEAT' is just an empty state. Do not call its methods";
                return State.REPEAT;
            };
        },
        MISSINGTIMEVALUE {
            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                assert false : "State 'REPEAT' is just an empty state. Do not call its methods";
                return State.REPEAT;
            };
        },
        MISSINGTIMESPAN {
            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                assert false : "State 'REPEAT' is just an empty state. Do not call its methods";
                return State.REPEAT;
            };
        },
        NOTIMESPAN {
            @Override
            public State handle(Map<String, ?> data, UserProfile profile) {
                assert false : "State 'REPEAT' is just an empty state. Do not call its methods";
                return State.REPEAT;
            };
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
        }
    }

    /**
     * This object contains several information. For example
     * `request` holds strings to be presented when the chat
     * requests something from the user. `response` holds
     * strings to be returned when the user has written something.
     */
    private JSONObject rootJSON;

    private StanfordCoreNLP coreNLP;
    private String academicTitle;

    private State currentState;

    private State getCurrentState() {
        return currentState;
    }

    private void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public RecentAcademicQualificationClause(DialogStateController controller, String identifier,
                                             UserProfile profile, StanfordCoreNLP coreNLP, String academicTitle) {
        super(controller, identifier, profile);
        this.coreNLP = coreNLP;
        this.academicTitle = academicTitle;

        try {
            String JSONContent = FileLoader.loadContentOfFile("hu/berlin/dialog/responses/recentacademicqualification.json");
            this.rootJSON = new JSONObject(JSONContent);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void enter() {
        super.enter();

        this.setCurrentState(State.START);
        String question = this.getQuestion(State.START);
        question = question.replace("#ACADEMICTITLE#", this.academicTitle);
        this.put(question);
    }

    @Override
    public void evaluate(String input) {
        super.evaluate(input);

        HashMap<String, Object> data = new HashMap<>();
        int sec;

        try {
            sec = PastTimespanRecognizer.getNormalizedTimespan(input, this.coreNLP);
            data.put(kTimespanValueKey, sec);
            data.put(kTimespanErrorKey, TimespanError.NOERROR);
        } catch (PastTimespanRecognizerMissingTimeUnitException e) {
            data.put(kTimespanErrorKey, TimespanError.MISSINGTIMEUNIT);
        } catch (PastTimespanRecognizerMissingTimeValueException e) {
            data.put(kTimespanErrorKey, TimespanError.MISSINGTIMEVALUE);
        } catch (PastTimespanRecognizerMissingTimeUnitAndValueException e) {
            data.put(kTimespanErrorKey, TimespanError.MISSINGTIMEUNITANDVALUE);
        } catch (PastTimespanRecognizerNoTimeException ex) {
            data.put(kTimespanErrorKey, TimespanError.NOTIMESPAN);
        } catch (PastTimespanRecognizerException ignored)  {

        }

        State nextState = (State)this.getCurrentState().handle(data, this.getProfile());

        switch (nextState) {
            case NOTIMESPAN:
            case MISSINGTIMEUNITANDVALUE:
            case MISSINGTIMEVALUE:
            case MISSINGTIMEUNIT:
            case MISSINGTIMESPAN:
                this.put(this.getQuestion(nextState));
                break;
            case START:
                break;
            case REPEAT:
                break;
            case END:
                this.enterState(State.END);
                this.leave();
                break;
            default:
                assert false : "Unhandled switch case in EducationClause. State: " + nextState.toString();
        }

    }

    // Enter state
    private void enterState(State state) {
        this.setCurrentState(state);
        String question = this.getQuestion(state);

        if (question != null) {
            this.put(question);
        }
    }

    // Response generation
    private List getAllQuestions(State type) {
        return this.rootJSON.getJSONObject("question").getJSONArray(type.name()).toList();
    }

    private String getQuestion(State type) {
        List requests = this.getAllQuestions(type);

        assert requests != null : "Could not find question array in json. Maybe key was spelled wrong?";

        String question = (String) requests.get((int)(Math.random() * requests.size()));
        return question;
    }

}
