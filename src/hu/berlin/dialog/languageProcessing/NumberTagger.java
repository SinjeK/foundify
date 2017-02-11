package hu.berlin.dialog.languageProcessing;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Duc on 10.02.17.
 */
public class NumberTagger {

    private StanfordCoreNLP coreNLP;

    public NumberTagger(StanfordCoreNLP coreNLP) {
        super();
        this.coreNLP = coreNLP;
    }

    public List<Number> findAllNumbers(String input) {

        List<Number> result = new ArrayList<>();
        Annotation document = new Annotation(input);
        this.coreNLP.annotate(document);

        for (CoreLabel token : document.get(CoreAnnotations.TokensAnnotation.class)) {
            // this is the text of the token
            String word = token.get(CoreAnnotations.TextAnnotation.class);
            // this is the POS tag of the token
            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

            if (pos.equals("CARD") || pos.equals("ADJD") || pos.equals("ART")) {
                Number number = NumberNormalizer.normalizeStringToNumber(word);
                if (number != null) {
                    result.add(NumberNormalizer.normalizeStringToNumber(word));
                }
            }

        }

        return result;
    }

    // test method
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, ner, parse, depparse");
        props.setProperty("tokenize.language", "de");
        props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/german/german-hgc.tagger");
        props.setProperty("ner.applyNumericClassifiers", "false");
        props.setProperty("ner.model", "edu/stanford/nlp/models/ner/german.conll.hgc_175m_600.crf.ser.gz");
        props.setProperty("ner.useSUTime", "false");
        props.setProperty("parse.model", "edu/stanford/nlp/models/lexparser/germanFactored.ser.gz");
        props.setProperty("depparse.model", "edu/stanford/nlp/models/parser/nndep/UD_German.gz");
        props.setProperty("depparse.language", "german");
        StanfordCoreNLP core = new StanfordCoreNLP(props);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        NumberTagger tagger = new NumberTagger(core);

        try {
            while ((input = reader.readLine()) != null) {
                System.out.println(tagger.findAllNumbers(input));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
