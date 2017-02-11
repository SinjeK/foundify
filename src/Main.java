import hu.berlin.dialog.DialogSystem;

/**
 * Foundify - Your personal startup bot
 *
 * @author Viet Duc Nguyen, Sinje Kühme
 * @version 0.0.1
 */
public class Main {
    public static void main(String[] args) {

        System.out.println("\n\n\nAuthors: Viet-Duc & Sinje");
        System.out.println("Version 0.0.1\n\n\n");

        try {
            DialogSystem system = new DialogSystem();
            system.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
