package hu.berlin.dialog.languageProcessing;

/**
 * A classifier is the public interface for all classes
 * whose purpose is to assign the user's input to predefined
 * categories.<br><br>
 *
 * Follow these steps to create a class implementing Classifier:
 * <pre>
 *     1. Create an enum implementing Category
 *     (This enum contains all used categories)
 * </pre>
 * <pre>
 *     2. Implement classify
 * </pre>
 */
interface Classifier {

    /**
     * This interface is intended to use with enums.
     */
    interface Category {}
    enum BaseCategory implements Category {
        UNSPECIFIED
    }

    /**
     * Computes the category/ classification of a text
     *
     * @param input The text to be classified
     * @return Classification of the text
     */
    Category classify(String input);

}
