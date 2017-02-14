package hu.berlin.dialog;
import hu.berlin.dialog.languageProcessing.GuideClassifier;
import hu.berlin.dialog.languageProcessing.GuideClassifier.GuideCategory;
import hu.berlin.dialog.languageProcessing.YesNoClassifier;
import hu.berlin.dialog.languageProcessing.YesNoClassifier.YesNoCategory;
import hu.berlin.user.UserProfile;

/**
 * This state is responsible for finding the needs of the user
 * and choosing the appropriate state.
 */
public class Guide extends DialogState {

    private static String[] responses = {
            "Wobei kann ich dir helfen?\n" +
                    "(PS: Wenn du nicht weißt, wobei ich alles helfen kann, frag ruhig!)",
            "Wobei brauchst du Hilfe?\n" +
                    "(PS: Wenn du nicht weißt, wobei ich alles helfen kann, frag ruhig!)"
    };

    public enum State {
        ASSISTANCEPROGRAMS,
        UNDEFINIED,
    }

    private GuideClassifier guideClassifier;
    private State nextState;

    public Guide(DialogStateController controller, String identifier, UserProfile profile) {
        super(controller, identifier, profile);
        this.guideClassifier = new GuideClassifier();
    }

    public State getNextState() {
        return this.nextState;
    }

    @Override
    public void enter() {
        put(createResponse());
    }

    @Override
    public void evaluate(String input) {
        GuideCategory category = this.guideClassifier.classify(input);

        switch (category) {
            case ASSISTPROGRAMS:
                this.put("Also gut, suchen wir nach passenden Förderprogrammen für dein Startup");
                this.nextState = State.ASSISTANCEPROGRAMS;
                this.leave();
                break;
            case EVENTS:
                this.put("Zurzeit kann ich dabei nur sehr eingeschränkt helfen");
                this.put("aber hier paar Links, damit du nicht leer ausgehst");
                this.put("http://www.gruenderszene.de/events");
                this.put("https://www.gruenderkueche.de/events/");
                this.put("Was möchtest du noch wissen?");
                break;
            case FUNCTIONS:
                this.put("Also in erster Linie kann ich nach passenden Förderprogrammen suchen und dir somit den Stress bei der Suche abnehmen");
                this.put("Daneben kann ich nach Gründerevents suchen, dich beim Ideenprozess unterstützen, Kontaktpersonen vermitteln" +
                        " und Informationen für die ersten Schritte bei der Startupgründung bereitstellen");
                this.put("Am besten du probierst es einfach aus!");
                break;
            case IDEA:
                this.put("Das kann ich leider noch nicht. Aber irgendwann werde ich ein Bot sein, der Startupideen am Fließband produziert" +
                        " und ich erhalte jeweils 5% Unternehmensanteile und werde reich ^_^");
                this.put("Hier noch ein ernsthafter Tipp: Es kommt nicht auf die Idee an, denn jede Idee kann man tot reden." +
                        "Die Umsetzung ist mindestens genau so wichtig! Das haben mir viele erfolgreiche Startgründer erzählt.");
                this.put("Wobei kann ich dir noch helfen?");
                break;
            case LAW:
                this.put("Uff das ist eine Grauzone. Ich will nicht wegen falscher Beratung verklagt werden. Tut mir leid.");
                break;
            case PERSONS:
                this.put("Zurzeit kenne ich keinen. Aber hier paar Tipps: Jede Uni hat einen Gründungsservice. Da bist du" +
                        " für den Anfang gut aufgehoben. Ansonsten, sprich die Professoren an! Die freuen sich über jede frische Idee" +
                        " , du musst dich nur trauen");
                this.put("Gibt es sonst noch etwas, was ich für dich tun kann?");
                break;
            case STEPS:
                this.put("Also zuerst solltest du eine Idee haben");
                this.put("Und dann irgendwie Geld");
                this.put("und andere Sachen");
                this.put("hm mehr kann ich dazu auch nicht sagen, weil ich mir darüber noch keine Gedanken gemacht habe");
                this.put("Aber bei den anderen Sachen kann ich dir mit Sicherheit weiterhelfen");
                break;
            case UNSPECIFIED:
                this.put("Tut mir leid ich habe dich nicht verstehen können.");
                this.put("Falls du Fragen hast, wobei ich alles helfen kann, schieß einfach los");
                break;
            default:
                assert false : "Unhandled switch statement in Guide class @ evaluate()";
        }
    }

    private String createResponse() {
        int idx = (int) (Math.random()*responses.length);
        return responses[idx];
    }

}
