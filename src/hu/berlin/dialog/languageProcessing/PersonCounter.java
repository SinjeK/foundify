package hu.berlin.dialog.languageProcessing;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;

/**
 * Created by Duc on 10.02.17.
 */
public class PersonCounter {

    private StanfordCoreNLP coreNLP;
    private NumberTagger numberTagger;

    public PersonCounter(StanfordCoreNLP coreNLP) {
        super();
        this.coreNLP = coreNLP;
        this.numberTagger = new NumberTagger(coreNLP);
    }

    public int countPersons(String input) {
        List<Number> numbers = this.numberTagger.findAllNumbers(input);
        int sum = 0;

        for (Number n : numbers) {
            sum += n.intValue();
        }

        Annotation document = new Annotation(input);
        this.coreNLP.annotate(document);

        for (CoreLabel token : document.get(CoreAnnotations.TokensAnnotation.class)) {
            // this is the POS tag of the token
            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

            if (pos.equals("PPOSAT") || pos.equals("PPER")) {
                sum++;
            }
        }

        return sum;

    }

}
