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
        return list.get(Random.randomNumber(0,list.size()-1));
    }

    public static String randomElement(String[] array) {
        if (array.length > 0) {
            return array[randomNumber(0, array.length)];
        } else {
            return null;
        }
    }

}
