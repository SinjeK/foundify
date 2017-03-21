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
        BYE,
        STARTUP,
        UNSPECIFIED
    }

    // change this if a new classifier is used
    final private static String IDENTIFIER = "90e7b7x198-nlc-3484";
    private NaturalLanguageClassifier classifier;

    public GuideClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword("cb2d2aab-c25e-40e2-9482-421643dea0bc", "XTsgobIPoRjP");
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
            case "Bye":
                category = GuideCategory.BYE;
                break;
            case "StartUp":
                category = GuideCategory.STARTUP;
                break;
            default:
                category = GuideCategory.UNSPECIFIED;
                break;
        }

        return category;
    }

}
