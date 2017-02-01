package hu.berlin.dialog.languageProcessing;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;

import hu.berlin.dialog.configuration.WatsonLanguageClassifierConfig;
import hu.berlin.dialog.configuration.WatsonLanguageClassifierConfig2;
import hu.berlin.dialog.languageProcessing.EducationClassifier.EducationCategory;

public class EmploymentClassifier implements Classifier {

	public enum EmploymentCategory implements Category {
		UNEMPLOYED,
		STUDENT,
		SCIENTIST,
		OTHER_EMPLOYMENT,
		UNSPECIFIED
	}
	//cedf17x168-nlc-2759 - the joint classifier
	final private static String IDENTIFIER = "ff1c2bx159-nlc-3934";
	private NaturalLanguageClassifier classifier;
	
	public EmploymentClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword(WatsonLanguageClassifierConfig2.USERNAME, WatsonLanguageClassifierConfig2.PASSWORD);
    }
	
	@Override
	public EmploymentCategory classify(String input) {
		EmploymentCategory category;
        Classification result = this.classifier.classify(EmploymentClassifier.IDENTIFIER, input).execute();
       
        switch (result.getTopClass()) {
        case "Studium":
            category = EmploymentCategory.STUDENT;
            break;
        case "Qualification":
            category = EmploymentCategory.SCIENTIST;
            break;
        case "Phd":
            category = EmploymentCategory.OTHER_EMPLOYMENT;
            break;
        case "Master":
            category = EmploymentCategory.UNEMPLOYED;
            break;
        default:
            category = EmploymentCategory.UNSPECIFIED;
    }

    return category;
}

	//args[0] die Eingabe? 
	public static void main(String[] args) {
        System.out.println("Starting testing method for EmploymentClassifier");

        EmploymentClassifier classifier = new EmploymentClassifier();
        EmploymentCategory category = classifier.classify("Ich studiere im Moment Informatik und Informationswissenschaft.");
        System.out.println(category);
    }
	
}
