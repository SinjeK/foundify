import hu.berlin.dialog.DialogSystem;

/**
 * Foundify - Your personal startup bot
 *
 * @author Viet Duc Nguyen, Sinje Kühne
 * @version 0.0.1
 */
public class Main {
    public static void main(String[] args) {
        try {
            DialogSystem system = new DialogSystem();
            system.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
