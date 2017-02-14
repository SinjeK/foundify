package hu.berlin.dialog.languageProcessing;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;
import java.util.List;


public class GuideClassifier implements Classifier {

    public enum GuideCategory implements Category {
        ASSISTPROGRAMS,
        IDEA,
        LAW,
        PERSONS,
        STEPS,
        EVENTS,
        FUNCTIONS,
        UNSPECIFIED
    }

    // change this if a new classifier is used
    final private static String IDENTIFIER = "f5b42fx173-nlc-3988";
    private NaturalLanguageClassifier classifier;

    public GuideClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword("b762e2a2-f1f5-4b2a-9f91-e089c81048b5", "BA2vp3JSoRhi");
    }

    public GuideCategory classify(String input) {
        GuideCategory category;
        Classification result = this.classifier.classify(IDENTIFIER, input).execute();

        List<ClassifiedClass> classes = result.getClasses();
        double topConfidence = 0.0;
        for (ClassifiedClass c : classes) {
            if (c.getConfidence() > topConfidence) {
                topConfidence = c.getConfidence();
            }
        }

        if (topConfidence < 0.7) {
            return GuideCategory.UNSPECIFIED;
        }

        switch (result.getTopClass()) {
            case "AssistPrograms":
                category = GuideCategory.ASSISTPROGRAMS;
                break;
            case "Idea":
                category = GuideCategory.IDEA;
                break;
            case "Unspecified":
                category = GuideCategory.UNSPECIFIED;
                break;
            case "Jura":
                category = GuideCategory.LAW;
                break;
            case "Kontaktpersonen":
                category = GuideCategory.PERSONS;
                break;
            case "Steps":
                category = GuideCategory.STEPS;
                break;
            case "Events":
                category = GuideCategory.EVENTS;
                break;
            case "Help":
                category = GuideCategory.FUNCTIONS;
                break;
            default:
                category = GuideCategory.UNSPECIFIED;
                break;
        }

        return category;
    }

}
