/**
 * Created by JoeCutting on 14/06/15.
 */
package breakout;

import breakout.log.Event;
import breakout.log.Log;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;


public class Board extends JPanel implements Commons {

    Image ii;
    Timer timer;
    String message = "Game Over";
    Ball ball;
    Paddle paddle;
    Brick bricks[];
    int numberOfBricks;
    Controller controller;
    Board thisBoard;
    int score;
    int currentTest;

    boolean ingame = true;
    boolean restartGame = false;
    int timerId;


    public Board(Controller newController) {

        controller = newController;
        addKeyListener(new TAdapter());
        setFocusable(true);
        numberOfBricks = BRICKS_ACROSS * BRICKS_DOWN;

        setDoubleBuffered(true);
        currentTest=0;
        thisBoard = this;

    }

    public void addNotify() {
        super.addNotify();
        restartGame();
    }
    private void restartGame() {
        Log.log.log(Event.GAMESTART);
        restartGame=false;
        currentTest++;
        bricks = new Brick[numberOfBricks];
        gameInit();
        ingame=true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 1000, 1);

    }

    public void gameInit() {

        ball = new Ball();
        paddle = new Paddle();
        score =0;

        int k = 0;
        for (int i = 0; i < BRICKS_DOWN; i++) {
            for (int j = 0; j < BRICKS_ACROSS; j++) {
                bricks[k] = new Brick(j * 40 + 30, i * 10 + 50);
                k++;
            }
        }
    }


    public void paint(Graphics g) {
        super.paint(g);

        if (ingame) {
            g.drawImage(ball.getImage(), ball.getX(), ball.getY(),
                    ball.getWidth(), ball.getHeight(), this);
            g.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
                    paddle.getWidth(), paddle.getHeight(), this);

            for (int i = 0; i <numberOfBricks ; i++) {
                if (!bricks[i].isDestroyed())
                    g.drawImage(bricks[i].getImage(), bricks[i].getX(),
                            bricks[i].getY(), bricks[i].getWidth(),
                            bricks[i].getHeight(), this);
            }
        } else {

            Font font = new Font("Verdana", Font.BOLD, 18);
            FontMetrics metr = this.getFontMetrics(font);

            g.setColor(Color.BLACK);
            g.setFont(font);
            g.drawString(message,
                    (Commons.WIDTH - metr.stringWidth(message)) / 2,
                    Commons.WIDTH / 2);
        }


        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            controller.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            controller.keyPressed(e);
        }
    }


    class ScheduleTask extends TimerTask {

        public void run() {

            ball.move();
            int dx = controller.getAction(thisBoard);
            paddle.move(dx);
            checkCollision();
            repaint();
            if (restartGame) {
                restartGame();
            }

        }
    }

    public void stopGame() {
        ingame = false;
        timer.cancel();
        Log.log.log(Event.GAMEOVER);
        //Log.log.logScore(score);
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        if (currentTest <NUMBER_OF_TESTS) {
            restartGame = true;
        } else {
            // finished X tests


            Stats.setScores(Log.log.getScores());
            Log.log.console(Stats.getAverage());
            Log.log.console(Stats.getStdDev());
            Log.log.output();
        }

    }


    public void checkCollision() {

        if (ball.getRect().getMaxY() > BOTTOM) {
            stopGame();
        }

        for (int i = 0, j = 0; i < numberOfBricks; i++) {
            if (bricks[i].isDestroyed()) {
                j++;
            }
            if (j == numberOfBricks) {
                message = "Victory";
                stopGame();
            }
        }

        if ((ball.getRect()).intersects(paddle.getRect())) {
            Log.log.log(Event.PADDLEHIT);
            int paddleLPos = (int)paddle.getRect().getMinX();
            int ballLPos = (int)ball.getRect().getMinX();

            int first = paddleLPos + 8;
            int second = paddleLPos + 16;
            int third = paddleLPos + 24;
            int fourth = paddleLPos + 32;

            if (ballLPos < first) {
                ball.setXDir(-BALL_SPEED);
                ball.setYDir(-BALL_SPEED);
            }

            if (ballLPos >= first && ballLPos < second) {
                ball.setXDir(-BALL_SPEED);
                ball.setYDir(-BALL_SPEED * ball.getYDir());
            }

            if (ballLPos >= second && ballLPos < third) {
                ball.setXDir(0);
                ball.setYDir(-BALL_SPEED);
            }

            if (ballLPos >= third && ballLPos < fourth) {
                ball.setXDir(BALL_SPEED);
                ball.setYDir(-BALL_SPEED * ball.getYDir());
            }

            if (ballLPos > fourth) {
                ball.setXDir(BALL_SPEED);
                ball.setYDir(-BALL_SPEED);
            }
        }


        for (int i = 0; i < numberOfBricks; i++) {
            if ((ball.getRect()).intersects(bricks[i].getRect())) {
                int ballLeft = (int)ball.getRect().getMinX();
                int ballHeight = (int)ball.getRect().getHeight();
                int ballWidth = (int)ball.getRect().getWidth();
                int ballTop = (int)ball.getRect().getMinY();

                Point pointRight =
                        new Point(ballLeft + ballWidth + 1, ballTop);
                Point pointLeft = new Point(ballLeft - 1, ballTop);
                Point pointTop = new Point(ballLeft, ballTop - 1);
                Point pointBottom =
                        new Point(ballLeft, ballTop + ballHeight + 1);

                if (!bricks[i].isDestroyed()) {
                    if (bricks[i].getRect().contains(pointRight)) {
                        ball.setXDir(-BALL_SPEED);
                    }

                    else if (bricks[i].getRect().contains(pointLeft)) {
                        ball.setXDir(BALL_SPEED);
                    }

                    if (bricks[i].getRect().contains(pointTop)) {
                        ball.setYDir(BALL_SPEED);
                    }

                    else if (bricks[i].getRect().contains(pointBottom)) {
                        ball.setYDir(-BALL_SPEED);
                    }
                    score++;
                    bricks[i].setDestroyed(true);
                }
            }
        }
    }
}
