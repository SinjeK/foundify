package hu.berlin.dialog.languageProcessing;
import java.util.List;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;
import hu.berlin.dialog.configuration.WatsonLanguageClassifierConfig3;

/**
 * This classifier is never used. Instead PersonCounter is used to retrieve
 * the size of a team.
 */

/*
public class TeamSizeClassifier implements Classifier {

	public enum SizeCategory implements Category {
		THREEORFEWER,
		FOUR,
		MORETHANFOUR,
		UNSPECIFIED, 
	}
	
	final private static String IDENTIFIER = "f5bbbbx174-nlc-1429";
	private NaturalLanguageClassifier classifier;
	
	public TeamSizeClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword(WatsonLanguageClassifierConfig3.USERNAME, WatsonLanguageClassifierConfig3.PASSWORD);
    }
	
	@Override
	public SizeCategory classify(String input) {
		SizeCategory category;
        Classification result = this.classifier.classify(IDENTIFIER, input).execute();
       
        List<ClassifiedClass> classes = result.getClasses();
        double confidence;
        double topConfidence = 0.0; 
        for (ClassifiedClass c : classes) {
        	confidence = c.getConfidence();
        	if (confidence > topConfidence) {
        		topConfidence = confidence;
        	}
        }
        
        //TODO: welchen Wert nehmen?? 
        if (topConfidence < 0.5) {
        	category = SizeCategory.UNSPECIFIED;
        } else {
	        switch (result.getTopClass()) {
	        case "threeorfewer":
	            category = SizeCategory.THREEORFEWER;
	            break;
	        case "four":
	            category = SizeCategory.FOUR;
	            break;
	        case "morethanfour":
	            category = SizeCategory.MORETHANFOUR;
	            break;
	        default:
				assert false : "Returned unknown category in classifier: TeamSizeClassifier - category: " + result.getTopClass();
				category = SizeCategory.UNSPECIFIED;
	        }
        }

    return category;
}

	public static void main(String[] args) {
        System.out.println("Starting testing method for TeamSizeClassifier");

        TeamSizeClassifier classifier = new TeamSizeClassifier();
        SizeCategory category = classifier.classify("Wir sind zu dritt im Team.");
        System.out.println(category);
    }


}
*/