package hu.berlin.dialog;
import hu.berlin.user.UserProfile;

import java.util.Map;

/**
 * Created by Duc on 01.02.17.
 */
public interface DialogAtomicState {
    DialogAtomicState handle(Map<String, ?> data, UserProfile profile);
}
