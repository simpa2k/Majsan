package random;

public class Random {

    public static double random(double min, double max){

        return Math.random() * (max-min) + min;

    }


}
