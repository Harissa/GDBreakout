package breakout;

import java.util.Random;

/**
 * Created by JoeCutting on 15/06/15.
 */
public class PlayerModelController extends Controller{
    private Random rand;

    public PlayerModelController() {

        rand = new Random();
    }

    public int getAction(Board board) {
        double ballX = board.ball.getX();
        double paddleX = board.paddle.getX()+(board.paddle.getWidth()/5)*rand.nextInt(5);
        if (ballX>paddleX) return Commons.PADDLE_SPEED;
        if (ballX<paddleX) return -Commons.PADDLE_SPEED;
        return 0;

    }
}
