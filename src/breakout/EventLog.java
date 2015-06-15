package breakout;

/**
 * Created by davidgundry on 15/06/15.
 */

public class EventLog {
    long time;
    Log.Event event;

    EventLog(Log.Event event, long time)
    {
        this.event = event;
        this.time = time;
    }

    public String output()
    {
        return String.valueOf(this.time) + ": " + this.event.toString();
    }

}
