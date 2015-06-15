package breakout.log;

import java.io.*;
import java.util.ArrayList;

/**
 * Static functions for handling file output
 *
 * Created by davidgundry on 15/06/15.
 */
public class LogOutput {

    static boolean printEventLog(String filepath, String filename, ArrayList<EventLog> events) {
        try {
            OutputStreamWriter writer = openFile(filepath, filename, ".txt");
            if (writer==null)
                return false;

            for (int i = 0; i < events.size(); i++) {
                writer.write(events.get(i).toString()+"\n");
            }
                closeFile(writer);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        return true;
    }

    static boolean printTimeDifferences(String filepath, String filename, long[] differences)  {
        try {
            OutputStreamWriter writer = openFile(filepath, filename, ".csv");
            if (writer==null)
                return false;

            for (int i=0;i<differences.length;i++)
            {
                writer.write(differences[i]+",");
            }
            closeFile(writer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean printScores(String filepath, String filename, int[] scores)  {
        try {
            OutputStreamWriter writer = openFile(filepath, filename, ".csv");
            if (writer==null)
                return false;

            for (int i=0;i<scores.length;i++)
            {
                writer.write(scores[i]+",");
            }
            closeFile(writer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static OutputStreamWriter openFile(String filepath, String filename, String extension) throws IOException {
        if (createDirectory(filepath)) {
            File file = new File(filename+extension);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            OutputStreamWriter writer  = new OutputStreamWriter(bos, "UTF8");

            return writer;
        }
        return null;
    }

    private static void closeFile(OutputStreamWriter writer) throws IOException {
        writer.flush();
        writer.close();
    }

    private static boolean createDirectory(String filepath) {
        File theDir = new File(filepath);
        boolean result = true;

        if (!theDir.exists()) {
            try {
                theDir.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }

}
