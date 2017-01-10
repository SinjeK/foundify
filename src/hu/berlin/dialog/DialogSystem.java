package hu.berlin.dialog;
import de.tuebingen.uni.sfs.germanet.api.GermaNet;
import de.tuebingen.uni.sfs.germanet.relatedness.Relatedness;
import hu.berlin.file.FileLoader;
import hu.berlin.dialog.io.DialogInput;
import hu.berlin.dialog.io.DialogInput.DialogInputDelegate;
import hu.berlin.dialog.io.DialogOutput;
import hu.berlin.user.Profile;
import hu.berlin.user.UserProfile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A dialog system manages all dialog states of the chat bot. It is
 * therefore a dialog state controller. Thus it is responsible for
 * entering and leaving states, forwarding user input to the states
 * and managing the transitions. <br><br>
 *
 * Call run() to start the system, quit() to quit the system. <br><br>
 *
 * A dialogsystem runs on its own threadpool.<br>
 */
public class DialogSystem implements DialogInputDelegate, DialogStateController {
    private DialogInput input;
    private DialogOutput output;
    private DialogState currentState;
    private ExecutorService systemService;
    private GermaNet germaNet;
    private Relatedness relatedness;
    private Profile profile;

    final private static String kGermaNetRessources = "gn/ressources/v90XML";
    final private static String kWelcomeIdentifier = "welcome";
    final private static String kGuideIdentifier = "guide";
    final private static String kAssistanceProgramsIdentifier = "assistancePrograms";

    // Constructor
    public DialogSystem() throws Exception {
        super();

        this.systemService = Executors.newSingleThreadExecutor();
        this.input = new DialogInput(this);
        this.input.delegateService = this.systemService;
        this.output = new DialogOutput();
        this.germaNet = new GermaNet(FileLoader.loadRessource(kGermaNetRessources));
        this.relatedness = new Relatedness(this.germaNet);
        this.profile = new UserProfile();
    }

    // Getter & Setter
    private DialogState getCurrentState() {
        return this.currentState;
    }

    private void setCurrentState(DialogState newState) {
        this.currentState = newState;
    }




    // ------ DialogInputDelegate -------------------------------------
    @Override
    public void dialogInputReceivedMessage(DialogInput dialogInput, String message) {
        assert currentState!= null : "Current state is null which is not good. You might have forgotten to use enterState(State)";

        this.currentState.evaluate(message);
    }

    // ----------------------------------------------------------------



    // ------ DialogStateController Delegate --------------------------
    @Override
    public void dialogStateDidLeave(DialogState state) {

        this.currentState = null;

        if (state.getIdentifier().equals(kWelcomeIdentifier)) {
            Guide guideState = new Guide(this, kGuideIdentifier, this.profile, this.germaNet, this.relatedness);
            this.enterState(guideState);
        } else if (state.getIdentifier().equals(kGuideIdentifier)) {
            Guide guide = (Guide)state;

            switch (guide.getNextState()) {
                case ASSISTANCEPROGRAMS: {
                    AssistancePrograms assistanceProgram = new AssistancePrograms(this, kAssistanceProgramsIdentifier, this.profile);
                    this.enterState(assistanceProgram);
                    break;
                }
                case UNDEFINIED: {
                    break;
                }
                default: {
                    assert false : "Unhandled switch case in DialogSystem@dialogStateDidLeave()";
                }
            }
        }
    }

    @Override
    public void dialogStateWantsToOutput(DialogState state, String output) {
        this.output.put(output);
    }


    // ----------------------------------------------------------------


    public void run() {
        this.systemService.submit(() -> {
            this.input.execute();

            Welcome welcomeState = new Welcome(this, kWelcomeIdentifier, this.profile);
            this.enterState(welcomeState);
        });
    }

    public void quit() {
        this.input.stop();
        this.systemService.shutdownNow();
    }


    /**
     * Helper method for entering a state
     * @param state State which is about to be entered
     */
    private void enterState(DialogState state) {
        this.currentState = state;
        state.enter();
    }

}
