package hu.berlin.dialog.languageProcessing.example;

import de.tuebingen.uni.sfs.germanet.api.GermaNet;
import de.tuebingen.uni.sfs.germanet.api.Synset;
import de.tuebingen.uni.sfs.germanet.relatedness.Frequency;
import de.tuebingen.uni.sfs.germanet.relatedness.Relatedness;
import de.tuebingen.uni.sfs.germanet.relatedness.RelatednessResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class GermaNetRelatednessExample {

    final private static String kGermaNetRessources = "gn/ressources/v90XML";
    final private static String kGermaNetFrequency = "gn/ressources/frequencies";

    public static void main(String[] args) {
        try {
            File dir = GermaNetRelatednessExample.getGermaNetRessourcesDirectory(kGermaNetRessources);
            GermaNet gn = new GermaNet(dir);
            Relatedness relatedness = new Relatedness(gn);

            File freqDir = GermaNetRelatednessExample.getGermaNetRessourcesDirectory(kGermaNetFrequency);
            //Frequency.assignFrequencies(freqDir.getAbsolutePath(), gn);
            HashMap <String, Long> frequencies = Frequency.loadFreq(freqDir.getAbsolutePath() + "/frequencies.csv");

            if (dir != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String input1, input2;
                int depth = 10;

                try {
                    // CMD+D to end input
                    System.out.println("Calculate relatedness between two words");
                    while ((input1 = reader.readLine()) != null && (input2 = reader.readLine()) != null) {
                        List<Synset> synsets1 = gn.getSynsets(input1);
                        List<Synset> synsets2 = gn.getSynsets(input2);

                        for (Synset set1 : synsets1) {
                            for (Synset set2 : synsets2) {
                                RelatednessResult pathRes = relatedness.path(set1, set2);
                                RelatednessResult resnikRes = relatedness.resnik(set1, set2, frequencies);
                                RelatednessResult linRes = relatedness.lin(set1, set2, frequencies);
                                RelatednessResult jiang = relatedness.jiangAndConrath(set1, set2, frequencies);
                                RelatednessResult hirst = relatedness.hirstAndStOnge(set1, set2);
                                RelatednessResult lesk = relatedness.lesk(set1, set2, gn);
                                RelatednessResult wu = relatedness.wuAndPalmer(set1, set2);
                                RelatednessResult leacock = relatedness.leacockAndChodorow(set1,set2);

                                System.out.println(set1.toString() + " " + set2.toString());
                                System.out.println("Path: " + pathRes.getNormalizedResult() + " | Resnik: " + resnikRes.getNormalizedResult()
                                        + " | Lin: " + linRes.getNormalizedResult() + " | Jiang: " + jiang.getNormalizedResult()
                                        + " | hirst " + hirst.getNormalizedResult() + " | Lesk " + lesk.getNormalizedResult()
                                        + " | wu " + wu.getNormalizedResult() + " | leacock " + leacock.getNormalizedResult());
                            }
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

    private static File getGermaNetRessourcesDirectory(String path) {
        URL location = GermaNetExample.class.getClassLoader().getResource(path);
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
}
