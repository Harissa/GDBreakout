package breakout.log;

import breakout.Commons;
import breakout.Controller;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The Log class handles all analytics logging. It contains a static object log,
 * which can be called anywhere to use logging. At the end of a game session,
 * the log is dumped to a file
 *
 * Created by davidgundry on 15/06/15.
 */
public class Log implements Commons {
    public static Log log = new Log();

    private ArrayList<EventLog> events;
    private int[] scores;
    private int nextScore = 0;

    private String filename;
    private static final String filepath = "logoutput/";
    public long startTime;

    Log()
    {
        this("");
    }

    Log(String filename)
    {
        this.events = new ArrayList<EventLog>();
        this.filename = filename;
        this.startTime = System.nanoTime();
        scores = new int[NUMBER_OF_TESTS];
    }

    public void log(Event event)
    {
        EventLog newEvent = new EventLog(event, System.nanoTime());
        events.add(newEvent);
        if (newEvent.getEvent() == Event.GAMESTART)
            Log.console(newEvent.toString());
    }

    public void logScore(int score)
    {
        scores[nextScore] = score;
        nextScore++;

        EventLog newEvent = new EventLog(Event.SCORE, System.nanoTime(), score);
        events.add(newEvent);
        //Log.console(newEvent.toString());
    }

    public int[] getScores() { return scores;}

    public void output(Controller controller, String trialName)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        if (LogOutput.printTimeDifferences(filepath,filename+trialName+"_"+controller.getClass().getSimpleName()+"_n="+NUMBER_OF_TESTS+"_blockTimeDiff_" + dateFormat.format(date),brickTimeDifferences()))
            Log.console("Saved time differences");
        if (LogOutput.printEventLog(filepath, filename+trialName+"_"+controller.getClass().getSimpleName()+"_n="+NUMBER_OF_TESTS+"_eventsLog_"  + dateFormat.format(date), events))
            Log.console("Saved event log");
        if (LogOutput.printScores(filepath, filename+trialName+"_"+controller.getClass().getSimpleName()+"_n="+NUMBER_OF_TESTS+"_scores_"  + dateFormat.format(date), scores))
            Log.console("Saved scores log");
    }

    public static void console(Object o)
    {
        System.out.println(o);
    }

    private long[][] brickTimeDifferences()
    {
        long[][] timeDiffs = new long[NUMBER_OF_TESTS][BRICKS_ACROSS*BRICKS_DOWN];

        long lastBreakEventTime = 0;
        int positionInEvents = -1;
        int gameID = 0;

        while ((gameID < NUMBER_OF_TESTS) && (positionInEvents < events.size())) {
            int record = 0;
            while (positionInEvents < events.size()) {
                positionInEvents++;
                Event e = events.get(positionInEvents).getEvent();
                if (e == Event.GAMESTART)
                    lastBreakEventTime = events.get(positionInEvents).getRelativeTime();
                else if (e == Event.BRICKBREAK) {
                    timeDiffs[gameID][record] = events.get(positionInEvents).getRelativeTime() - lastBreakEventTime;
                    lastBreakEventTime = events.get(positionInEvents).getRelativeTime();
                    record++;
                } else if (e == Event.GAMEOVER)
                    break;
            }
            gameID++;
        }

        return timeDiffs;
    }

    private static long[] toLongPrimitives(Long... objects) {

        long[] primitives = new long[objects.length];
        for (int i = 0; i < objects.length; i++)
            primitives[i] = objects[i];

        return primitives;
    }

}
