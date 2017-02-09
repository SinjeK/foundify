package hu.berlin.dialog;
import hu.berlin.user.UserProfile;

/**
 * A state is an independent unit which is responsible for exactly one task. <br>
 * A state is responsible for evaluating the users input and printing an
 * appropriate message for the user. <br><br>
 * If subclassing, make sure to <u>override</u> <code>enter()</code>
 * and <code>evaluate()</code><br>
 */
public abstract class DialogState {


    //------ PROPERTIES ------------------------------------//


    /**
     * Delegate object
     */
    private DialogStateController controller;

    /**
     * Identifier used for easy identification of states.
     */
    private String identifier;

    private UserProfile profile;


    //------ CONSTRUCTOR  ------------------------------------//


    public DialogState(DialogStateController controller, String identifier, UserProfile profile) {
        super();
        this.controller = controller;
        this.identifier = identifier;
        this.profile = profile;
    }

    /*
    public DialogState() {
        this(null, "");
    }
    */

    protected UserProfile getProfile() {
        return this.profile;
    }

    private void setProfile(UserProfile p) {
        this.profile = p;
    }


    //------ PUBLIC  ------------------------------------//


    /**
     * @return Responsible dialog controller
     */
    public DialogStateController getDialogController() {
        return this.controller;
    }

    /**
     * @return Identifier which was specified by initialization
     */
    public String getIdentifier() {
        return this.identifier;
    }


    //------ SUBCLASS  ------------------------------------//


    /**
     * @param s String to be printed
     */
    public void put(String s) {
        assert this.getDialogController() != null : "Dialog State Controller is null in class " + this.getClass().toString() + "@put(String s)";
        this.controller.dialogStateWantsToOutput(this, s);
    }

    /**
     * Override this function to provide a starting point.
     */
    abstract public void enter();

    /**
     * Use this to signal that the state has finished executing its processes.
     * <p>
     * Subclass notes: <b>Always call super().</b>
     */
    protected void leave() {
        assert this.getDialogController() != null : "Dialog State Controller is null in class " + this.getClass().toString() + "@leave()";
        this.controller.dialogStateDidLeave(this);
    }

    /**
     * Override this function to evaluate the user's input
     * and if necessary return a response. Use <code>dialogStateWantsToOutput
     * (DialogState state, String output)</code> to return such a response.
     *
     * @param input Input typed by the user.
     */
    abstract public void evaluate(String input);
}
