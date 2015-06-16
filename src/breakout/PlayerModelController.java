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
        // takes 100ms to response to change in direction.
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
        double noise = rand.nextGaussian()*indexOfDifficulty(Math.abs(paddleX-ballX),board.paddle.getWidth())*10;
        Log.log.console(noise);
        double targetX = paddleX + noise;
        if (ballX>targetX) return board.getCurrentConfig().PADDLE_SPEED;
        if (ballX<targetX) return -board.getCurrentConfig().PADDLE_SPEED;
        return 0;

    }
    private double indexOfDifficulty(double distanceToTarget,double targetWidth) {
        return log2(distanceToTarget*2/targetWidth);
    }
    private static double log2(double x)
    {
        return (Math.log(x) / Math.log(2));
    }
}
