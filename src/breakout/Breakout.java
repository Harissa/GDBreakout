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

    public static void main(String[] args) {

        Configuration[] configs = new Configuration[1];
        configs[0] = new Configuration(new PlayerModelController(), "");
       // configs[0] = new Configuration(new RandomController());
        //configs[1] = new Configuration(new FollowController());

        new Breakout(configs);

        //new Breakout(new KeyController());
        //new Breakout(new RandomController());
        //new Breakout(new FollowController());
        //new Breakout(new PlayerModelController());
    }
}
