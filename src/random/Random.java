package random;

/**
 * Created by Maja on 2016-12-14.
 */
public class Random {

    public static double random(double min, double max){

        return Math.random() * (max-min) + min;

    }


}
