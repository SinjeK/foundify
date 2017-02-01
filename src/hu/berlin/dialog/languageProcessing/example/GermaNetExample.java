package hu.berlin.dialog.languageProcessing.example;
import de.tuebingen.uni.sfs.germanet.api.ConRel;
import de.tuebingen.uni.sfs.germanet.api.GermaNet;
import de.tuebingen.uni.sfs.germanet.api.Synset;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Example and test class for the GermaNet API.
 */
public class GermaNetExample {

    final private static String kGermaNetRessources = "gn/ressources/v90XML";

    public static void main(String[] args) {
        try {
            File dir = GermaNetExample.getGermaNetRessourcesDirectory();
            GermaNet gn = new GermaNet(dir);

            if (dir != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String input;
                int depth = 10;

                try {
                    // CMD+D to end input
                    System.out.println("Type the word for which you want to find its hypernyms");
                    while ((input = reader.readLine()) != null) {
                        // find all synsets of a word
                        List<Synset> synsets = gn.getSynsets(input);
                        if (synsets.size() != 0) {
                            for (Synset set : synsets) {
                                List <Synset> hypernyms = getHypernyms(set, depth, new HashSet<Synset>());
                                System.out.println("\n\nHypernyms of " + set + ": \n");
                                for (Synset hyp : hypernyms) {
                                    System.out.println(hyp.toString());
                                }
                            }
                        } else {
                            System.out.println(input + " is not a member of any synset");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static File getGermaNetRessourcesDirectory() {
        URL location = GermaNetExample.class.getClassLoader().getResource(kGermaNetRessources);
        if (location == null) {
            System.out.print("Cannot find germanet ressources");
            return null;
        }

        String dirPath = location.getFile();
        File dir = new File(dirPath);
        if (!dir.exists()) {
            System.out.print("Directory doesnt exist");
            return null;
        }

        return dir;
    }

    private static List<Synset> getHypernyms(Synset set, int depth, HashSet<Synset> visited) {
        List <Synset> result = new ArrayList<Synset>();
        List <Synset> hypernyms;

        hypernyms = set.getRelatedSynsets(ConRel.has_hypernym);
        visited.add(set);

        for (Synset hyp : hypernyms) {
            if (!visited.contains(hyp)) {
                result.add(hyp);
                if (depth > 1) {
                    result.addAll(getHypernyms(hyp, depth-1, visited));
                }
            }
        }

        return result;
    }

}
