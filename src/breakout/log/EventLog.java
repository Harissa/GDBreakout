package breakout.log;

/**
 * An EventLog contains everything that needs to be stored about a particular occurrence of an event
 *
 * Created by davidgundry on 15/06/15.
 */
public class EventLog {

    private long absoluteTime;
    private long relativeTime;
    private int score;
    private Event event;

    EventLog(Event event, long time)
    {
        this.event = event;
        this.absoluteTime = time;
        this.relativeTime = time-Log.log.startTime;
    }

    EventLog(Event event, long time, int score)
    {
        this(event,time);
        this.score = score;
    }

    @Override
    public String toString()
    {
        if (this.event == Event.SCORE)
            return String.valueOf(this.relativeTime) + ": " + this.event.toString()+": "+this.score;
        else
            return String.valueOf(this.relativeTime) + ": " + this.event.toString();
    }

    public Event getEvent()
    {
        return event;
    }

    public long getRelativeTime()
    {
        return relativeTime;
    }

    public long getAbsoluteTime()
    {
        return absoluteTime;
    }

}
