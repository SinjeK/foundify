package hu.berlin.dialog.languageProcessing;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import hu.berlin.dialog.clause.CompanyClause;

/**
 * Created by Duc on 13.02.17.
 */
public class CompanyAgeClassifier implements Classifier {

    public enum CompanyAgeCategory implements Category {
        YOUNGEROREQUALTHAN12YEARS, // <=12
        OLDERHAN12YEARS, // > 12
        UNSPECIFIED
    }

    private StanfordCoreNLP coreNLP;

    public CompanyAgeClassifier(StanfordCoreNLP coreNLP) {
        super();
        this.coreNLP = coreNLP;
    }

    @Override
    public CompanyAgeCategory classify(String input) {
        try {
            int sec = PastTimespanRecognizer.getNormalizedTimespan(input, this.coreNLP);
            int year = sec / (3600 * 24 *365);
            if (year <= 12) return CompanyAgeCategory.YOUNGEROREQUALTHAN12YEARS;
            else return CompanyAgeCategory.OLDERHAN12YEARS;
        } catch (PastTimespanRecognizerException e) {
            return CompanyAgeCategory.UNSPECIFIED;
        }
    }
}
