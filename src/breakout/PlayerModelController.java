package breakout;

import breakout.log.Log;

import java.util.Random;

/**
 * Created by JoeCutting on 15/06/15.
 */
public class PlayerModelController extends Controller{
    private Random rand;
    private double lastBallX;
    private double bounceTime=-100;
    private final int BOUNCE_WAIT=10;//10

    public PlayerModelController() {

        rand = new Random();
    }

    public int getAction(Board board) {
        double ballX;
        if (board.hasBounced) {
            bounceTime = tickCount;
        }
        if ((tickCount - bounceTime) > BOUNCE_WAIT) {
            ballX = board.ball.getX();
            lastBallX = ballX;
        } else {
            ballX = lastBallX;
        }

        double paddleX = board.paddle.getX()+(board.paddle.getWidth()/5)*rand.nextInt(5);
        if (ballX>paddleX) return Commons.PADDLE_SPEED;
        if (ballX<paddleX) return -Commons.PADDLE_SPEED;
        return 0;

    }
}
