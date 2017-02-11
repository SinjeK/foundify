package hu.berlin.dialog.languageProcessing;

import java.util.List;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;

import hu.berlin.dialog.configuration.WatsonLanguageClassifierConfig3;
import hu.berlin.dialog.languageProcessing.EmploymentClassifier.EmploymentCategory;

public class TeamClassifier implements Classifier {

	public enum TeamCategory implements Category {
		BUSISCITECH,
		BUSITECH,
		BUSISCI,
		SCITECH,
		BUSI,
		SCI,
		TECH,
		UNSPECIFIED, 
	}
	
	final private static String IDENTIFIER = "f5bbbbx174-nlc-1358";
	private NaturalLanguageClassifier classifier;
	
	public TeamClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword(WatsonLanguageClassifierConfig3.USERNAME, WatsonLanguageClassifierConfig3.PASSWORD);
    }
	
	@Override
	public TeamCategory classify(String input) {
		TeamCategory category;
        Classification result = this.classifier.classify(TeamClassifier.IDENTIFIER, input).execute();
       
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
        
        if (topConfidence < 0.3) {
        	category = TeamCategory.UNSPECIFIED;
        } else {
	        switch (result.getTopClass()) {
	        case "BusiSciTech":
	            category = TeamCategory.BUSISCITECH;
	            break;
	        case "BusiSci":
	            category = TeamCategory.BUSISCI;
	            break;
	        case "BusiTech":
	            category = TeamCategory.BUSITECH;
	            break;
	        case "SciTech":
	            category = TeamCategory.SCITECH;
	            break;
	        case "BusiOnly":
	            category = TeamCategory.BUSI;
	            break;
	        case "SciOnly":
	            category = TeamCategory.SCI;
	            break;
	        case "TechOnly":
	            category = TeamCategory.TECH;
	            break;
	        default:
				category = TeamCategory.UNSPECIFIED;
	        }
        }

    return category;
}

	public static void main(String[] args) {
        System.out.println("Starting testing method for TeamClassifier");

        TeamClassifier classifier = new TeamClassifier();
        TeamCategory category = classifier.classify("meine freundin hat informatik studiert und die andere hat BWL studiert \n");
        System.out.println(category);
    }
	
}