package hu.berlin.dialog.languageProcessing;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;



public class SmalltalkClassifier implements Classifier {

    public enum SmalltalkCategory implements Category {
        NOSMALLTALK,
        GREETING,
        HOWAREYOU,
        INSULT,
        CONFUSED
    }

    // change this if a new classifier is used
    final private static String IDENTIFIER = "f5b432x172-nlc-3260";
    private NaturalLanguageClassifier classifier;

    public SmalltalkClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword("8076141f-5c2f-4a10-83b0-18c5b74dbef7", "DrDgeUPZ7hV7");
    }

    @Override
    public SmalltalkCategory classify(String input) {
        SmalltalkCategory category;
        Classification result = this.classifier.classify(IDENTIFIER, input).execute();

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
            default:
                assert false : "Returned unknown category in classifier: SmalltalkClassifier - category: " + result.getTopClass();
                category = SmalltalkCategory.NOSMALLTALK;
        }

        return category;
    }

}
