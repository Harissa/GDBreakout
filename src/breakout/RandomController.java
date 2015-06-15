package breakout;

import java.util.Random;

/**
 * Created by JoeCutting on 15/06/15.
 */
public class RandomController extends Controller{
    private Random rand;

    public RandomController() {
        rand = new Random();
    }
    public int getAction(Board board) {
        return (rand.nextInt(3)-1)*Commons.PADDLE_SPEED;
    }
}
