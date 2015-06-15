package breakout.log;

/**
 * An EventLog contains everything that needs to be stored about a particular occurrence of an event
 *
 * Created by davidgundry on 15/06/15.
 */
public class EventLog {

    private long absoluteTime;
    private long relativeTime;
    private Event event;

    EventLog(Event event, long time)
    {
        this.event = event;
        this.absoluteTime = time;
        this.relativeTime = time-Log.log.startTime;
    }

    @Override
    public String toString()
    {
        return String.valueOf(this.relativeTime) + ": " + this.event.toString();
    }

}
