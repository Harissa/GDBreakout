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

}