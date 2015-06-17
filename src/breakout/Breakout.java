/**
 * Created by JoeCutting on 14/06/15.
 * Based on code from http://zetcode.com/tutorials/javagamestutorial/breakout/
 * © 2007 - 2015 Jan Bodnar
 */
package breakout;

import javax.swing.JFrame;

public class Breakout extends JFrame {

    public Breakout(Configuration[] configs)
    {
        add(new Board(configs));
        setTitle("Breakout");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Commons.WIDTH, Commons.HEIGHT);
        setLocationRelativeTo(null);
        setIgnoreRepaint(true);
        setResizable(false);
        setVisible(true);
    }

    private static Configuration[] generateConfigRange(double ballSpeedLow, double ballSpeedHigh, int ballSpeedSteps, double paddleSpeedLow, double paddleSpeedHigh,int paddleSpeedSteps)
    {
        Configuration[] configs = new Configuration[ballSpeedSteps*paddleSpeedSteps];
        double ballStep = (ballSpeedHigh-ballSpeedLow)/ballSpeedSteps;
        double paddleStep = (paddleSpeedHigh-paddleSpeedLow)/paddleSpeedSteps;
        for (int i=0;i<ballSpeedSteps;i++)
            for (int j=0;j<paddleSpeedSteps;j++)
            {
                configs[i*paddleSpeedSteps + j] = new Configuration(new PredictionPlayerController(),"");
                configs[i*paddleSpeedSteps + j].setBallSpeed((int) (ballSpeedLow + ballStep * i));
                configs[i*paddleSpeedSteps + j].setPaddleSpeed((int) (paddleSpeedLow+paddleStep*j));
            }
        return configs;
    }

    public static void main(String[] args) {
        Configuration[] configs = new Configuration[Commons.NUMBER_OF_CONFIGS];
        configs[0] = new Configuration(new PredictionPlayerController(), "");

        /* Compares various controllers with a fast or slow ball */
        /*configs[1] = new Configuration(new RandomController(), "fast");
        configs[1].setBallSpeed(4);
        configs[2] = new Configuration(new FollowController(), "slow");
        configs[2].setBallSpeed(2);
        configs[3] = new Configuration(new FollowController(), "fast");
        configs[3].setBallSpeed(4);
        configs[4] = new Configuration(new PlayerModelController(), "slow");
        configs[4].setBallSpeed(2);
        configs[5] = new Configuration(new PlayerModelController(), "fast");
        configs[5].setBallSpeed(4);(*/

        Configuration[] configRange = generateConfigRange(1,5,5,1,5,5);

        new Breakout(configs);
    }
}
