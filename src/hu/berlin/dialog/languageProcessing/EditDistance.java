package hu.berlin.dialog.languageProcessing;

/**
 * Created by Duc on 09.01.17.
 */
public class EditDistance {

    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    /**
     * Source: https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
     * @param word1
     * @param word2
     * @return
     */
    public static int computeLevenshteinDistance(String word1, String word2) {
        int[][] distance = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++)
            distance[i][0] = i;
        for (int j = 1; j <= word2.length(); j++)
            distance[0][j] = j;

        for (int i = 1; i <= word1.length(); i++)
            for (int j = 1; j <= word2.length(); j++)
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + ((word1.charAt(i - 1) == word2.charAt(j - 1)) ? 0 : 1));

        return distance[word1.length()][word2.length()];
    }

}
