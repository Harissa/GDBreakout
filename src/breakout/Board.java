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
    int currentConfig;

    private int tick = 0;

    boolean ingame = true;
    boolean restartGame = false;
    boolean hasBounced=false;
    boolean hasHitPaddle=false;
    boolean waitToStart=false;
    int timerId;

    Configuration[] configs;


    public Board(Configuration[] configs) {
        this.configs = configs;
        currentConfig=0;
        this.setConfig(configs[currentConfig]);

        addKeyListener(new TAdapter());
        setFocusable(true);

        setDoubleBuffered(true);
        currentTest=0;
        thisBoard = this;

        Log.log.setTrial(getCurrentConfig());
    }

    private void setConfig(Configuration config)
    {
        this.controller = config.getController();
        this.numberOfBricks = config.BRICKS_ACROSS * config.BRICKS_DOWN;
    }

    public void addNotify() {
        super.addNotify();
        restartGame();
    }
    private void restartGame() {
        Log.log.log(Event.GAMESTART,tick);
        restartGame=false;
        currentTest++;
        controller.resetTicks();
        bricks = new Brick[numberOfBricks];
        gameInit();
        if (Configuration.IS_PLAYER) {
            ingame=false;
            message="Press a key to start";
            waitToStart=true;
            repaint();
        } else {
            startGame();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), Configuration.GAME_WAIT, Configuration.TICK_LENGTH);



    }
    private void startGame() {
        waitToStart=false;
        ingame=true;
    }

    public void gameInit() {

        ball = new Ball(getCurrentConfig().BALL_SPEED);
        paddle = new Paddle();
        score =0;

        int k = 0;
        for (int i = 0; i < getCurrentConfig().BRICKS_DOWN; i++) {
            for (int j = 0; j < getCurrentConfig().BRICKS_ACROSS; j++) {
                bricks[k] = new Brick(j * 40 + 60, i * 10 + 50);
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
            drawScore(g);
        } else {

            Font font = new Font("Verdana", Font.BOLD, 18);
            FontMetrics metr = this.getFontMetrics(font);

            g.setColor(Color.BLACK);
            g.setFont(font);
            g.drawString(message,
                    (Commons.WIDTH - metr.stringWidth(message)) / 2,
                    Commons.HEIGHT / 2);
        }


        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    private void drawScore(Graphics g) {
        Font font = new Font("Verdana", Font.PLAIN, 18);
        //FontMetrics metr = this.getFontMetrics(font);

        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("Score:"+score, 10, 20);

    }

    public Configuration getCurrentConfig()
    {
        return configs[currentConfig];
    }

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            controller.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            if (waitToStart) {
                startGame();
            } else {
                controller.keyPressed(e);
            }
        }
    }


    class ScheduleTask extends TimerTask {

        public void run() {
            if (!waitToStart) {
                hasBounced = ball.move();
                controller.increaseTicks();
                int dx = controller.getAction(thisBoard);
                hasBounced = false;
                hasHitPaddle = false;
                paddle.move(dx);

                checkCollision();
                repaint();
                tick++;
                if (controller.isTimeout()) {
                    message = "Time's Up! You scored " + score;
                    Log.log.log(Event.TIMEOUT, tick);
                    stopGame();
                }

                if (restartGame) {
                    restartGame();
                }
            } else {
               repaint();

            }
        }
    }

    public void stopGame() {
        ingame = false;
        timer.cancel();
        Log.log.log(Event.GAMEOVER,tick);
        Log.log.logScore(score,tick);
        try {
            Thread.sleep(Configuration.GAME_WAIT);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        if (currentTest <Configuration.NUMBER_OF_TESTS) {
            restartGame = true;
        } else {
            // finished X tests
            Stats.setScores(Log.log.getScores());
            Log.log.console(Stats.getAverage());
            Log.log.console(Stats.getStdDev());
            Log.log.logOverallStats(currentConfig,Stats.getAverage(),Stats.getStdDev());
            Log.log.output(getCurrentConfig().BRICKS_ACROSS*getCurrentConfig().BRICKS_DOWN);
            currentConfig++;

            if (currentConfig<this.configs.length) {
                Log.log.clear(tick);
                this.setConfig(getCurrentConfig());
                Log.log.setTrial(getCurrentConfig());
                currentTest = 0;
                restartGame=true;
            }
            else
            {
                Log.log.printOverallStats(configs);
            }

        }

    }


    public void checkCollision() {

        if (ball.getRect().getMaxY() > Commons.BOTTOM) {
            message= "Game Over. You scored "+score;
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
            hasBounced=true;
            hasHitPaddle=true;
            Log.log.log(Event.PADDLEHIT,tick);
            int paddleLPos = (int)paddle.getRect().getMinX();
            int ballLPos = (int)ball.getRect().getMinX();

            int first = paddleLPos + 8;
            int second = paddleLPos + 16;
            int third = paddleLPos + 24;
            int fourth = paddleLPos + 32;
            while((ball.getRect()).intersects(paddle.getRect())){
                ball.reverse();
            }

            if (ballLPos < first) {
                ball.setXDir(-getCurrentConfig().BALL_SPEED);
                ball.setYDir(-getCurrentConfig().BALL_SPEED);
            }

            if (ballLPos >= first && ballLPos < second) {
                ball.setXDir(-getCurrentConfig().BALL_SPEED);
                ball.setYDir(-getCurrentConfig().BALL_SPEED * ball.getYDir());
            }

            if (ballLPos >= second && ballLPos < third) {
                ball.setXDir(0);
                ball.setYDir(-getCurrentConfig().BALL_SPEED);
            }

            if (ballLPos >= third && ballLPos < fourth) {
                ball.setXDir(getCurrentConfig().BALL_SPEED);
                ball.setYDir(-getCurrentConfig().BALL_SPEED * ball.getYDir());
            }

            if (ballLPos > fourth) {
                ball.setXDir(getCurrentConfig().BALL_SPEED);
                ball.setYDir(-getCurrentConfig().BALL_SPEED);
            }

        }


        for (int i = 0; i < numberOfBricks; i++) {
            if ((ball.getRect()).intersects(bricks[i].getRect())) {
                hasBounced=true;
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
                    ball.reverse();
                    if (bricks[i].getRect().contains(pointRight)) {
                        ball.setXDir(-getCurrentConfig().BALL_SPEED);
                    }

                    else if (bricks[i].getRect().contains(pointLeft)) {
                        ball.setXDir(getCurrentConfig().BALL_SPEED);
                    }

                    if (bricks[i].getRect().contains(pointTop)) {
                        ball.setYDir(getCurrentConfig().BALL_SPEED);
                    }

                    else if (bricks[i].getRect().contains(pointBottom)) {
                        ball.setYDir(-getCurrentConfig().BALL_SPEED);
                    }
                    score++;
                    if ((!bricks[i].destroyed))
                        Log.log.log(Event.BRICKBREAK, tick);
                    bricks[i].setDestroyed(true);
                }
            }
        }
    }
}
