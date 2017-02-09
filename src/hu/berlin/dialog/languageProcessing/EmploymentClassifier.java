package hu.berlin.dialog.languageProcessing;

import java.util.List;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;

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
	//cedf17x168-nlc-2759 - the joint classifier ff1c2bx159-nlc-3934
	final private static String IDENTIFIER = "cedf17x168-nlc-2759";
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
       
        List<ClassifiedClass> classes = result.getClasses();
        double confidence;
        double topConfidence = 0.0; 
        ClassifiedClass topClass = null;
        for (ClassifiedClass c : classes) {
        	confidence = c.getConfidence();
        	if (confidence > topConfidence) {
        		topConfidence = confidence;
        		topClass = c;
        	}
        }
        System.out.println(topClass.toString());
        
        if (topConfidence < 0.5) {
        	category = EmploymentCategory.UNSPECIFIED;
        	System.out.println(topConfidence);
        } else {
	        switch (result.getTopClass()) {
	        case "Student":
	            category = EmploymentCategory.STUDENT;
	            break;
	        case "Scientist":
	            category = EmploymentCategory.SCIENTIST;
	            break;
	        case "Unemployed":
	            category = EmploymentCategory.UNEMPLOYED;
	            break;
	        case "Other Employment":
	            category = EmploymentCategory.OTHER_EMPLOYMENT;
	            break;
	        default:
	            category = EmploymentCategory.UNSPECIFIED;
	        }      
        }

    return category;
}

	public static void main(String[] args) {
        System.out.println("Starting testing method for EmploymentClassifier");

        EmploymentClassifier classifier = new EmploymentClassifier();
        EmploymentCategory category = classifier.classify("Ich studiere Informatik.");
        System.out.println(category);
    }
	
}
