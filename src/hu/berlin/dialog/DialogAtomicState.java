package hu.berlin.dialog;
import hu.berlin.user.UserProfile;
import java.util.Map;

/**
 * Created by Duc on 01.02.17.
 *
 * A dialog atomic state is the atomic unit of a dialog.
 * It is responsible for determining the flow of the dialog
 * by providing the correct transitions from a state to a
 * state. Additionally, it provides responses from type
 * REPEAT, FEEDBACK and QUESTION.
 *
 */
public interface DialogAtomicState {
    String[] getRepeatStrings();
    // String[] getFeedbackStrings();
    String[] getQuestions();

    DialogAtomicState handle(Map<String, ?> data, UserProfile profile);
}
