package hu.berlin.dialog.languageProcessing;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;
import hu.berlin.dialog.languageProcessing.Classifier;
import hu.berlin.dialog.languageProcessing.Classifier.Category;

import java.util.List;

/**
 * Created by Duc on 13.02.17.
 */
public class KindOfAssistanceClassifier implements Classifier {

    public enum AssistanceCategory implements Category {
        MONEY,
        EMPLOYEES,
        COACH,
        UNSPECIFIED
    }

    /**
     * Identifier of used classifier
     */
    final private static String IDENTIFIER = "4d5c10x177-nlc-4509";

    /**
     * If top confidence is lower than this threshold,
     * the category UNSPECIFIED is returned.
     */
    final private static double kConfidenceThreshold = 0.7;

    /**
     * The classifier which is used
     */
    private NaturalLanguageClassifier classifier;

    /**
     * designated constructor
     */
    public KindOfAssistanceClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword("c70864f5-8916-44bc-acd3-fc8a0a150be9", "viGiiyAqcrhK");
    }

    @Override
    public AssistanceCategory classify(String input) {
        AssistanceCategory category;
        Classification result = this.classifier.classify(IDENTIFIER, input).execute();

        List<ClassifiedClass> classes = result.getClasses();
        double topConfidence = 0.0;
        for (ClassifiedClass c : classes) {
            if (c.getConfidence() > topConfidence) {
                topConfidence = c.getConfidence();
            }
        }

        if (topConfidence < kConfidenceThreshold) {
            return AssistanceCategory.UNSPECIFIED;
        }

        switch (result.getTopClass()) {
            case "Coach":
                category = AssistanceCategory.COACH;
                break;
            case "Employees":
                category = AssistanceCategory.EMPLOYEES;
                break;
            case "Money":
                category = AssistanceCategory.MONEY;
                break;
            default:
                category = AssistanceCategory.UNSPECIFIED;
        }

        return category;
    }

}
