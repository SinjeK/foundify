package hu.berlin.dialog.languageProcessing;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;

import java.util.List;


public class SmalltalkClassifier implements Classifier {

    public enum SmalltalkCategory implements Category {
        NOSMALLTALK,
        GREETING,
        HOWAREYOU,
        INSULT,
        BYE,
        CONFUSED
    }

    // change this if a new classifier is used
    final private static String IDENTIFIER = "90e7b4x199-nlc-2482";
    private NaturalLanguageClassifier classifier;

    public SmalltalkClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword("d806eb6d-6c07-4628-948d-4c2819178cb6", "MlCFo4QG3F3w");
    }

    @Override
    public SmalltalkCategory classify(String input) {
        SmalltalkCategory category;
        Classification result = this.classifier.classify(IDENTIFIER, input).execute();

        List<ClassifiedClass> classes = result.getClasses();
        double topConfidence = 0.0;
        for (ClassifiedClass c : classes) {
            if (c.getConfidence() > topConfidence) {
                topConfidence = c.getConfidence();
            }
        }

        if (topConfidence < 0.85) {
            return SmalltalkCategory.NOSMALLTALK;
        }

        switch (result.getTopClass()) {
            case "Confused":
                category = SmalltalkCategory.CONFUSED;
                break;
            case "HowAreYou":
                category = SmalltalkCategory.HOWAREYOU;
                break;
            case "Greeting":
                category = SmalltalkCategory.GREETING;
                break;
            case "Insult":
                category = SmalltalkCategory.INSULT;
                break;
            case "Bye":
                category = SmalltalkCategory.BYE;
                break;
            default:
                assert false : "Returned unknown category in classifier: SmalltalkClassifier - category: " + result.getTopClass();
                category = SmalltalkCategory.NOSMALLTALK;
        }

        return category;
    }

}
