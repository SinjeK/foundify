package hu.berlin.dialog;

import hu.berlin.user.UserProfile;

/**
 * Created by Duc on 10.02.17.
 */
public class EvaluationState extends DialogState {

    private enum State {
        START,
        WHATWASGOOD,
        HANDHABUNG,
        EASY,
        REPEAT,
        UNDERSTAND,
        FLOW,
        OTHER
    }
    private State currentState;

    public EvaluationState(DialogStateController controller, String identifier, UserProfile profile) {
        super(controller, identifier, profile);
    }

    @Override
    public void enter() {
        this.put("\n\nBitte nehme dir Zeit und fülle die Evaluation aus");
        this.currentState = State.START;
        this.put("An welcher Stelle sollte die Handhabung des Gründungs-Chatbots deiner Meinung nach verbessert werden?\n");
    }

    @Override
    public void evaluate(String input) {
        switch (currentState) {
            case START:
                this.currentState = State.WHATWASGOOD;
                this.put("Was hat dir gut gefallen?");
                break;
            case WHATWASGOOD:
                this.currentState = State.HANDHABUNG;
                this.put("Wie hat dir die Handhabung des Chatbots insgesamt gefallen?\nAntworte mit \tsehr gut\t \tgut\t \tmittelmäßig\t \tschlecht\t\t sehr schlecht");
                break;
            case HANDHABUNG:
                this.currentState = State.EASY;
                this.put("Wie einfach fandest du die Handhabung des Bots?\nAntworte mite \tsehr einfach\t \teinfach\t\t mittelmäßig\t eher kompliziert\t sehr kompliziert");
                break;
            case EASY:
                this.currentState = State.REPEAT;
                this.put("Wie oft hat sich der Bot im Gespräch wiederholt?\nAntworte mit \tsehr oft \t\toft \t\tgelegentlich \t\tselten \t\t\tnie");
                break;
            case REPEAT:
                this.currentState = State.UNDERSTAND;
                this.put("Wie oft wurdest du nicht verstanden?\nAntworte mit \tsehr oft \t\toft \t\tgelegentlich \t\tselten \t\t\tnie");
                break;
            case UNDERSTAND:
                this.currentState = State.FLOW;
                this.put("Wie natürlich kam dir der Gesprächsfluss insgesamt vor?\nAntworte mit \tsehr natürlich\t\t natürlich\t etwas natürlich\t unnatürlich\t\tsehr unnatürlich");
                break;
            case FLOW:
                this.currentState = State.OTHER;
                this.put("Noch irgendwelche sonstigen Kommentare?");
                break;
            case OTHER:
                this.leave();
                break;
        }

    }

}
