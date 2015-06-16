package breakout.log;

import breakout.Commons;
import breakout.Configuration;
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

    private double[][] overallStats;

    private String trialName;
    private String configString;

    private String filename;
    private static final String filepath = "logoutput/";
    public long startTime;

    Log()
    {
        this("");
    }

    Log(String filename)
    {
        this.filename = filename;
        this.clear(0);
        this.overallStats = new double[NUMBER_OF_CONFIGS][2];
    }

    public void clear(int tick)
    {
        this.events = new ArrayList<EventLog>();
        this.startTime = tick;
        scores = new int[NUMBER_OF_TESTS];
        nextScore = 0;
    }

    public void log(Event event, int tick)
    {
        EventLog newEvent = new EventLog(event, tick);
        events.add(newEvent);
        if (newEvent.getEvent() == Event.GAMESTART)
            Log.console(newEvent.toString());
    }

    public void logScore(int score, int tick)
    {
        scores[nextScore] = score;
        nextScore++;

        EventLog newEvent = new EventLog(Event.SCORE, tick, score);
        events.add(newEvent);
        //Log.console(newEvent.toString());
    }

    public int[] getScores() { return scores;}

    public void setTrial(Configuration config)
    {
        this.trialName = config.getName();
        this.configString = config.toString();
    }

    public void output(int bricks)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        if (LogOutput.printTimeDifferences(filepath,filename+trialName+"_"+configString+"_n="+NUMBER_OF_TESTS+"_blockTimeDiff_" + dateFormat.format(date),brickTimeDifferences(bricks)))
            Log.console("Saved time differences");
        if (LogOutput.printEventLog(filepath, filename+trialName+"_"+configString+"_n="+NUMBER_OF_TESTS+"_eventsLog_"  + dateFormat.format(date), events))
            Log.console("Saved event log");
        if (LogOutput.printScores(filepath, filename+trialName+"_"+configString+"_n="+NUMBER_OF_TESTS+"_scores_"  + dateFormat.format(date), scores))
            Log.console("Saved scores log");

    }

    public void printOverallStats(Configuration[] configs)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        if (LogOutput.printOverallStats(filepath,filename+trialName+"_"+configString+"_n="+NUMBER_OF_TESTS+"_overall_"  + dateFormat.format(date), configs, overallStats))
            Log.console("Saved overall stats");
    }

    public static void console(Object o)
    {
        System.out.println(o);
    }

    private long[][] brickTimeDifferences(int bricks)
    {
        long[][] timeDiffs = new long[NUMBER_OF_TESTS][bricks];

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

    public void logOverallStats(int currentConfig, double average, double stdDev) {
        overallStats[currentConfig][0] = average;
        overallStats[currentConfig][1] = stdDev;
    }
}
