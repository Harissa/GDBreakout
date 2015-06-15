package breakout;

/**
 * Created by JoeCutting on 15/06/15.
 */
public class Stats {
    private static int[] scores;
    private static int size;

    public static void setScores(int[] newScores) {
        scores = newScores;
        size = scores.length;
    }
    public static double getAverage() {
        double sum = 0.0;
        for(double a : scores)
            sum += a;
        return sum/size;
    }
    public static double getVariance()
    {
        double mean = getAverage();
        double temp = 0;
        for(double a :scores)
            temp += (mean-a)*(mean-a);
        return temp/size;
    }
    public static double getStdDev()
    {
        return Math.sqrt(getVariance());
    }
}
