package breakout;

import breakout.log.Log;

import java.util.Random;

/**
 * Created by JoeCutting on 15/06/15.
 */
public class PredictionPlayerController extends Controller{
    private Random rand;
    private double lastBallX;
    private double lastBallY;
    private double lastBalldX;
    private double lastBalldY;
    private double bounceTime=-100;
    private double changeTime=-100;
    private int lastDirection=0;
    private int paddleTarget=2;
    private final int BOUNCE_WAIT=8;//10
    private final int REACTION_TIME=10;//12;
    private final double FITTS_NOISE=4;

    public PredictionPlayerController() {

        rand = new Random();
    }

    public int getAction(Board board) {
        double ballX, ballY, predictedX;
        int direction=0;
        // takes 100ms to response to change in direction.
        if (board.hasBounced) {
            bounceTime = tickCount;
        }
        if (board.hasHitPaddle) {
            paddleTarget = rand.nextInt(5);
        }
        if ((tickCount - bounceTime) > BOUNCE_WAIT) {
            ballX = board.ball.getX();
            ballY = board.ball.getY();
            lastBallX = ballX;
            lastBallY = ballY;
            lastBalldX = board.ball.getXDir();
            lastBalldY = board.ball.getYDir();
        } else {
            lastBallX+=lastBalldX;
            lastBallY+=lastBalldY;
            ballX = lastBallX;
            ballY = lastBallY;
        }
        if (lastBalldY>0) {
            predictedX = ballX+ ((board.paddle.getY()-ballY)/lastBalldY)*lastBalldX;
        } else {
            predictedX = ballX;
        }


       // Log.log.console("predicted x"+predictedX);
        double paddleX = board.paddle.getX()+(board.paddle.getWidth()/5)*paddleTarget;
        double paddleLeft = board.paddle.getX();
        double paddleRight = board.paddle.getX()+board.paddle.getWidth();
        double noise = rand.nextGaussian()*indexOfDifficulty(Math.abs(paddleX-ballX),board.paddle.getWidth())*FITTS_NOISE;
        //Log.log.console(noise);
        double targetLeft = paddleLeft + noise;
        double targetRight = paddleRight + noise;
        // Aim to hit the paddle
        if (predictedX>targetRight) {
            direction= board.getCurrentConfig().PADDLE_SPEED;
        } else {
            if (predictedX < targetLeft) {
                direction = -board.getCurrentConfig().PADDLE_SPEED;
            } else {
                // if in the paddle then aim to hit the target in the paddle
                double paddleTargetLeft = board.paddle.getX()+(board.paddle.getWidth()/5)*paddleTarget;
                double paddleTargetRight = board.paddle.getX()+(board.paddle.getWidth()/5)*paddleTarget+1;
                if (predictedX>paddleTargetLeft) {
                    direction= board.getCurrentConfig().PADDLE_SPEED;
                } else {
                    if (predictedX < paddleTargetRight) {
                        direction = -board.getCurrentConfig().PADDLE_SPEED;
                    } else {
                        // If in target then go in the same direction as the ball
                        direction = ((int)lastBalldX / board.getCurrentConfig().BALL_SPEED) * board.getCurrentConfig().PADDLE_SPEED;
                    }
                }

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
                //Log.log.console("waiting to change "+direction+" "+lastDirection);
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
