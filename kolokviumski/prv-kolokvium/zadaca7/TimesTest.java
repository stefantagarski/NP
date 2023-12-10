package zadaca7;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String message) {
        super(message);
    }
}

class InvalidTimeException extends  Exception {
    public InvalidTimeException(String message) {
        super(message);
    }
}

class Time implements Comparable<Time> {
    int hour;
    int minutes;


    public Time(String time) throws InvalidTimeException, UnsupportedFormatException {
        String[] parts = time.split("\\.");
        if (parts.length == 1){
            parts = time.split(":");
        }
        if (parts.length == 1) {
            throw new UnsupportedFormatException(time);
        }
        this.hour = Integer.parseInt(parts[0]);
        this.minutes = Integer.parseInt(parts[1]);
        if (hour <0 || hour > 23 || minutes < 0 || minutes > 59) {
            throw new InvalidTimeException(time);
        }

    }

    @Override
    public String toString() {
        return String.format("%2d:%02d",hour,minutes);
    }


    public String toStringAMPM() {
        String part = "AM";
        int h = hour;
        if (h ==0) {
            h+=12;
        }
        else if (h ==12) {
            part = "PM";
        } else if (h > 12) {
            h -= 12;
            part = "PM";
        }
        return String.format("%2d:%02d %s",h,minutes,part);
    }

    @Override
    public int compareTo(Time o) {
        if (hour == o.hour)
            return minutes - o.minutes;
        else
            return hour - o.hour;
    }
}

class TimeTable {

    List<Time> times;

    public TimeTable() {
        times = new ArrayList<>();
    }


    public void readTimes(InputStream in) throws InvalidTimeException, UnsupportedFormatException {

        Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            for (String part : parts) {
                Time time = new Time(part);
                times.add(time);
            }
        }
    }

    public void writeTimes(OutputStream out, TimeFormat timeFormat) {
        PrintWriter pw = new PrintWriter(out);

        Collections.sort(times);
        for (Time time : times) {
            if (timeFormat == TimeFormat.FORMAT_24) {
                pw.println(time);
            } else {
                pw.println(time.toStringAMPM());
            }
        }
        pw.flush();
    }
}


public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

