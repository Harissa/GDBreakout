package breakout;

/**
 * This class holds all of the relevant information about a particular configuration of the game
 *
 * Created by davidgundry on 16/06/15.
 */
public class Configuration {

    public int PADDLE_SPEED=2;
    public int BALL_SPEED=2;
    public int BRICKS_ACROSS=12;//6
    public int BRICKS_DOWN=5;

    private Controller controller;

    public Configuration(Controller controller)
    {
        this.controller = controller;
    }

    public Controller getController()
    {
        return this.controller;
    }

    public void setPaddleSpeed(int speed)
    {
        this.PADDLE_SPEED = speed;
    }

    public void setBallSpeed(int speed)
    {
        this.BALL_SPEED = speed;
    }

    public void setBricksDimensions(int across, int down)
    {
        this.BRICKS_ACROSS = across;
        this.BRICKS_DOWN = down;
    }

}
