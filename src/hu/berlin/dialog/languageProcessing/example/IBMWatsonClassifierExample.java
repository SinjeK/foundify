package hu.berlin.dialog.languageProcessing.example;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Introduction:
 *      http://www.ibm.com/watson/developercloud/natural-language-classifier/api/v1/#introduction
 */
public class IBMWatsonClassifierExample {

    final private static String kIBMWatsonClassifierPassword = "a2i6TsenvJP4";
    final private static String kIBMWatsonClassifierUsername = "cf915869-251f-4120-b26a-f6d8148fff8d";
    final private static String kIBMWatsonClassifierIdentifier = "ff18c7x157-nlc-2455";

    public static void main(String[] args) {
        NaturalLanguageClassifier classifier = new NaturalLanguageClassifier();
        classifier.setUsernameAndPassword(kIBMWatsonClassifierUsername, kIBMWatsonClassifierPassword);

        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while ((input = reader.readLine()) != null) {
                Classification result = classifier.classify(kIBMWatsonClassifierIdentifier, input).execute();
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
