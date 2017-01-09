package hu.berlin.dialog.languageProcessing;
import de.tuebingen.uni.sfs.germanet.api.GermaNet;
import de.tuebingen.uni.sfs.germanet.relatedness.Relatedness;
import json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class YesNoClassifier implements Classifier{

    public enum YesNoCategory implements Category {
        YES,
        NO,
        UNSPECIFIED
    }

    private GermaNet germaNet;
    private Relatedness relatedness;
    private List yesList;
    private List noList;

    public YesNoClassifier(GermaNet germaNet, Relatedness relatedness) {
        super();
        this.germaNet = germaNet;
        this.relatedness = relatedness;

        try {
            JSONObject yesJSON = new JSONObject(getJSON("yes.json"));
            JSONObject noJSON = new JSONObject(getJSON("no.json"));
            this.yesList = yesJSON.getJSONArray("data").toList();
            this.noList = noJSON.getJSONArray("data").toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public YesNoCategory classify(String input) {
        float score = 0;
        String[] components = input.trim().split("\\s+");

        for (String s : components) {
            if (this.yesList.contains(s.toLowerCase())) {
                score += 1;
            }

            if (this.noList.contains(s.toLowerCase())) {
                score -= 1;
            }
        }

        if (score > 0) {
            return YesNoCategory.YES;
        } else if (score < 0) {
            return YesNoCategory.NO;
        } else {
            return YesNoCategory.UNSPECIFIED;
        }
    }

    // helper methods
    private String getJSON(String filename) throws IOException {
        URL location = this.getClass().getClassLoader().getResource("hu/berlin/dialog/languageProcessing/ressources/" + filename);

        if (location == null) {
            System.out.print("Cannot find ressource");
            return null;
        }

        String dirPath = location.getFile();
        byte[] encoded = Files.readAllBytes(Paths.get(dirPath));
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
