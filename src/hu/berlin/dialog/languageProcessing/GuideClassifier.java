package hu.berlin.dialog.languageProcessing;

/**
 * Created by Duc on 09.02.17.
 */
public class GuideClassifier implements Classifier {

    public enum GuideCategory implements Category {
        UNSPECIFIED
    }

    public GuideCategory classify(String input) {
        return GuideCategory.UNSPECIFIED;
    }

}
