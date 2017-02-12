package hu.berlin.dialog.languageProcessing.example;

import edu.stanford.nlp.ie.NumberNormalizer;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

/**
 * Created by Duc on 08.02.17.
 *
 * http://nlp.stanford.edu/nlp/javadoc/javanlp/
 *
 * POS means Part of Speech Tagger. It classifies if the word is
 * a noun, verb, etc... Possible POS Tags can be found here:
 * http://www.ims.uni-stuttgart.de/forschung/ressourcen/lexika/TagSets/stts-table.html
 *
 * NER means Named Entity Recognition.
 *
 * Dependency relations can be found here
 * http://universaldependencies.org/u/dep/index.html
 *
 */
public class StanfordCoreExample {

    public static void main(String[] args) {

        System.out.println("Start");

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

        try {
            String input;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while ((input = reader.readLine()) != null) {
                Annotation document = new Annotation(input);

                // these are all the sentences in this document
                // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
                List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

                for(CoreMap sentence: sentences) {
                    // traversing the words in the current sentence
                    // a CoreLabel is a CoreMap with additional token-specific methods
                    for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                        // this is the text of the token
                        String word = token.get(CoreAnnotations.TextAnnotation.class);
                        // this is the POS tag of the token
                        String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        // this is the NER label of the token
                        String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                        System.out.println("Word: " + word);
                        System.out.println("POS: " + pos);
                        System.out.println("NER: " + ne);
                        System.out.println("");

                    }

                    SemanticGraph dep = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
                    System.out.println(dep.toList());
                    System.out.println("");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Ende");
    }

}
