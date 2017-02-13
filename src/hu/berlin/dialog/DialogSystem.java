package hu.berlin.dialog;
import de.tuebingen.uni.sfs.germanet.api.GermaNet;
import de.tuebingen.uni.sfs.germanet.relatedness.Relatedness;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import hu.berlin.file.FileLoader;
import hu.berlin.dialog.io.DialogInput;
import hu.berlin.dialog.io.DialogInput.DialogInputDelegate;
import hu.berlin.dialog.io.DialogOutput;
import hu.berlin.user.UserProfile;
import hu.berlin.util.Console;
import java.util.Properties;
import java.util.concurrent.*;

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
    private StanfordCoreNLP stanfordCoreNLP;
    // private Relatedness relatedness;
    // private GermaNet germaNet;
    private UserProfile profile;

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
        this.profile = new UserProfile();

        // this.germaNet = new GermaNet(FileLoader.loadRessource(kGermaNetRessources), true);
        // this.relatedness = new Relatedness(this.germaNet);
        Console.enableErrorStream(false);
        //this.output.put("Starte foundify",false);

        this.output.put("");
        this.output.put("███████╗ ██████╗ ██╗   ██╗███╗   ██╗██████╗ ██╗███████╗██╗   ██╗\n" +
                "██╔════╝██╔═══██╗██║   ██║████╗  ██║██╔══██╗██║██╔════╝╚██╗ ██╔╝\n" +
                "█████╗  ██║   ██║██║   ██║██╔██╗ ██║██║  ██║██║█████╗   ╚████╔╝ \n" +
                "██╔══╝  ██║   ██║██║   ██║██║╚██╗██║██║  ██║██║██╔══╝    ╚██╔╝  \n" +
                "██║     ╚██████╔╝╚██████╔╝██║ ╚████║██████╔╝██║██║        ██║   \n" +
                "╚═╝      ╚═════╝  ╚═════╝ ╚═╝  ╚═══╝╚═════╝ ╚═╝╚═╝        ╚═╝   ");
        this.output.put("");

        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
        ScheduledFuture future = schedule.scheduleWithFixedDelay(()->{
            this.output.putOnSameLine(".");
        },0,500, TimeUnit.MILLISECONDS);

        this.stanfordCoreNLP = this.createStanfordCoreNLP();
        future.cancel(true);
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
        assert currentState != null : "Current state is null which is not good. You might have forgotten to use enterState(State)";

        // type ::restart to restart the chat dialog without the need to reload the
        // language processing modules, thus saving a lot of time
        if (message.equals("::restart")) {
            Welcome welcomeState = new Welcome(this, kWelcomeIdentifier, this.profile);
            this.enterState(welcomeState);
        } else {
            this.currentState.evaluate(message);
        }
    }

    // ----------------------------------------------------------------



    // ------ DialogStateController Delegate --------------------------
    @Override
    public void dialogStateDidLeave(DialogState state) {

        this.currentState = null;

        if (state.getIdentifier().equals(kWelcomeIdentifier)) {
            Guide guideState = new Guide(this, kGuideIdentifier, this.profile);
            this.enterState(guideState);
        } else if (state.getIdentifier().equals(kGuideIdentifier)) {
            Guide guide = (Guide)state;

            switch (guide.getNextState()) {
                case ASSISTANCEPROGRAMS: {
                    AssistanceProgramsState assistanceProgram = new AssistanceProgramsState(this, kAssistanceProgramsIdentifier, this.profile, this.stanfordCoreNLP);
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
        } else if (state.getIdentifier().equals(kAssistanceProgramsIdentifier)) {
            this.output.put("Das war's dann. Bis zum nächsten Mal ^_^");
            this.quit();
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
        this.setCurrentState(state);
        state.enter();
    }

    /**
     * Helper method to create the stanford core nlp
     */
    private StanfordCoreNLP createStanfordCoreNLP() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, ner, parse, depparse");
        props.setProperty("tokenize.language", "de");
        props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/german/german-hgc.tagger");
        props.setProperty("ner.applyNumericClassifiers", "false");
        props.setProperty("ner.model", "edu/stanford/nlp/models/ner/german.conll.hgc_175m_600.crf.ser.gz");
        props.setProperty("ner.useSUTime", "false");
        props.setProperty("parse.model", "edu/stanford/nlp/models/lexparser/germanFactored.ser.gz");
        props.setProperty("depparse.model", "edu/stanford/nlp/models/parser/nndep/UD_German.gz");
        props.setProperty("depparse.language", "german");
        return new StanfordCoreNLP(props);
    }

}
