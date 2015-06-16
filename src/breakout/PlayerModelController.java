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
    private double changeTime=-100;
    private int lastDirecion=0;
    private final int BOUNCE_WAIT=10;//10
    private final int REACTION_TIME=25;
    private final double FITTS_NOISE=5;

    public PlayerModelController() {

        rand = new Random();
    }

    public int getAction(Board board) {
        double ballX;
        int direction=0;
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
        double noise = rand.nextGaussian()*indexOfDifficulty(Math.abs(paddleX-ballX),board.paddle.getWidth())*FITTS_NOISE;
        Log.log.console(noise);
        double targetX = paddleX + noise;
        if (ballX>targetX) direction= board.getCurrentConfig().PADDLE_SPEED;
        if (ballX<targetX) direction= -board.getCurrentConfig().PADDLE_SPEED;
        // if we're changing direction
        if (direction!=lastDirecion) {
            // if not started change timer
            if (changeTime!=-1) {
                changeTime = tickCount;
            }
            // if time elapsed
            if ((tickCount-changeTime) > REACTION_TIME) {
                // set last direction
                lastDirecion=direction;
                changeTime=-1;
            } else {
                // don't change direction
                direction=lastDirecion;
            }
        }
        return direction;

    }
    private double indexOfDifficulty(double distanceToTarget,double targetWidth) {
        return log2(distanceToTarget*2/targetWidth);
    }
    private static double log2(double x)
    {
        return (Math.log(x) / Math.log(2));
    }
}
