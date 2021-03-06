package hu.berlin.dialog.languageProcessing;
import java.util.List;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;

public class EmploymentClassifier implements Classifier {

   public enum EmploymentCategory implements Category {
       PUPIL,
       UNEMPLOYED,
       STUDENT,
       SCIENTIST,
       OTHER_EMPLOYMENT,
       UNSPECIFIED
   }

   final private static String IDENTIFIER = "90e7b7x198-nlc-2508";
   private NaturalLanguageClassifier classifier;
   
   public EmploymentClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword("c0442b24-13d8-4093-be63-d6339798f2a1", "QhFNwGAcR5Mo");
    }
   
   @Override
   public EmploymentCategory classify(String input) {
       EmploymentCategory category;
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

       // System.out.println(topClass.toString());
        
        if (topConfidence < 0.6) {
           category = EmploymentCategory.UNSPECIFIED;
          // System.out.println(topConfidence);
        } else {
	        switch (result.getTopClass()) {
	            case "Schuler":
	                category = EmploymentCategory.PUPIL;
                    break;
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
                    assert false : "Returned unknown category in classifier: EmploymentClassifier - category: " + result.getTopClass();
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