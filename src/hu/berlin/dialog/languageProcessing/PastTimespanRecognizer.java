package hu.berlin.dialog.languageProcessing;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import hu.berlin.dialog.languageProcessing.PastTimespanRecognizerException;
import hu.berlin.dialog.languageProcessing.PastTimespanRecognizerException.*;
import java.util.*;


/**
 * Created by Duc on 11.02.17.
 */
public class PastTimespanRecognizer {

    // not used
    private static final List<String> keywords = new ArrayList<>();
    static {
        keywords.add("seit");
        keywords.add("gestern");
        keywords.add("vorgestern");
        keywords.add("vor");
    }

    public static final Map<String, Integer> timeunits = new HashMap<>();
    static {
        timeunits.put("jahr", 60*60*24*365);
        timeunits.put("jahre", 60*60*24*365);
        timeunits.put("jahren", 60*60*24*365);
        timeunits.put("monat", 60*60*24*30);
        timeunits.put("monate", 60*60*24*30);
        timeunits.put("monaten", 60*60*24*30);
        timeunits.put("woche", 60*60*24*7);
        timeunits.put("wochen", 60*60*24*7);
        timeunits.put("tag", 60*60*24);
        timeunits.put("tage", 60*60*24);
        timeunits.put("tagen", 60*60*24);
        timeunits.put("stunde", 60*60);
        timeunits.put("stunden", 60*60);
        timeunits.put("minute", 60);
        timeunits.put("minuten", 60);
        timeunits.put("sekunde", 1);
        timeunits.put("sekunden", 1);
    }


    private static final Map<String, Integer> timevalue = new HashMap<>();
    static {
        timevalue.put("gestern", 60*60*24);
        timevalue.put("vorgestern", 60*60*24*2);
        timevalue.put("gerade", 0);
        timevalue.put("jetzt", 0);
        timevalue.put("eben", 0);
    }

    /**
     *
     * @param input
     * @param coreNLP
     * @return the timespan in seconds
     */
    public static int getNormalizedTimespan(String input, StanfordCoreNLP coreNLP) throws PastTimespanRecognizerException {
        Annotation document = new Annotation(input);
        coreNLP.annotate(document);

        List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);

        /*
        if (PastTimespanRecognizer.containsKeywords(document)) {

        } else {
            throw new PastTimespanRecognizerNoTimeException();
        }
        */

        int unitsec = -1;
        int value = -1;
        for(CoreLabel token : tokens) {
            String word = token.get(CoreAnnotations.TextAnnotation.class);

            Number num = NumberNormalizer.normalizeStringToNumber(word);
            if (num != null) {
                value = num.intValue();
            } else if (timevalue.containsKey(word.toLowerCase())) {
                return timevalue.get(word.toLowerCase());
            } else if (timeunits.containsKey(word.toLowerCase())) {
                unitsec = timeunits.get(word.toLowerCase());
            }
        }

        if (unitsec == -1 && value == -1)
            throw new PastTimespanRecognizerMissingTimeUnitAndValueException();
        if (unitsec == -1)
            throw new PastTimespanRecognizerMissingTimeUnitException();
        if (value == -1)
            throw new PastTimespanRecognizerMissingTimeValueException();

        return value * unitsec;
    }

    private static boolean containsKeywords(Annotation document) {
        List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);

        for(CoreLabel token : tokens) {
            String word = token.get(CoreAnnotations.TextAnnotation.class);
            if (keywords.contains(word)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, ner, parse");
        props.setProperty("tokenize.language", "de");
        props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/german/german-hgc.tagger");
        props.setProperty("ner.applyNumericClassifiers", "false");
        props.setProperty("ner.model", "edu/stanford/nlp/models/ner/german.conll.hgc_175m_600.crf.ser.gz");
        props.setProperty("ner.useSUTime", "false");
        props.setProperty("parse.model", "edu/stanford/nlp/models/lexparser/germanFactored.ser.gz");
        StanfordCoreNLP coreNLP = new StanfordCoreNLP(props);

        try {
            int i = PastTimespanRecognizer.getNormalizedTimespan("ich studiere informatik seit ", coreNLP);
            System.out.print(i/(3600*24));
        } catch (PastTimespanRecognizerMissingTimeUnitException ex) {
            System.out.print("Reden wir hier von Jahren, Minuten?");
        } catch (PastTimespanRecognizerMissingTimeValueException ex) {
            System.out.print("Wie viele zeiteinheiten?");
        } catch (PastTimespanRecognizerMissingTimeUnitAndValueException ex) {
            System.out.print("Nichts");
        } catch (PastTimespanRecognizerException ex) {

        }
    }

}
