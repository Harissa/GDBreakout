package breakout;

/**
 * Created by davidgundry on 15/06/15.
 */

public class EventLog {

    long time;
    Event event;

    EventLog(Event event, long time)
    {
        this.event = event;
        this.time = time;
    }

    @Override
    public String toString()
    {
        return String.valueOf(this.time) + ": " + this.event.toString();
    }


}
