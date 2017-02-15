package hu.berlin.dialog.clause;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import hu.berlin.dialog.DialogAtomicState;
import hu.berlin.dialog.DialogStateController;
import hu.berlin.dialog.languageProcessing.*;
import hu.berlin.dialog.languageProcessing.KindOfAssistanceClassifier.AssistanceCategory;
import hu.berlin.dialog.languageProcessing.YesNoClassifier.YesNoCategory;
import hu.berlin.dialog.languageProcessing.Classifier.Category;
import hu.berlin.dialog.languageProcessing.Classifier.BaseCategory;
import hu.berlin.dialog.clause.Predicates.PredicateConstants;
import hu.berlin.dialog.languageProcessing.CompanyAgeClassifier.CompanyAgeCategory;
import hu.berlin.user.UserProfile;
import hu.berlin.util.Random;
import java.util.HashMap;
import java.util.Map;

public class CompanyClause extends Clause {

    final private static String kYesNoCategory = "yesno";
    final private static String kTimespan = "timespan";
    final private static String kTimespanError = "timespanError";
    final private static String kLookingForCategory = "lookingfor";

    private interface DialogCompanyAtomicState extends DialogAtomicState {
        String[] getFeedbackStrings(Category category);
    }

    private enum State implements DialogCompanyAtomicState {
        DOESCOMPANYEXIST {
            @Override
            public String[] getFeedbackStrings(Category category) {
                String[] yesFeedback = new String[]{"sehr gut, denn für einige Förderprogramme ist es notwendig, " +
                        "dass das Unternehmen bereits existiert"};
                String[] noFeedback = new String[]{"also kommen für dich nur Förderprogramme in Frage, die euch bei" +
                        " der Existenzgründung unterstützen"};

                switch ((YesNoCategory)category) {
                    case YES:
                        return yesFeedback;
                    case NO:
                        return noFeedback;
                    case UNSPECIFIED:
                        // no feedback for unspecified
                        // instead look in getRepeatStrings()
                        return null;
                    default:
                        assert false : "Unhandled case in CompanyClause State: DoesCompanyExist in getFeedbackStrings";
                        return null;
                }
            }

            @Override
            public String[] getRepeatStrings() {
                return new String[]{
                        "ich bin mir nicht sicher, wie ich das verstehen soll\n" +
                                "am besten verneinst oder bejahst du die frage, damit ich alles zweifelsfrei" +
                                "verstehen kann"
                };
            }

            @Override
            public String[] getQuestions() {
                return new String[]{
                        "hast du das startup bereits gegründet?",
                        "existiert das unternehmen bereits?"
                };
            }

            @Override
            public DialogAtomicState handle(Map<String, ?> data, UserProfile profile) {
                YesNoCategory category = (YesNoCategory)data.get(kYesNoCategory);
                State nextState = REPEAT;

                switch (category) {
                    case YES:
                        nextState = FORHOWLONGDOESCOMPANYEXIST;
                        profile.setValueForPredicate(true, PredicateConstants.companyHasBeenFounded);
                        break;
                    case NO:
                        nextState = LOOKINGFOR;
                        profile.setValueForPredicate(false, PredicateConstants.companyHasBeenFounded);
                        break;
                    case UNSPECIFIED:
                        nextState = REPEAT;
                        break;
                }

                return nextState;
            }
        },
        FORHOWLONGDOESCOMPANYEXIST {
            @Override
            public String[] getFeedbackStrings(Category category) {
                // no feed back string
                return null;
            }

            @Override
            public String[] getRepeatStrings() {
                return new String[]{
                        "tut mir leid, ich konnte nicht herauslesen, seit wann das unternehmen schon existiert"
                };
            }

            @Override
            public String[] getQuestions() {
                return new String[]{
                        "wie lange existiert es schon?"
                };
            }

            @Override
            public DialogAtomicState handle(Map<String, ?> data, UserProfile profile) {
                CompanyAgeCategory companyAgeCategory = (CompanyAgeCategory)data.get(kTimespan);

                switch (companyAgeCategory) {
                    case UNSPECIFIED:
                        return REPEAT;
                    case OLDERHAN12YEARS:
                        profile.setValueForPredicate(true, PredicateConstants.companyIsOlderThan12Years);
                        return FASTGROWING;
                    case YOUNGEROREQUALTHAN12YEARS:
                        profile.setValueForPredicate(true, PredicateConstants.companyIsYoungerOrEqualThan12Years);
                        return FASTGROWING;
                    default:
                        assert false : "unhandled switch case";
                        return REPEAT;
                }
            }
        },
        FASTGROWING {
            @Override
            public String[] getFeedbackStrings(Category category) {
                switch ((YesNoCategory)category) {
                    case UNSPECIFIED:
                        return new String[]{
                                "Ist nicht tragisch, falls du es nicht aus dem Kopf weiß"
                        };
                    default:
                        return null;
                }
            }

            @Override
            public String[] getRepeatStrings() {
                return new String[]{
                        "wie bitte?"
                };
            }

            @Override
            public String[] getQuestions() {
                return new String[]{
                        "Ist das Unternehmen in den letzten Jahren um 20% oder mehr gewachsen hinsichtlich der Mitarbeiteranzahl" +
                                "oder des Umsatzes?"
                };
            }

            @Override
            public DialogAtomicState handle(Map<String, ?> data, UserProfile profile) {
                YesNoCategory category = (YesNoCategory)data.get(kYesNoCategory);
                State nextState = REPEAT;

                switch (category) {
                    case YES:
                        profile.setValueForPredicate(true, PredicateConstants.fastGrowth);
                        nextState = MONEYFORSCIENCE;
                        break;
                    case NO:
                        profile.setValueForPredicate(false, PredicateConstants.fastGrowth);
                        nextState = MONEYFORSCIENCE;
                        break;
                    case UNSPECIFIED:
                        profile.setValueForPredicate(false, PredicateConstants.fastGrowth);
                        nextState = MONEYFORSCIENCE;
                }

                return nextState;
            }
        },
        MONEYFORSCIENCE {
            @Override
            public String[] getFeedbackStrings(Category category) {
                switch ((YesNoCategory)category) {
                    case UNSPECIFIED:
                        return new String[]{
                                "Wenn du es nicht weiß, ist es auch nicht so schlimm"
                        };
                    default:
                        return null;
                }
            }

            @Override
            public String[] getRepeatStrings() {
                return new String[]{
                        "sorry konnte dich nicht verstehen"
                };
            }

            @Override
            public String[] getQuestions() {
                return new String[]{
                        "Habt ihr in den letzten drei Jahren wenigstens einmal mehr als 5% eures Unternehmenumsatzes" +
                                " für Forschung und Entwicklung ausgegeben?"
                };
            }

            @Override
            public DialogAtomicState handle(Map<String, ?> data, UserProfile profile) {
                YesNoCategory category = (YesNoCategory)data.get(kYesNoCategory);
                State nextState = REPEAT;

                switch (category) {
                    case YES:
                        profile.setValueForPredicate(true, PredicateConstants.costsAtLeast5PercentOnceInLast3Years);
                        nextState = LOOKINGFOR;
                        break;
                    case NO:
                        profile.setValueForPredicate(false, PredicateConstants.costsAtLeast5PercentOnceInLast3Years);
                        nextState = LOOKINGFOR;
                        break;
                    case UNSPECIFIED: // user doesnt know
                        profile.setValueForPredicate(false, PredicateConstants.costsAtLeast5PercentOnceInLast3Years);
                        nextState = LOOKINGFOR;
                        break;
                }

                return nextState;
            }
        },
        LOOKINGFOR {
            @Override
            public String[] getFeedbackStrings(Category category) {
                switch ((AssistanceCategory)category) {
                    case UNSPECIFIED:
                        return null;
                    case MONEY:
                        return new String[]{
                                "die meisten Förderprogramme bieten finanzielle Unterstützung, also wird sich sicherlich etwas für " +
                                        "dich finden"
                        };
                    case EMPLOYEES:
                        return new String[]{
                            "ich kenne ein Förderprogramm, welches das Gehalt für bis zu zwei neue Angestellte übernimmt\n" +
                                    "Mal sehen ob das für dich passt"
                        };
                    case COACH:
                        return new String[]{
                            "Mal schauen ob ich da was finde. Soweit ich weiß, gibt es einige Förderprogramme " +
                                    "die Coachingseminare anbieten"
                        };
                    default:
                            return null;
                }
            }

            @Override
            public String[] getRepeatStrings() {
                return new String[]{};
            }

            @Override
            public String[] getQuestions() {
                return new String[]{
                        "Nach was für eine Art von Förderung sucht ihr? Also Finanzierungen, Coaches, etc"
                };
            }

            @Override
            public DialogAtomicState handle(Map<String, ?> data, UserProfile profile) {
                AssistanceCategory category = (AssistanceCategory)data.get(kLookingForCategory);
                State nextState = WONINNOPRIZES;

                switch (category) {
                    case UNSPECIFIED:
                        break;
                    case COACH:
                        profile.setValueForPredicate(true, PredicateConstants.lookingForCoaching);
                        break;
                    case EMPLOYEES:
                        profile.setValueForPredicate(true, PredicateConstants.lookingForEmployees);
                        break;
                    case MONEY:
                        profile.setValueForPredicate(true, PredicateConstants.lookingForMoney);
                        break;
                }

                return nextState;
            }
        },
        WONINNOPRIZES {
            @Override
            public String[] getFeedbackStrings(Category category) {
                if (category == YesNoCategory.YES) {
                    return new String[]{
                            "Wow nicht schlecht. Aus meiner Erfahrung erhöht das die Chancen immens :)"
                    };
                }
                return null;
            }

            @Override
            public String[] getRepeatStrings() {
                return new String[]{
                        "Ich glaube nicht, dass ich dich verstehen konnte. tut mir leid"
                };
            }

            @Override
            public String[] getQuestions() {
                return new String[]{
                        "Hast du mit der Idee eigentlich Innovationspreise gewinnen können?"
                };
            }

            @Override
            public DialogAtomicState handle(Map<String, ?> data, UserProfile profile) {
                YesNoCategory category = (YesNoCategory)data.get(kYesNoCategory);
                State nextState = REPEAT;

                switch (category) {
                    case YES:
                    case NO:
                        nextState = CLAIMSPATENT;
                        break;
                    case UNSPECIFIED:
                        nextState = REPEAT;
                        break;
                }

                return nextState;
            }
        },
        CLAIMSPATENT {
            @Override
            public String[] getFeedbackStrings(Category category) {
                if (category == YesNoCategory.YES) {
                    return new String[]{
                            "das sind gute voraussetzungen, wenn du bereits ein patent angemeldet hast, da dies " +
                                    "für gewöhnlich zeigt, dass deine Idee wirklich innovativ ist"
                    };
                }
                return null;
            }

            @Override
            public String[] getRepeatStrings() {
                return new String[]{
                        "Ich glaube nicht, dass ich dich verstehen konnte. tut mir leid\n" +
                                "um klarheit zu schaffen, ich verstehe leider nur simple ja und nein antworten"
                };
            }

            @Override
            public String[] getQuestions() {
                return new String[]{
                        "Interessant wäre auch, ob Patente zu der Idee angemeldet wurden?"
                };
            }

            @Override
            public DialogAtomicState handle(Map<String, ?> data, UserProfile profile) {
                YesNoCategory category = (YesNoCategory)data.get(kYesNoCategory);
                State nextState = REPEAT;

                switch (category) {
                    case YES:
                    case NO:
                        nextState = END;
                        break;
                    case UNSPECIFIED:
                        nextState = REPEAT;
                        break;
                }

                return nextState;
            }
        },
        REPEAT {
            @Override
            public String[] getFeedbackStrings(Category category) { return null; }

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
            public String[] getFeedbackStrings(Category category) { return null; }

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
    }


    private CompanyAgeClassifier companyAgeClassifier;
    private YesNoClassifier yesNoClassifier;
    private KindOfAssistanceClassifier assistanceClassifier;
    private StanfordCoreNLP coreNLP;
    private State currentState;

    public CompanyClause(DialogStateController controller, String identifier, UserProfile profile, StanfordCoreNLP coreNLP) {
        super(controller, identifier, profile);
        this.coreNLP = coreNLP;
        this.assistanceClassifier = new KindOfAssistanceClassifier();
        this.yesNoClassifier = new YesNoClassifier();
        this.companyAgeClassifier = new CompanyAgeClassifier(this.coreNLP);
    }

    @Override
    public void enter() {
        super.enter();
        this.put("kommen wir nun zum Unternehmen");
        this.enterState(State.DOESCOMPANYEXIST);
    }

    @Override
    public void evaluate(String input) {
        super.evaluate(input);

        Map<String, Object> data = new HashMap<>();
        // the category which is about to be calculated
        Category category;

        // determine the correct classifier for the current state
        switch (this.currentState) {
            case DOESCOMPANYEXIST:
            case FASTGROWING:
            case MONEYFORSCIENCE:
            case CLAIMSPATENT:
            case WONINNOPRIZES:
                category = this.yesNoClassifier.classify(input);
                data.put(kYesNoCategory, category);
                break;
            case LOOKINGFOR:
                category = this.assistanceClassifier.classify(input);
                data.put(kLookingForCategory, category);
                break;
            case FORHOWLONGDOESCOMPANYEXIST:
                category = this.companyAgeClassifier.classify(input);
                data.put(kTimespan, category);
                break;
            default:
                category = BaseCategory.UNSPECIFIED;
        }

        // print feedback string
        String feedback = Random.randomElement(this.currentState.getFeedbackStrings(category));
        if (feedback != null) this.put(feedback);

        // enter state
        State nextState = (State)this.currentState.handle(data, this.getProfile());
        this.enterState(nextState);
    }

    private void enterState(State state) {
        if (state != State.REPEAT) {
            this.currentState = state;

            String question = Random.randomElement(state.getQuestions());
            if (question != null) this.put(question);

            if (state == State.END) {
                this.leave();
            }
        } else {
            String repeat = Random.randomElement(this.currentState.getRepeatStrings());
            if (repeat != null) this.put(repeat);
        }
    }

}
