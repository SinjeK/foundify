package hu.berlin.dialog.languageProcessing;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;

/**
 * Created by Duc on 09.02.17.
 */
public class GuideClassifier implements Classifier {

    public enum GuideCategory implements Category {
        UNSPECIFIED
    }

    // change this if a new classifier is used
    final private static String IDENTIFIER = "f5b432x172-nlc-2044";
    private NaturalLanguageClassifier classifier;

    public GuideClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword("b762e2a2-f1f5-4b2a-9f91-e089c81048b5", "BA2vp3JSoRhi");
    }

    public GuideCategory classify(String input) {
        return GuideCategory.UNSPECIFIED;
    }

}
