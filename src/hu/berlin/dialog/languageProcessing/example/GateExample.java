package hu.berlin.dialog.languageProcessing.example;
import gate.*;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import javax.sound.midi.SysexMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Duc on 07.02.17.
 */
public class GateExample {

    public static void main(String args[]) {
        System.out.print("Gate Example");

        try {
            Gate.init();

            // dir = directory zu $GATE_HOME/plugins/ANNIE/ANNIE_with_defaults.gapp
            File dir = Gate.getPluginsHome();
            dir = new File(dir, "ANNIE");
            dir = new File(dir, "ANNIE_with_defaults.gapp");

            // ANNIE initialisieren
            CorpusController c = (CorpusController)PersistenceManager.loadObjectFromFile(dir);
            Corpus corpus = Factory.newCorpus("main");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input;
            while ((input = reader.readLine()) != null) {
                Document doc = (Document)Factory.newDocument(input);
                corpus.add(doc);
                c.setCorpus(corpus);
                c.execute();

                AnnotationSet set = doc.getAnnotations();
                System.out.print(set.toString());
            }
        } catch (GateException ge) {
            System.out.print(ge.getMessage());
        } catch (IOException io) {
            System.out.print(io.getMessage());
        }
    }

}
