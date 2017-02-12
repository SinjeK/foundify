package hu.berlin.dialog.languageProcessing;
import edu.stanford.nlp.util.Generics;
import java.util.Map;

/**
 * This class provides methods to parse any string to a number.
 * This includes cardinals as well. For instance: "zu dritt" is
 * parsed to represent the number 3, "erster" is parsed to 1.
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
        /**
        stringToNumber.put("0", 0);
        stringToNumber.put("1", 1);
        stringToNumber.put("2", 2);
        stringToNumber.put("3", 3);
        stringToNumber.put("4", 4);
        stringToNumber.put("5", 5);
        stringToNumber.put("6", 6);
        stringToNumber.put("7", 7);
        stringToNumber.put("8", 8);
        stringToNumber.put("9", 9);
        stringToNumber.put("10", 10);
         **/
    }

    private static final Map<String, Number> otherStringToNumber = Generics.newHashMap();
    static {
        otherStringToNumber.put("alleine", 1);
        otherStringToNumber.put("allein", 1);
        otherStringToNumber.put("einem", 1);
        otherStringToNumber.put("eine", 1);
        otherStringToNumber.put("einen", 1);
        otherStringToNumber.put("ein", 1);
    }

    private static final Map<String, Number> ordinaryStringToNumber = Generics.newHashMap();
    static {
        ordinaryStringToNumber.put("zwei", 2);
        ordinaryStringToNumber.put("dritt", 3);
        ordinaryStringToNumber.put("viert", 4);
        ordinaryStringToNumber.put("fünft", 5);
        ordinaryStringToNumber.put("sechst", 6);
        ordinaryStringToNumber.put("siebent", 7);
        ordinaryStringToNumber.put("acht", 8);
        ordinaryStringToNumber.put("neunt", 9);
        ordinaryStringToNumber.put("zehnt", 10);
    }

    /**
     * Parses a string to a normalized number.
     *
     * @param in The string to be parsed.
     * @return A valid number or null if it could not be
     * parsed or no number was found
     */
    public static Number normalizeStringToNumber(String in) {
        String input = in.toLowerCase();
        try {
            int i = Integer.parseInt(input);
            return i;
        } catch (NumberFormatException e) {
            if (stringToNumber.containsKey(input)) {
                return stringToNumber.get(input);
            } else if (otherStringToNumber.containsKey(input)) {
                return otherStringToNumber.get(input);
            } else if (ordinaryStringToNumber.containsKey(input)) {
                return ordinaryStringToNumber.get(input);
            } else {
                return null;
            }
        }
    }

}
