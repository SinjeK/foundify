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

   final private static String IDENTIFIER = "f5b42fx173-nlc-3619";
   private NaturalLanguageClassifier classifier;
   
   public EmploymentClassifier() {
        super();
        this.classifier = new NaturalLanguageClassifier();
        this.classifier.setUsernameAndPassword("f8faa55e-f483-4c0e-9c7a-ac92c1f8908b", "uXF62QHoMR1z");
    }
   
   @Override
   public EmploymentCategory classify(String input) {
      EmploymentCategory category;
        Classification result = this.classifier.classify(EmploymentClassifier.IDENTIFIER, input).execute();
       
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
        
        if (topConfidence < 0.5) {
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