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
    private int lastDirection=0;
    private int paddleTarget=2;
    private final int BOUNCE_WAIT=10;//10
    private final int REACTION_TIME=10;
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
        if (board.hasHitPaddle) {
            paddleTarget = 2;//rand.nextInt(5);
        }
        if ((tickCount - bounceTime) > BOUNCE_WAIT) {
            ballX = board.ball.getX();
            lastBallX = ballX;
        } else {
            ballX = lastBallX;
        }

        double paddleX = board.paddle.getX()+(board.paddle.getWidth()/5)*paddleTarget;
        double paddleLeft = board.paddle.getX();
        double paddleRight = board.paddle.getX()+board.paddle.getWidth();
        double noise = rand.nextGaussian()*indexOfDifficulty(Math.abs(paddleX-ballX),board.paddle.getWidth())*FITTS_NOISE;
        //Log.log.console(noise);
        double targetLeft = paddleLeft + noise;
        double targetRight = paddleRight + noise;
        if (ballX>targetRight) {
            direction= board.getCurrentConfig().PADDLE_SPEED;
        } else {
            if (ballX < targetLeft) {
                direction = -board.getCurrentConfig().PADDLE_SPEED;
            } else {
                // go the same direction as the ball
                direction = (board.ball.xdir / board.getCurrentConfig().BALL_SPEED) * board.getCurrentConfig().PADDLE_SPEED;
            }
        }

        // if we're changing direction
        if (direction!=lastDirection) {
            // if not started change timer
            if (changeTime==-100) {
                changeTime = tickCount;
            }
            // if time elapsed
            if ((tickCount-changeTime) >= REACTION_TIME) {
                // set last direction
                lastDirection=direction;
                changeTime=-100;
            } else {
                // don't change direction
                Log.log.console("waiting to change "+direction+" "+lastDirection);
                direction=lastDirection;
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
