package zadaca19;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Element {
    int timeFrom;
    int timeTo;
    String text;
    int number;

    public Element(int number, String time, String text) {
        this.number = number;
        String[] parts = time.split("-->");
        timeFrom = stringToTime(parts[0].trim());
        timeTo = stringToTime(parts[1].trim());
        this.text = text;
    }

    static int stringToTime(String time) {
        String[] parts = time.split(",");
        int res = Integer.parseInt(parts[1]);
        parts = parts[0].split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        res += seconds * 1000;
        res += minutes * 60 * 1000;
        res += hours * 60 * 60 * 1000;
        return res;
    }
    static String timeToString(int time) {
        int hours = time / (60 * 60 * 1000);
        time = time % (60 * 60 * 1000);
        int minutes = time / (60 * 1000);
        time = time % (60 * 1000);
        int seconds = time / 1000;
        int ms = time % 1000;
        return String.format("%02d:%02d:%02d,%03d", hours, minutes, seconds, ms);
    }

    public void shift(int ms) {
        timeFrom += ms;
        timeTo += ms;
    }
    public boolean findText(String someText) {
        return text.contains(someText);
    }

    @Override
    public String toString() {
        return String.format("%d\n%s --> %s\n%s\n",number,timeToString(timeFrom),timeToString(timeTo),text);
    }
}

class Subtitles {
    List<Element> elements;

    public Subtitles() {
        elements = new ArrayList<>();
    }

    public int loadSubtitles(InputStream in) {
        Scanner scanner = new Scanner(in);
        int subtitleCount = 0;

        while (scanner.hasNextLine()) {
            String numberLine = scanner.nextLine().trim();
            if (numberLine.isEmpty()) {
                continue;
            }
            int number = Integer.parseInt(numberLine);

            if (scanner.hasNextLine()) {
                String timeLine = scanner.nextLine().trim();

                if (scanner.hasNextLine()) {
                    StringBuilder textBuilder = new StringBuilder();
                    String textLine = scanner.nextLine().trim();
                    textBuilder.append(textLine);

                    while (scanner.hasNextLine()) {
                        String nextLine = scanner.nextLine().trim();
                        if (nextLine.isEmpty()) {
                            break;
                        }
                        textBuilder.append("\n").append(nextLine);
                    }

                    elements.add(new Element(number, timeLine, textBuilder.toString()));
                    subtitleCount++;
                }
            }
        }
        return subtitleCount;
    }


    public void find(String text) {
        for (Element e : elements) {
            if (e.findText(text)) {
                System.out.println(e.number);
            }
        }
    }

    public void print() {
        for (Element element : elements) {
            System.out.println(element);
        }

    }


    public void shift(int ms) {
        for (Element element : elements) {
            element.shift(ms);
        }
    }
}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}


