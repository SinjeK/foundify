package hu.berlin.dialog.languageProcessing;
import edu.stanford.nlp.util.Generics;
import java.util.Map;

/**
 * Created by Duc on 08.02.17.
 */
public class NumberNormalizer {

    private static final Map<String, Number> stringToNumber = Generics.newHashMap();
    static {
        stringToNumber.put("null", 0);
        stringToNumber.put("eins", 1);
        stringToNumber.put("zwei", 2);
        stringToNumber.put("drei", 3);
        stringToNumber.put("vier", 4);
        stringToNumber.put("fünf", 5);
        stringToNumber.put("sechs", 6);
        stringToNumber.put("sieben", 7);
        stringToNumber.put("acht", 8);
        stringToNumber.put("neun", 9);
        stringToNumber.put("zehn", 10);
    }

    private static final Map<String, Number> ordinaryStringToNumber = Generics.newHashMap();
    static {

    }
}
