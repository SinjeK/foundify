package hu.berlin.util;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Duc on 12.02.17.
 */
public class Random {

    public static int randomNumber(int min, int max) {
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNum;
    }

    public static Object randomElement(List list) {
        if (list == null) return null;
        if (list.size()==1) return list.get(0);
        if (list.size() > 1) return list.get(Random.randomNumber(0,list.size()-1));

        return null;
    }

    public static String randomElement(String[] array) {

        if (array == null)
            return null;

        if (array.length == 1) {
            return array[0];
        } else if (array.length > 1) {
            return array[randomNumber(0, array.length-1)];
        } else {
            return null;
        }
    }

}
