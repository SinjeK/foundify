package hu.berlin.dialog;

/**
 * This interface specifies the communication between a controller and its states.
 */
public interface DialogStateController {

    /**
     * Use this to signal the controller that the state wants to print something. <br>
     * The controller is then responsible for the output.
     *
     * @param state State which wants to print something
     * @param output Content to be printed
     */
    void dialogStateWantsToOutput(DialogState state, String output);

    /**
     * Use this to signal the controller that a state has finished. <br>
     * Normally, the controller would chose the next transition based
     * on the left dialog state. You can use the state's identifier
     * property to uniquely recognize the state.
     *
     * @param state State which had finished
     */
    void dialogStateDidLeave(DialogState state);
}
