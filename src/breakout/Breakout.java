/**
 * Created by JoeCutting on 14/06/15.
 * Based on code from http://zetcode.com/tutorials/javagamestutorial/breakout/
 * © 2007 - 2015 Jan Bodnar
 */
package breakout;

import javax.swing.JFrame;

public class Breakout extends JFrame {

    public Breakout()
    {
        add(new Board());
        setTitle("Breakout");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Commons.WIDTH, Commons.HEIGTH);
        setLocationRelativeTo(null);
        setIgnoreRepaint(true);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Breakout();
    }
}
