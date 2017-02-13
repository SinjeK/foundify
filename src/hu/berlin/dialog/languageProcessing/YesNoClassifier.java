package hu.berlin.dialog.languageProcessing;
import json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class YesNoClassifier implements Classifier{

    public enum YesNoCategory implements Category {
        YES,
        NO,
        UNSPECIFIED
    }

    // private GermaNet germaNet;
    // private Relatedness relatedness;

    /**
     * If a string contains words in this list, it is
     * classified as YES
     */
    private List yesList;

    /**
     * If a string contains words in this list, it is
     * classified as NO.
     */
    private List noList;

    /**
     * If a string contains words in this list, it is
     * classified as YES.
     */
    private List<String> keywordsYes;

    public List<String> getKeywordsYes() {
        return keywordsYes;
    }

    public void setKeywordsYes(List<String> keywordsYes) {
        this.keywordsYes = keywordsYes;
    }

    /**
     * If a string contains words in this list, it is
     * classified as NO.
     */
    private List<String> keywordsNo;

    public List<String> getKeywordsNo() {
        return keywordsNo;
    }

    public void setKeywordsNo(List<String> keywordsNo) {
        this.keywordsNo = keywordsNo;
    }


    /**
     * Designated constructor
     */
    public YesNoClassifier() {
        super();
        //this.germaNet = germaNet;
        //this.relatedness = relatedness;

        this.keywordsNo = new ArrayList<>();
        this.keywordsYes = new ArrayList<>();

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
        // \s = whitespace, newline, tabs
        String[] components = input.trim().split("\\s+");

        for (String d : components) {
            String s = d.toLowerCase();
            for (Object o : this.yesList) {
                String n = (String)o;
                if (EditDistance.computeLevenshteinDistance(s,n)<=1 && !this.noList.contains(s)
                        && s.length() > 1 && (this.keywordsNo == null || !this.keywordsNo.contains(s))) {
                    return YesNoCategory.YES;
                }
            }

            if (this.keywordsYes != null) {
                for (String n : this.keywordsYes) {
                    if (EditDistance.computeLevenshteinDistance(s,n)<=1 && !this.noList.contains(s) && s.length() > 1) {
                        return YesNoCategory.YES;
                    }
                }
            }


            if (this.noList.contains(s.toLowerCase()) && s.length() > 1) {
                return YesNoCategory.NO;
            }

            if (this.keywordsNo != null) {
                if (this.keywordsNo.contains(s.toLowerCase())) {
                    return YesNoCategory.NO;
                }
            }
        }


        return YesNoCategory.UNSPECIFIED;
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
