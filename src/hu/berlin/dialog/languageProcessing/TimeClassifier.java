package hu.berlin.dialog.languageProcessing;

/**
 * Created by Duc on 07.02.17.
 */
public class TimeClassifier implements Classifier {

    public enum TimeCategory implements Category {
        RECENT,
        LONGAGO,
        UNSPECIFIED
    }

    private int timeThreshold;

    public TimeClassifier(int timeThreshold) {
        super();
        this.timeThreshold = timeThreshold;
    }

    @Override
    public Category classify(String input) {
        return TimeCategory.RECENT;
    }

}
