package zadaca21;


import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


class Driver implements Comparable<Driver> {
 static  int counter = 1;
    String name;
    int lap1;
    int lap2;
    int lap3;
    int best;

    public Driver(String name, int lap1, int lap2, int lap3,int best) {
        this.name = name;
        this.lap1 = lap1;
        this.lap2 = lap2;
        this.lap3 = lap3;
        this.best = Math.min(Math.min(lap1,lap2),lap3);
    }

    public static int stringToTime(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 * 1000 +
                Integer.parseInt(parts[1]) * 1000 +
                Integer.parseInt(parts[2]);
    }

    public static String timeToString(int time) {
        int minutes = (time / 1000) / 60;
        int seconds = (time - minutes * 60 * 1000) / 1000;
        int ms = time % 1000;

        return String.format("%d:%02d:%03d",minutes,seconds,ms);
    }

    public static Driver create(String line) {
        String[] parts = line.split(" ");
        String name = parts[0];
        int lap1 = stringToTime(parts[1]);
        int lap2 = stringToTime(parts[2]);
        int lap3 = stringToTime(parts[3]);
        int bestLap = Math.min(Math.min(lap1,lap2),lap3);
        return new Driver(name,lap1,lap2,lap3,bestLap);
    }


    @Override
    public int compareTo(Driver o) {
        return Integer.compare(this.best,o.best);
    }

    @Override
    public String toString() {
        return String.format("%d. %-10s%10s",counter++,name,timeToString(best));
    }
}


class F1Race {

    List<Driver> drivers;

    public void readResults(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

       drivers = br.lines().map(Driver::create).collect(Collectors.toList());

    }

    public void printSorted(PrintStream out) {

        PrintWriter pw = new PrintWriter(out);

        drivers.stream().sorted(Comparator.naturalOrder()).forEach(pw::println);

        pw.flush();
    }
}
public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

