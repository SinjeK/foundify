package hu.berlin.dialog.languageProcessing;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;
import java.util.List;


/**
 * Assigns a short text to a set of education categories.
 * This can be used to identify the user's education. <br><br>
 * It uses Watson's Natural Language Classifier to
 * recognize the category of a text.
 */
public class EducationClassifier implements Classifier {

    public enum EducationCategory implements Category {
        NOEDUCATION,
        STUDIUM,
        PHD,
        MASTER,
        BACHELOR,
        ABITUR,
        QUALIFICATION,
        UNSPECIFIED
    }

    // change this if a new classifier is used
    final private static String IDENTIFIER = "f5b42ex171-nlc-3504";
    private NaturalLanguageClassifier classifier;

    public EducationClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword("84c6b852-4d9d-4e3d-9625-ceff7d379dba", "xFu6osAbLYot");
    }

    @Override
    public EducationCategory classify(String input) {
        EducationCategory category;
        Classification result = this.classifier.classify(IDENTIFIER, input).execute();

        List<ClassifiedClass> classes = result.getClasses();
        double topConfidence = 0.0;
        for (ClassifiedClass c : classes) {
            if (c.getConfidence() > topConfidence) {
                topConfidence = c.getConfidence();
            }
        }

        if (topConfidence < 0.7) {
            return EducationCategory.UNSPECIFIED;
        }

        switch (result.getTopClass()) {
            case "Studium":
                category = EducationCategory.STUDIUM;
                break;
            case "Qualification":
                category = EducationCategory.QUALIFICATION;
                break;
            case "Phd":
                category = EducationCategory.PHD;
                break;
            case "Master":
                category = EducationCategory.MASTER;
                break;
            case "Bachelor":
                category = EducationCategory.BACHELOR;
                break;
            case "Abitur":
                category = EducationCategory.ABITUR;
                break;
            case "NoEducation":
                category = EducationCategory.NOEDUCATION;
                break;
            default:
                category = EducationCategory.UNSPECIFIED;
        }

        return category;
    }

    public static void main(String[] args) {
        System.out.println("Starting testing method for EducationClassifier");

        EducationClassifier classifier = new EducationClassifier();
        EducationCategory category = classifier.classify("Hallo ich heiße Duc und bin Informatikstudent");
        System.out.println(category);
    }

}
